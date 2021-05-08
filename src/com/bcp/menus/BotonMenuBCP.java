package com.bcp.menus;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

public class BotonMenuBCP {

    private String titulo;
    private Drawable fondo;
    private ColorStateList textColor;
    private Drawable imageView;

    public BotonMenuBCP() {

    }

    public BotonMenuBCP(String titulo, Drawable fondo, ColorStateList textColor, Drawable imageView) {
        this.titulo = titulo;
        this.fondo = fondo;
        this.textColor = textColor;
        this.imageView = imageView;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Drawable getFondo() {
        return fondo;
    }

    public void setFondo(Drawable fondo) {
        this.fondo = fondo;
    }

    public ColorStateList getTextColor() {
        return textColor;
    }

    public void setTextColor(ColorStateList textColor) {
        this.textColor = textColor;
    }

    public Drawable getImageView() {
        return imageView;
    }

    public void setImageView(Drawable imageView) {
        this.imageView = imageView;
    }
}
