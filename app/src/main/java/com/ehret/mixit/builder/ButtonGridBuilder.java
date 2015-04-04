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
import android.widget.Button;

/**
 * TextView Builder utilsé pour peupler un GridLayout
 */
public class ButtonGridBuilder extends AbstractTextViewBuilder<Button, ButtonGridBuilder> {
    /**
     * Textview qui ser a construit
     */
    protected Button textView;


    /**
     * Retourne le textView construit
     */
    public Button getView() {
        return textView;
    }

    @Override
    protected void createView(Context context) {
        textView =  new Button(context);
    }

    @Override
    protected void setTypeBuilder() {
        builder = this;
    }
}
