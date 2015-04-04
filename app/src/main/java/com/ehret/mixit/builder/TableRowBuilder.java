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
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;

public class TableRowBuilder {

    /**
     * TableRow qui sera a construit
     */
    private TableRow tableRow;

    /**
     * Renvoi le Layout de type TableRow

     */
    public static LayoutParams getLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * Cree une nouvelle instance de {@link #tableRow}
     */
    public TableRowBuilder buildTableRow(Context context) {
        tableRow = new TableRow(context);
        //tableRow.setPadding(1, 1, 1, 1);
        return this;
    }

    /**
     * Specifie le nb colonne de {@link #tableRow}
     */
    public TableRowBuilder addNbColonne(int nb) {
        tableRow.setLayoutParams(new LayoutParams(nb));
        return this;
    }

    /**
     * Ajoute une couleur de fond a {@link #tableRow}
     */
    public TableRowBuilder addBackground(int color) {
        tableRow.setBackgroundColor(color);
        return this;
    }

    /**
     * Retourne le tableRow construit
     */
    public TableRow getView() {
        return tableRow;
    }
}
