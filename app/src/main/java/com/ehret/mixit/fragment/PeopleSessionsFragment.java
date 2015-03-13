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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.UIUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Ce fragment permet d'afficher les sessions d'un user
 */
public class PeopleSessionsFragment extends Fragment {

    private ViewGroup mRootView;
    private LayoutInflater mInflater;
    private LinearLayout linearLayoutRoot;
    private TextView name;
    private TextView descriptif;
    private ImageView image;
    private TextView level;
    private TextView horaire;
    private TextView talkImageText;
    private TextView talkSalle;

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
        if (membre != null) {
            //On recupere aussi la liste des sessions de l'utilisateur
            List<Conference> conferences = ConferenceFacade.getInstance().getSessionMembre(membre, getActivity());

            //On affiche les liens que si on a recuperer des choses
            if (conferences != null && !conferences.isEmpty()) {
                //On utilisait auparavant une liste pour afficher ces éléments dans la page mais cette liste
                //empêche d'avoir un ScrollView englobant pour toute la page. Nous utilisons donc un tableau
                linearLayoutRoot = (LinearLayout) mInflater.inflate(R.layout.layout_linear, mRootView, false);

                //On vide les éléments
                linearLayoutRoot.removeAllViews();

                linearLayoutRoot.addView(new TextViewTableBuilder()
                        .buildView(getActivity())
                        .addText(getString(R.string.description_sessions))
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

                for (final Conference conf : conferences) {
                    LinearLayout row = (LinearLayout) mInflater.inflate(R.layout.item_talk, null);
                    row.setBackgroundResource(R.drawable.row_transparent_background);
                    //Dans lequel nous allons ajouter le contenu que nous faisons mappé dans
                    image = (ImageView) row.findViewById(R.id.talk_image);
                    name = (TextView) row.findViewById(R.id.talk_name);
                    descriptif = (TextView) row.findViewById(R.id.talk_shortdesciptif);
                    level = (TextView) row.findViewById(R.id.talk_level);
                    horaire = (TextView) row.findViewById(R.id.talk_horaire);
                    talkImageText = (TextView) row.findViewById(R.id.talkImageText);
                    talkSalle = (TextView) row.findViewById(R.id.talk_salle);

                    name.setText(conf.getTitle());
                    descriptif.setText(conf.getSummary().trim());
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                    if (conf.getStart() != null && conf.getEnd() != null) {
                        horaire.setText(String.format(getResources().getString(R.string.periode),
                                sdf.format(conf.getStart()),
                                DateFormat.getTimeInstance(DateFormat.SHORT).format(conf.getStart()),
                                DateFormat.getTimeInstance(DateFormat.SHORT).format(conf.getEnd())
                        ));
                    } else {
                        horaire.setText(getResources().getString(R.string.pasdate));

                    }
                    Salle salle = Salle.INCONNU;
                    if (conf instanceof Talk && Salle.INCONNU != Salle.getSalle(((Talk) conf).getRoom())) {
                        salle = Salle.getSalle(((Talk) conf).getRoom());
                    }
                    talkSalle.setText(String.format(getResources().getString(R.string.Salle), salle.getNom()));
                    talkSalle.setBackgroundColor(getResources().getColor(salle.getColor()));


                    if (conf instanceof Talk) {
                        level.setText("[" + ((Talk) conf).getLevel() + "]");

                        if ("Workshop".equals(((Talk) conf).getFormat())) {
                            talkImageText.setText("Workshop");
                            image.setImageDrawable(getResources().getDrawable(R.drawable.workshop));
                        } else {
                            talkImageText.setText("Talk");
                            image.setImageDrawable(getResources().getDrawable(R.drawable.talk));
                        }
                    } else {
                        talkImageText.setText("L.Talk");
                        image.setImageDrawable(getResources().getDrawable(R.drawable.lightning));
                    }

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Map<String, Object> parameters = new HashMap<String, Object>(2);
                            parameters.put(UIUtils.ARG_ID, conf.getId());
                            TypeFile typeFile = null;
                            if (conf instanceof Talk) {
                                if ("Workshop".equals(((Talk) conf).getFormat())) {
                                    typeFile = TypeFile.workshops;
                                } else {
                                    typeFile = TypeFile.talks;
                                }
                            } else {
                                typeFile = TypeFile.lightningtalks;
                            }
                            //Todo display the talk for a speaker
                            //parameters.put(UIUtils.TYPE, typeFile);
                            // UIUtils.startActivity(TalkActivity.class, getActivity(), parameters);
                        }
                    });

                    tableLayout.addView(row);
                }

                linearLayoutRoot.addView(tableLayout);
                mRootView.addView(linearLayoutRoot);
            }
        }
    }



}
