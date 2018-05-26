package com.bloomers.tedxportsaid.CustomView.onboarder;


import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public class AhoyOnboarderCard {

    private String title;
    private String description;
    private Drawable imageResource;
    @StringRes
    private int titleResourceId;
    @StringRes
    private int descriptionResourceId;
    @DrawableRes
    private int imageResourceId;
    @ColorRes
    private int titleColor;
    @ColorRes
    private int descriptionColor;
    @ColorRes
    private int backgroundColor;

    private float titleTextSize;
    private float descriptionTextSize;
    private int iconWidth;
    private int iconHeight;
    private int marginTop;
    private int marginLeft;
    private int marginRight;
    private int marginBottom;

    public AhoyOnboarderCard(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public AhoyOnboarderCard(int title, int description) {
        this.titleResourceId = title;
        this.descriptionResourceId = description;
    }

    public AhoyOnboarderCard(String title, String description, int imageResourceId) {
        this.title = title;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public AhoyOnboarderCard(String title, String description, Drawable imageResource) {
        this.title = title;
        this.description = description;
        this.imageResource = imageResource;
    }

    public AhoyOnboarderCard(int title, int description, int imageResourceId) {
        this.titleResourceId = title;
        this.descriptionResourceId = description;
        this.imageResourceId = imageResourceId;
    }

    public AhoyOnboarderCard(int title, int description, Drawable imageResource) {
        this.titleResourceId = title;
        this.descriptionResourceId = description;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getTitleResourceId() {
        return titleResourceId;
    }

    public String getDescription() {
        return description;
    }

    public int getDescriptionResourceId() {
        return descriptionResourceId;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(@SuppressWarnings("SameParameterValue") int color) {
        this.titleColor = color;
    }

    public int getDescriptionColor() {
        return descriptionColor;
    }

    public void setDescriptionColor(@SuppressWarnings("SameParameterValue") int color) {
        this.descriptionColor = color;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public float getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
    }

    public float getDescriptionTextSize() {
        return descriptionTextSize;
    }

    public void setDescriptionTextSize(float descriptionTextSize) {
        this.descriptionTextSize = descriptionTextSize;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(@SuppressWarnings("SameParameterValue") int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconLayoutParams(int iconWidth, int iconHeight, int marginTop, int marginLeft, int marginRight, int marginBottom) {
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.marginLeft = marginLeft;
        this.marginRight = marginRight;
        this.marginTop = marginTop;
        this.marginBottom = marginBottom;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getMarginTop() {
        return marginTop;
    }

    public int getMarginLeft() {
        return marginLeft;
    }

    public int getMarginRight() {
        return marginRight;
    }

    public int getMarginBottom() {
        return marginBottom;
    }
}
