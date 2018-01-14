package com.pierfrancescosoffritti.chromecastyoutubesample.youTube;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.pierfrancescosoffritti.youtubeplayer.ui.PlayerUIController;
import com.pierfrancescosoffritti.youtubeplayer.ui.menu.YouTubePlayerMenu;


public abstract class DumbPlayerUI implements PlayerUIController {
    @Override
    public void showUI(boolean show) {

    }

    @Override
    public void showPlayPauseButton(boolean show) {

    }

    @Override
    public void showVideoTitle(boolean show) {

    }

    @Override
    public void setVideoTitle(@NonNull String videoTitle) {

    }

    @Override
    public void enableLiveVideoUI(boolean enable) {

    }

    @Override
    public void setCustomAction1(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener) {

    }

    @Override
    public void setCustomAction2(@NonNull Drawable icon, @Nullable View.OnClickListener clickListener) {

    }

    @Override
    public void showCustomAction1(boolean show) {

    }

    @Override
    public void showCustomAction2(boolean show) {

    }

    @Override
    public void showFullscreenButton(boolean show) {

    }

    @Override
    public void setCustomFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener) {

    }

    @Override
    public void showMenuButton(boolean show) {

    }

    @Override
    public void setCustomMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener) {

    }

    @Override
    public abstract void addView(@NonNull View view);

    @Override
    public abstract void removeView(@NonNull View view);

    @NonNull
    @Override
    public YouTubePlayerMenu getMenu() {
        return null;
    }

    @Override
    public void setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu) {

    }
}
