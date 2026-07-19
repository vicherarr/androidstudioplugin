package \${packageName}.ui.main

import \${packageName}.domain.model.Item

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val items: List<Item>) : MainUiState
    data class Error(val message: String) : MainUiState
}