/*
 * Copyright 2015 Guillaume EHRET
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ehret.mixit.builder;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * Classe abstraite pour Bulder de TextView
 */
public abstract class AbstractTextViewBuilder<TypeView extends TextView, TypeBuilder> {

    protected TypeBuilder builder;

    /**
     * Cree une nouvelle instance de 
     *
     * @param text
     * @return
     */
    public TypeBuilder addText(String text) {
        getView().setText(text);
        return builder;
    }

    /**
     * Cree une nouvelle instance de 
     *
     * @param text
     * @return
     */
    public TypeBuilder addText(int text) {
        getView().setText(text);
        return builder;
    }

    /**
     * Aligenemnt de 
     *
     * @param gravity
     * @return
     */
    public TypeBuilder addAlignement(int gravity) {
        getView().setGravity(gravity);
        return builder;
    }

    /**
     * WrapContent 
     *
     * @return
     */
    public TypeBuilder addStrike() {
        getView().setPaintFlags(getView().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        return builder;
    }

    public TypeBuilder addBold(boolean value) {
        getView().getPaint().setFakeBoldText(value);
        return builder;
    }

    /**
     * Cree une nouvelle instance de 
     *
     * @param context
     * @return
     */
    public TypeBuilder buildView(Context context) {
        setTypeBuilder();
        createView(context);
        getView().setPadding(4, 4, 4, 4);
        getView().setSingleLine(true);
        return builder;
    }

    /**
     * Size  
     *
     * @param size
     * @return
     */
    public TypeBuilder addSize(int size) {
        getView().setTextSize(size);
        return builder;
    }

    /**
     * Size  
     *
     * @param size
     * @return
     */
    public TypeBuilder addSize(int unit, int size) {
        getView().setTextSize(unit, size);
        return builder;
    }

    /**
     * Ajoute des marges internes a  
     *
     * @param left
     * @param right
     * @return
     */
    public TypeBuilder addPadding(int left, int right, int bottom) {
        getView().setPadding(left, 4, right, bottom);
        return builder;
    }

    /**
     * Met les caracteres en majuscules
     *
     * @return
     */
    public TypeBuilder addUpperCase() {
        getView().setAllCaps(true);
        return builder;
    }

    /**
     * Ajoute une couleur de fond a 
     *
     * @param color
     * @return
     */
    public TypeBuilder addBackground(int color) {
        getView().setBackgroundColor(color);
        return builder;
    }

    /**
     * Ajoute une couleur de fond a 
     * via un drawable
     *
     * @param drawable
     * @return
     */
    public TypeBuilder addBackgroundDrawable(int drawable) {
        getView().setBackgroundResource(drawable);
        return builder;
    }

    /**
     * Ajoute le nb de lignes acceptées
     *
     * @param nb
     * @return
     */
    public TypeBuilder addNbLines(int nb) {
        getView().setLines(nb);
        return builder;
    }

    /**
     * Ajoute le nb max de lignes acceptées
     *
     * @param nb
     * @return
     */
    public TypeBuilder addNbMaxLines(int nb) {
        getView().setSingleLine(false);
        getView().setMaxLines(nb);
        return builder;
    }

    /**
     * Ajoute une couleur de texte a 
     *
     * @param color
     * @return
     */
    public TypeBuilder addTextColor(int color) {
        getView().setTextColor(color);
        return builder;
    }


    /**
     * Retourne le textView construit
     *
     * @return
     */
    abstract public TypeView getView();

    /**
     * Retourne le textView construit
     *
     * @return
     */
    abstract protected void createView(Context context);


    abstract protected void setTypeBuilder();
    
}
