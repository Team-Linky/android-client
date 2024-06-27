package com.linky.feature.ask

enum class AskCategory {
    None, Error, Feature, More
}

val askCategories get() = arrayOf(AskCategory.Error, AskCategory.Feature, AskCategory.More)