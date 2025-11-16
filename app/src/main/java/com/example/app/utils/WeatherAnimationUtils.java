package com.example.app.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;


public class WeatherAnimationUtils {

    /**
     * Fade in animation for views
     */
    public static void fadeIn(View view, int duration) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(duration);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.start();
    }

    /**
     * Fade in animation with default duration
     */
    public static void fadeIn(View view) {
        fadeIn(view, 300);
    }

    /**
     * iOS-style fade in with scale animation
     */
    public static void fadeInWithScale(View view, float startScale, float endScale, int duration) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setScaleX(startScale);
        view.setScaleY(startScale);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", startScale, endScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", startScale, endScale);
        
        fadeIn.setDuration(duration);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        
        fadeIn.setInterpolator(new DecelerateInterpolator());
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        
        fadeIn.start();
        scaleX.start();
        scaleY.start();
    }

    /**
     * iOS-style fade in with slide animation
     */
    public static void fadeInWithSlide(View view, float startY, float endY, int duration) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setTranslationY(startY);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator slideY = ObjectAnimator.ofFloat(view, "translationY", startY, endY);
        
        fadeIn.setDuration(duration);
        slideY.setDuration(duration);
        
        fadeIn.setInterpolator(new DecelerateInterpolator());
        slideY.setInterpolator(new DecelerateInterpolator());
        
        fadeIn.start();
        slideY.start();
    }

    /**
     * Slide up animation for views
     */
    public static void slideUp(View view, int duration) {
        if (view == null) return;
        
        view.setTranslationY(view.getHeight());
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0f);
        slideUp.setDuration(duration);
        slideUp.setInterpolator(new DecelerateInterpolator());
        slideUp.start();
    }

    /**
     * Slide up animation with default duration
     */
    public static void slideUp(View view) {
        slideUp(view, 400);
    }

    /**
     * Scale in animation for views
     */
    public static void scaleIn(View view, int duration) {
        if (view == null) return;
        
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);
        alpha.setDuration(duration);
        
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        alpha.setInterpolator(new DecelerateInterpolator());
        
        scaleX.start();
        scaleY.start();
        alpha.start();
    }

    /**
     * Scale in animation with default duration
     */
    public static void scaleIn(View view) {
        scaleIn(view, 300);
    }

    /**
     * Animate temperature change
     */
    public static void animateTemperatureChange(View temperatureView, String newTemperature) {
        if (temperatureView == null) return;
        
        // Scale animation for temperature change
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(temperatureView, "scaleX", 1f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(temperatureView, "scaleY", 1f, 1.1f, 1f);
        
        scaleX.setDuration(600);
        scaleY.setDuration(600);
        
        scaleX.setInterpolator(new DecelerateInterpolator());
        scaleY.setInterpolator(new DecelerateInterpolator());
        
        scaleX.start();
        scaleY.start();
    }

    /**
     * iOS-style temperature change with spring animation
     */
    public static void animateTemperatureChangeWithSpring(View temperatureView, String newTemperature) {
        if (temperatureView == null) return;
        
        // Spring-like scale animation for temperature change
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(temperatureView, "scaleX", 1f, 1.15f, 0.95f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(temperatureView, "scaleY", 1f, 1.15f, 0.95f, 1f);
        
        scaleX.setDuration(800);
        scaleY.setDuration(800);
        
        scaleX.setInterpolator(new OvershootInterpolator(0.8f));
        scaleY.setInterpolator(new OvershootInterpolator(0.8f));
        
        scaleX.start();
        scaleY.start();
    }

    /**
     * Animate weather icon rotation
     */
    public static void animateWeatherIcon(View iconView) {
        if (iconView == null) return;
        
        ObjectAnimator rotation = ObjectAnimator.ofFloat(iconView, "rotation", 0f, 360f);
        rotation.setDuration(1000);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.start();
    }

    /**
     * iOS-style weather icon animation with bounce
     */
    public static void animateWeatherIconWithBounce(View iconView) {
        if (iconView == null) return;
        
        // Scale and rotation animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(iconView, "scaleX", 0.8f, 1.1f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(iconView, "scaleY", 0.8f, 1.1f, 1f);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(iconView, "rotation", -10f, 10f, 0f);
        
        scaleX.setDuration(600);
        scaleY.setDuration(600);
        rotation.setDuration(600);
        
        scaleX.setInterpolator(new OvershootInterpolator(0.8f));
        scaleY.setInterpolator(new OvershootInterpolator(0.8f));
        rotation.setInterpolator(new DecelerateInterpolator());
        
        scaleX.start();
        scaleY.start();
        rotation.start();
    }

    /**
     * Animate card entrance with staggered effect
     */
    public static void animateCardEntrance(View cardView, int delay) {
        if (cardView == null) return;
        
        cardView.setTranslationY(50f);
        cardView.setAlpha(0f);
        cardView.setVisibility(View.VISIBLE);
        
        ObjectAnimator translateY = ObjectAnimator.ofFloat(cardView, "translationY", 50f, 0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);
        
        translateY.setStartDelay(delay);
        alpha.setStartDelay(delay);
        
        translateY.setDuration(500);
        alpha.setDuration(500);
        
        translateY.setInterpolator(new DecelerateInterpolator());
        alpha.setInterpolator(new DecelerateInterpolator());
        
        translateY.start();
        alpha.start();
    }

    /**
     * iOS-style card entrance with spring effect
     */
    public static void animateCardEntranceWithSpring(View cardView, int delay) {
        if (cardView == null) return;
        
        cardView.setTranslationY(80f);
        cardView.setAlpha(0f);
        cardView.setScaleX(0.9f);
        cardView.setScaleY(0.9f);
        cardView.setVisibility(View.VISIBLE);
        
        ObjectAnimator translateY = ObjectAnimator.ofFloat(cardView, "translationY", 80f, 0f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(cardView, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(cardView, "scaleY", 0.9f, 1f);
        
        translateY.setStartDelay(delay);
        alpha.setStartDelay(delay);
        scaleX.setStartDelay(delay);
        scaleY.setStartDelay(delay);
        
        translateY.setDuration(600);
        alpha.setDuration(600);
        scaleX.setDuration(600);
        scaleY.setDuration(600);
        
        translateY.setInterpolator(new OvershootInterpolator(0.8f));
        alpha.setInterpolator(new DecelerateInterpolator());
        scaleX.setInterpolator(new OvershootInterpolator(0.8f));
        scaleY.setInterpolator(new OvershootInterpolator(0.8f));
        
        translateY.start();
        alpha.start();
        scaleX.start();
        scaleY.start();
    }

    /**
     * Animate refresh button
     */
    public static void animateRefreshButton(View refreshButton) {
        if (refreshButton == null) return;
        
        ObjectAnimator rotation = ObjectAnimator.ofFloat(refreshButton, "rotation", 0f, 360f);
        rotation.setDuration(500);
        rotation.setInterpolator(new DecelerateInterpolator());
        rotation.start();
    }

    /**
     * Pulse animation for loading indicators
     */
    public static ValueAnimator createPulseAnimation(View view) {
        if (view == null) return null;
        
        ValueAnimator pulseAnimator = ValueAnimator.ofFloat(0.8f, 1.2f);
        pulseAnimator.setDuration(1000);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        
        pulseAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            view.setScaleX(scale);
            view.setScaleY(scale);
        });
        
        return pulseAnimator;
    }

    /**
     * Apply entrance animations to multiple views with staggered timing
     */
    public static void animateViewsEntrance(View... views) {
        for (int i = 0; i < views.length; i++) {
            if (views[i] != null) {
                animateCardEntrance(views[i], i * 100);
            }
        }
    }

    /**
     * iOS-style entrance animations with spring effect
     */
    public static void animateViewsEntranceWithSpring(View... views) {
        for (int i = 0; i < views.length; i++) {
            if (views[i] != null) {
                animateCardEntranceWithSpring(views[i], i * 150);
            }
        }
    }

    /**
     * Load animation from resources
     */
    public static Animation loadAnimation(Context context, int animationResource) {
        return android.view.animation.AnimationUtils.loadAnimation(context, animationResource);
    }

    /**
     * Apply bounce effect to view
     */
    public static void bounceView(View view) {
        if (view == null) return;
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.9f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.9f, 1f);
        
        scaleX.setDuration(150);
        scaleY.setDuration(150);
        
        scaleX.start();
        scaleY.start();
    }

    /**
     * iOS-style bounce effect with spring
     */
    public static void bounceViewWithSpring(View view) {
        if (view == null) return;
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.85f, 1.05f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.85f, 1.05f, 1f);
        
        scaleX.setDuration(300);
        scaleY.setDuration(300);
        
        scaleX.setInterpolator(new OvershootInterpolator(0.8f));
        scaleY.setInterpolator(new OvershootInterpolator(0.8f));
        
        scaleX.start();
        scaleY.start();
    }
}