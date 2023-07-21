package com.linky.link_detail_input.model

import com.kedia.ogparser.OpenGraphResult
import com.linky.model.open_graph.OpenGraphData

fun OpenGraphResult.toOpenGraphData(): OpenGraphData = OpenGraphData(
    title = title,
    description = description,
    url = url,
    image = image,
    siteName = siteName,
    type = type
)