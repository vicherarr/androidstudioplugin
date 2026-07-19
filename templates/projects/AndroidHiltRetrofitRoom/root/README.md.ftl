# \${appTitle}

Android project generated using **Android Studio Template (Hilt + Retrofit + Room)**.

## Architecture
- **UI Layer**: Jetpack Compose, Material3, ViewModels, \`UiState\` flow.
- **Domain Layer**: UseCases encapsulating business logic.
- **Data Layer**:
  - **Local**: Room Database, DAOs, Entities with KSP.
  - **Remote**: Retrofit, OkHttp Logging Interceptor, \`kotlinx.serialization\`.
  - **Repository**: Single source of truth coordinating local and remote sources.
- **DI**: Dagger Hilt for dependency injection.
