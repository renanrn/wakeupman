# Research Report: Samsung S26 Ultra Battery Intent Failure

**Date:** 2026-05-09
**Subject:** Falha silenciosa do botão de otimização de bateria na interface One UI 6 (Samsung S26 Ultra, Android 14).
**Analyst:** Atlas (@analyst)

## 1. Problem Definition
O usuário reportou que, mesmo após a implementação de um mecanismo de `try/catch` com múltiplos fallbacks no `BatteryOptimizationGuideScreen.kt`, o botão "ABRIR CONFIGURAÇÕES DE BATERIA" continua sem executar nenhuma ação (não abre a tela correspondente) no dispositivo **Samsung S26 Ultra**.

## 2. Root Cause Analysis

### O ecossistema Samsung (One UI 6+ / Android 14)
A Samsung personaliza profundamente o subsistema de energia do Android (Device Care / "Assistência do aparelho e bateria"). Como resultado:
1.  **Bloqueio de Intent Direto:** O Intent padrão `Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` muitas vezes é suprimido silenciosamente ou não resolve para nenhuma Activity exportada na One UI. Como não lança uma exceção tradicional (como `ActivityNotFoundException`), o bloco `catch` do nosso código **nunca é acionado**.
2.  **Redirecionamento Interno:** Quando a chamada passa sem erro (mas falha em abrir a UI), os Intents de fallback (`ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS` e `ACTION_APPLICATION_DETAILS_SETTINGS`) não são tentados.
3.  **Restrições de Contexto:** A abertura de Activitys a partir de contextos Compose ou serviços em background no Android 14 (sem a flag `FLAG_ACTIVITY_NEW_TASK`) pode ser suprimida pelo sistema de segurança.

## 3. Investigation Findings
O código atual (Story Bug-1.0.1) implementou a lógica correta sob a perspectiva do SDK padrão do Android:
```kotlin
try {
    context.startActivity(Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)...)
} catch (e: Exception) {
    // Tenta fallback...
}
```
No entanto, a premissa de que a falha gera uma `Exception` é o calcanhar de Aquiles aqui. O sistema resolve o Intent, envia para um Broadcast Receiver interno ou Activity "fantasma" da Samsung, e a execução morre sem que a nossa aplicação saiba, impedindo a cascata de fallbacks.

## 4. Architect Recommendations
Repasso este problema ao **@architect** com a forte recomendação do usuário: **utilizar uma abordagem híbrida/combinada.**

O design da solução deve contemplar:
1.  **Deteção da OEM (Opcional):** Tentar identificar se o dispositivo é Samsung e utilizar o Intent explícito deles (`com.samsung.android.lool`).
2.  **Verificação de Resolução do Intent:** Em vez de depender do `try-catch`, usar o `PackageManager` (`intent.resolveActivity(packageManager)`) para verificar proativamente se a tela *pode* ser aberta antes de chamar o `startActivity()`. Isso forçará os fallbacks se a Samsung tiver removido ou desativado a Activity principal.
3.  **Garantia de Navegação (Fallback Seguro):** A tela de `ACTION_APPLICATION_DETAILS_SETTINGS` sempre existe. O @architect deve desenhar a lógica para que este seja o destino à prova de falhas garantido, utilizando as flags corretas (`Intent.FLAG_ACTIVITY_NEW_TASK`).

**Conclusão da Análise:** O botão falha não por um "crash", mas por um silenciamento da API pela customização agressiva da fabricante. O fluxo precisa ser proativo e não reativo a exceções.