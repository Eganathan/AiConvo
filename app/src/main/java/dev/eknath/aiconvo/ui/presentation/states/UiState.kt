package dev.eknath.aiconvo.ui.presentation.states


sealed interface UiState {
    data object Initial : UiState
    data object Loading : UiState
    data object Success : UiState
    data object Error : UiState

}