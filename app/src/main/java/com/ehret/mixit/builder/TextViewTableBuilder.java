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
import android.view.ViewGroup;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class TextViewTableBuilder extends AbstractTextViewBuilder<TextView, TextViewTableBuilder>  {
    /**
     * Textview qui ser a construit
     */
    protected TextView textView;

    /**
     * Fusionne nbcol  {@link #textView}
     */
    public TextViewTableBuilder addSpan(int nbcol) {
        LayoutParams params = getLayoutParams(LayoutParams.MATCH_PARENT);
        params.span = nbcol;
        return this;
    }

    /**
     * Renvoi le Layout de type TableRow
     */
    private LayoutParams getLayoutParams(int match) {
        return getLayoutParams(match, 1f);
    }

    /**
     * Renvoi le Layout de type TableRow
     */
    private LayoutParams getLayoutParams(int match, float weight) {
        ViewGroup.LayoutParams params = getView().getLayoutParams();
        if (params == null || !(params instanceof LayoutParams)) {
            LayoutParams param2s = new LayoutParams(match, LayoutParams.MATCH_PARENT, weight);
            getView().setLayoutParams(param2s);
            return param2s;
        }
        return (LayoutParams) params;
    }

    /**
     * Ajoute des bordures a {@link #textView}
     */
    public TextViewTableBuilder addBorders(boolean borderLeft, boolean borderRight,
                                      boolean borderBottom, boolean borderTop) {

        int bottom = borderBottom ? 1 : 0;
        int right = borderRight ? 1 : 0;
        int left = borderLeft ? 1 : 0;
        int top = borderTop ? 1 : 0;

        LayoutParams params = getLayoutParams(LayoutParams.MATCH_PARENT);
        params.setMargins(left, top, right, bottom);
        return this;
    }


    /**
     * Retourne le textView construit
     *
     * @return
     */
    public TextView getView() {
        return textView;
    }

    @Override
    protected void createView(Context context) {
        textView =  new TextView(context);
    }

    @Override
    protected void setTypeBuilder() {
        builder = this;
    }
}
