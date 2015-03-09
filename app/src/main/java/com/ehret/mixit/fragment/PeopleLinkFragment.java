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

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Link;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.UIUtils;


/**
 * Ce fragment permet d'afficher les différents links qu'une des personnes référenceés
 * sous Mixit a partage
 */
public class PeopleLinkFragment extends Fragment {
    private ViewGroup mRootView;
    private LayoutInflater mInflater;
    private String typePersonne;
    private Long idPerson;
    private TextView link_text;
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

        idPerson = getParentFragment().getArguments().getLong(UIUtils.ARG_ID);
        typePersonne = getParentFragment().getArguments().getString(UIUtils.ARG_LIST_TYPE);

        //On recupere la personne concernee
        Membre membre = MembreFacade.getInstance().getMembre(getActivity(), typePersonne, idPerson);
        if(membre==null){
            membre = MembreFacade.getInstance().getMembre(getActivity(), TypeFile.members.name(), idPerson);
        }
        //On affiche les liens que si on a recuperer des choses
        if (membre!=null && membre.getSharedLinks() != null && !membre.getSharedLinks().isEmpty()) {
            linearLayoutRoot = (LinearLayout) mInflater.inflate(R.layout.layout_linear, mRootView, false);
            //On vide les éléments
            linearLayoutRoot.removeAllViews();

            linearLayoutRoot.addView(new TextViewTableBuilder()
                    .buildView(getActivity())
                    .addText(getString(R.string.description_liens))
                    .addPadding(0, 10, 4)
                    .addBold(true)
                    .addUpperCase()
                    .addSize(TypedValue.COMPLEX_UNIT_SP, getResources().getInteger(R.integer.text_size_cal))
                    .addTextColor(getResources().getColor(R.color.black))
                    .getView());

            //On ajoute un table layout
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout tableLayout = new TableLayout(getActivity().getBaseContext());
            tableLayout.setLayoutParams(tableParams);

            for (final Link link : membre.getSharedLinks()) {
                RelativeLayout row = (RelativeLayout) mInflater.inflate(R.layout.item_link, null);
                row.setBackgroundResource(R.drawable.row_transparent_background);
                //Dans lequel nous allons ajouter le contenu que nous faisons mappé dans
                link_text = (TextView) row.findViewById(R.id.link_text);
                link_text.setText(Html.fromHtml(String.format("%s : <a href=\"%s\">%s</a>", link.getName(), link.getUrl(), link.getUrl())));
                link_text.setBackgroundColor(Color.TRANSPARENT);
                link_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(link.getUrl()));
                        getActivity().startActivity(in);
                    }

                });
                tableLayout.addView(row);
            }


            linearLayoutRoot.addView(tableLayout);
            mRootView.addView(linearLayoutRoot);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("ID_PERSON_LINK", idPerson);
        outState.putString("TYPE_PERSON_LINK", typePersonne);
    }
}
