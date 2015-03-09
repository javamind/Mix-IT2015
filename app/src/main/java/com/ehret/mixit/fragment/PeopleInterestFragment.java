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
package com.ehret.mixit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Interet;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.UIUtils;


/**
 * Ce fragment permet d'afficher les différents interets qu'une des personnes référenceés
 * sous Mixit a partage
 */
public class PeopleInterestFragment extends Fragment {

    private ViewGroup mRootView;
    private LayoutInflater mInflater;
    private LinearLayout linearLayoutRoot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        mRootView = (ViewGroup) inflater.inflate(R.layout.layout_list, container);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Long idPerson = getParentFragment().getArguments().getLong(UIUtils.ARG_ID);
        String typePersonne = getParentFragment().getArguments().getString(UIUtils.ARG_LIST_TYPE);

        //On recupere la personne concernee
        Membre membre = MembreFacade.getInstance().getMembre(getActivity(), typePersonne, idPerson);
        if (membre == null) {
            membre = MembreFacade.getInstance().getMembre(getActivity(), TypeFile.members.name(), idPerson);
        }
        //On affiche les liens que si on a recuperer des choses
        if (membre != null && membre.getInterests() != null && !membre.getInterests().isEmpty()) {
            linearLayoutRoot = (LinearLayout) mInflater.inflate(R.layout.layout_linear, mRootView, false);
            //On vide les éléments
            linearLayoutRoot.removeAllViews();

            linearLayoutRoot.addView(new TextViewTableBuilder()
                    .buildView(getActivity())
                    .addText(getString(R.string.description_interet))
                    .addPadding(0, 10, 4)
                    .addBold(true)
                    .addUpperCase()
                    .addSize(TypedValue.COMPLEX_UNIT_SP, getResources().getInteger(R.integer.text_size_cal))
                    .addTextColor(getResources().getColor(R.color.black))
                    .getView());

            StringBuffer interets = new StringBuffer();
            for (final Long iidInteret : membre.getInterests()) {
                Interet interet = MembreFacade.getInstance().getInteret(getActivity(), iidInteret);
                if (interet != null) {
                    if (interets.length() > 0) {
                        interets.append(", ");
                    }
                    interets.append(interet.getName());
                }
            }
            TextView text = new TextViewTableBuilder()
                    .buildView(getActivity())
                    .addText(interets.toString())
                    .addPadding(4, 10, 4)
                    .addSize(TypedValue.COMPLEX_UNIT_SP, getResources().getInteger(R.integer.text_size_cal))
                    .addTextColor(getResources().getColor(R.color.black))
                    .getView();
            text.setSingleLine(false);
            linearLayoutRoot.addView(text);
            mRootView.addView(linearLayoutRoot);
        }
    }

}
