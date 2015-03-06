/*
 * Copyright 2014 Guillaume EHRET
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
package com.ehret.mixit.domain;

import com.ehret.mixit.R;

/**
 * Created with IntelliJ IDEA.
 * User: EHRET_G
 * Date: 04/04/13
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public enum Salle {
    SALLE1("Gosling", R.color.salle1, R.drawable.salle1_background,0),
    SALLE2("Eich", R.color.salle2, R.drawable.salle2_background,0),
    SALLE3("Nonaka", R.color.salle3, R.drawable.salle3_background,0),
    SALLE4("Dijkstra", R.color.salle4, R.drawable.salle4_background,0),
    SALLE5("Turing", R.color.salle5, R.drawable.salle5_background,0),
    SALLE6("Lovelace", R.color.salle6, R.drawable.salle6_background,0),
    SALLE7("G. Amphi", R.color.salle7, R.drawable.salle7_background,1),
    SALLE8("P. Amphi", R.color.salle8, R.drawable.salle8_background,1),
    SALLE9("Mezzanine", R.color.salle9, R.drawable.salle9_background,1),
    INCONNU("Inconnue", R.color.grey, 0,0);

    private String nom;
    private int color;
    private int drawable;
    private int etage;

    private Salle(String nom, int color,int drawable, int etage) {
        this.nom = nom;
        this.color = color;
        this.drawable=drawable;
        this.etage=etage;
    }

    public String getNom() {
        return nom;
    }

    public int getColor() {
        return color;
    }
    public int getDrawable() {
        return drawable;
    }
    public static Salle getSalle(String nom) {
        if (nom != null) {
            for (Salle salle : values()) {
                if (salle.getNom().equals(nom)) {
                    return salle;
                }
            }
        }
        return INCONNU;
    }

    public int getEtage() {
        return etage;
    }
}
