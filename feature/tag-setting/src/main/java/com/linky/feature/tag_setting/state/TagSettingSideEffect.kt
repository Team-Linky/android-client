package com.linky.feature.tag_setting.state

sealed interface TagSettingSideEffect {

    data object ClearSelectTagList : TagSettingSideEffect

    data class SnackBar(
        val message: String,
        val snackBarAction: SnackBarAction? = null
    ) : TagSettingSideEffect

}

sealed interface SnackBarAction {
    data object UnDoDelete : SnackBarAction
}