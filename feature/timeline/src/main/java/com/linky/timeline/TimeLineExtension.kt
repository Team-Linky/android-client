package com.linky.timeline

import androidx.activity.ComponentActivity
import com.linky.model.Link
import com.linky.webview.extension.launchWebViewActivity

internal fun ComponentActivity.launchTimeLinePage(link: Link, viewModel: TimeLineViewModel) {
    link.openGraphData.url?.also { url ->
        launchWebViewActivity(url)
    }
}