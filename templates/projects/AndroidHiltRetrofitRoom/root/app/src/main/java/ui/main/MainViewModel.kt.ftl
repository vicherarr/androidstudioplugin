package \${packageName}.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import \${packageName}.domain.usecase.GetItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getItemsUseCase: GetItemsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeItems()
        refreshData()
    }

    private fun observeItems() {
        viewModelScope.launch {
            getItemsUseCase()
                .catch { _uiState.value = MainUiState.Error(it.message ?: "Unknown error") }
                .collect { items ->
                    _uiState.value = MainUiState.Success(items)
                }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            val result = getItemsUseCase.refresh()
            if (result.isFailure) {
                if (_uiState.value is MainUiState.Loading) {
                    _uiState.value = MainUiState.Error(
                        result.exceptionOrNull()?.message ?: "Failed to load data"
                    )
                }
            }
        }
    }
}