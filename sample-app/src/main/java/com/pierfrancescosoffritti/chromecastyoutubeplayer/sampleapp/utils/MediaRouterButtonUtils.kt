package com.pierfrancescosoffritti.chromecastyoutubeplayer.sampleapp.utils

import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.MediaRouteButton
import android.support.v7.mediarouter.R
import android.view.ContextThemeWrapper
import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController

object MediaRouterButtonUtils {
    fun addMediaRouteButtonToPlayerUI(
            mediaRouteButton: MediaRouteButton, tintColor: Int,
            uiControllerDisabled: PlayerUIController?, uiControllerActivated: PlayerUIController) {

        setMediaRouterButtonTint(mediaRouteButton, tintColor)

        uiControllerDisabled?.removeView(mediaRouteButton)
        if(mediaRouteButton.parent != null) return
        uiControllerActivated.addView(mediaRouteButton)
    }

    private fun setMediaRouterButtonTint(mediaRouterButton: MediaRouteButton, color: Int) {
        val castContext = ContextThemeWrapper(mediaRouterButton.context, R.style.Theme_MediaRouter)
        val styledAttributes = castContext.obtainStyledAttributes(null, android.support.v7.mediarouter.R.styleable.MediaRouteButton, android.support.v7.mediarouter.R.attr.mediaRouteButtonStyle, 0)
        val drawable = styledAttributes.getDrawable(android.support.v7.mediarouter.R.styleable.MediaRouteButton_externalRouteEnabledDrawable)

        styledAttributes.recycle()
        DrawableCompat.setTint(drawable, ContextCompat.getColor(mediaRouterButton.context, color))

        mediaRouterButton.setRemoteIndicatorDrawable(drawable)
    }
}