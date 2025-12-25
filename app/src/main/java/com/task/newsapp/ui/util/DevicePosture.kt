package com.task.newsapp.ui.util

import androidx.window.core.layout.WindowSizeClass


enum class DevicePosture {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    BIG_SCREEN;

    companion object {
        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DevicePosture {
            val isWidthExpanded =
                windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
            val isHeightExpanded =
                windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)
            val isWidthMedium =
                windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
            val isHeightMedium =
                windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)

            return when {

                !isWidthMedium && (isHeightMedium || isHeightExpanded) -> MOBILE_PORTRAIT
                isWidthMedium && !isHeightMedium -> MOBILE_LANDSCAPE
                isWidthMedium && isHeightExpanded -> TABLET_PORTRAIT
                isWidthExpanded && isHeightMedium -> TABLET_LANDSCAPE
                else -> BIG_SCREEN
            }
        }
    }
}