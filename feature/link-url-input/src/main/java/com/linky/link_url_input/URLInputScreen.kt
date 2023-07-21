package com.linky.link_url_input

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.linky.design_system.ui.component.textfield.addFocusCleaner
import com.linky.design_system.ui.theme.LinkyLinkTheme
import com.linky.link_url_input.component.URLInputContent
import com.linky.link_url_input.component.URLInputHeader
import com.linky.navigation.link.LinkNavType
import okio.ByteString.Companion.encodeUtf8
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

fun NavGraphBuilder.urlInputScreen(navController: NavController) {
    composable(LinkNavType.URLInput.route) {
        val activity = LocalContext.current as ComponentActivity

        URLInputRoute(
            onNext = { url ->
                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate(route = LinkNavType.DetailInput.route.replace("{url}", encodedUrl)) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onBack = activity::finish
        )
    }
}

@Composable
private fun URLInputRoute(
    onNext: (String) -> Unit,
    onBack: () -> Unit
) {
    URLInputScreen(
        onNext = onNext,
        onBack = onBack
    )
}

@Composable
private fun URLInputScreen(
    onNext: (String) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var text by rememberSaveable { mutableStateOf("") }
    var isFocus by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current
    val onBackPressCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBack.invoke()
            }
        }
    }

    DisposableEffect(backDispatcher, onBackPressCallback) {
        backDispatcher?.onBackPressedDispatcher?.addCallback(onBackPressCallback)
        onDispose {
            onBackPressCallback.remove()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        URLInputHeader(
            isNextActive = !isFocus && text.isNotEmpty(),
            onNext = { onNext.invoke(text) },
            onBack = onBack,
        )
        URLInputContent(
            value = text,
            hasClipBoard = clipboardManager.hasText(),
            onValueChange = { text = it },
            onClear = { text = "" },
            focusManager = focusManager,
            focusRequester = focusRequester,
            onFocusChanged = { isFocus = it.isFocused },
            onClipBoardPaste = {
                val clipboardText = clipboardManager.getText()?.text ?: ""
                if (clipboardText.isNotEmpty()) {
                    text = clipboardText
                }
            }
        )
    }
}

@Preview
@Composable
private fun URLInputPreview() {
    LinkyLinkTheme {
        URLInputScreen()
    }
}