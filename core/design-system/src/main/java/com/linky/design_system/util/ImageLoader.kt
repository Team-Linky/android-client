package com.linky.design_system.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache

private const val DISK_CACHE_DIRECTORY_RELATIVE = "linky_image_cache"
private const val DISK_CACHE_MAX_SIZE_PERCENT = 0.2
private const val MEMORY_CACHE_MAX_SIZE_PERCENT = 0.25

@Composable
fun rememberImageLoader(
    context: Context = LocalContext.current,
    key: Any = Unit,
    relative: String = DISK_CACHE_DIRECTORY_RELATIVE,
    memoryMaxSizePercent: Double = MEMORY_CACHE_MAX_SIZE_PERCENT,
    diskMaxSizePercent: Double = DISK_CACHE_MAX_SIZE_PERCENT,
    option: ImageLoader.Builder.() -> Unit = {},
): ImageLoader = remember(key1 = key) {
    ImageLoader.Builder(context)
        .memoryCache {
            MemoryCache.Builder(context)
                .maxSizePercent(memoryMaxSizePercent)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(context.cacheDir.resolve(relative))
                .maxSizePercent(diskMaxSizePercent)
                .build()
        }
        .apply(option)
}.build()