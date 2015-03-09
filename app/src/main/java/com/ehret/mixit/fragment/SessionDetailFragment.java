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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.domain.talk.Lightningtalk;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.utils.UIUtils;
import com.github.rjeschke.txtmark.Processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Activité permettant d'afficher les informations sur un talk
 */
public class SessionDetailFragment extends Fragment {

    private ImageView image;
    private TextView horaire;
    private TextView level;
    private TextView levelTitle;
    private TextView name;
    private TextView summary;
    private TextView descriptif;
    private Button salle;
    private ImageView imageFavorite;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SessionDetailFragment newInstance(String typeAppel, Long message, int sectionNumber) {
        SessionDetailFragment fragment = new SessionDetailFragment();
        Bundle args = new Bundle();
        args.putString(UIUtils.ARG_LIST_TYPE, typeAppel);
        args.putLong(UIUtils.ARG_ID, message);
        args.putInt(UIUtils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_session, container, false);

        this.image = (ImageView) rootView.findViewById(R.id.talk_image);
        this.imageFavorite = (ImageView) rootView.findViewById(R.id.talk_image_favorite);
        this.horaire = (TextView) rootView.findViewById(R.id.talk_horaire);
        this.level = (TextView) rootView.findViewById(R.id.talk_level);
        this.levelTitle = (TextView) rootView.findViewById(R.id.talk_level_title);
        this.name = (TextView) rootView.findViewById(R.id.talk_name);
        this.summary = (TextView) rootView.findViewById(R.id.talk_summary);
        this.descriptif = (TextView) rootView.findViewById(R.id.talk_desciptif);
        this.salle = (Button) rootView.findViewById(R.id.talk_salle);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                "title_detail_" + getArguments().getString(UIUtils.ARG_LIST_TYPE),
                "color_" + getArguments().getString(UIUtils.ARG_LIST_TYPE),
                getArguments().getInt(UIUtils.ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    /**
     * Recuperation des marques de la partie en cours
     */
    @Override
    public void onResume() {
        super.onResume();

        Context context = getActivity().getBaseContext();

        //On commence par recuperer le Membre que l'on sohaite afficher
        Long id = getArguments().getLong(UIUtils.ARG_ID);
        String type = getArguments().getString(UIUtils.ARG_LIST_TYPE);

        Conference conference;
        if (TypeFile.lightningtalks.name().equals(type)) {
            conference = ConferenceFacade.getInstance().getLightningtalk(context, id);
            image.setImageDrawable(getResources().getDrawable(R.drawable.lightning));
        } else if (TypeFile.workshops.name().equals(type)) {
            conference = ConferenceFacade.getInstance().getTalk(context, id);
            image.setImageDrawable(getResources().getDrawable(R.drawable.workshop));
        } else {
            conference = ConferenceFacade.getInstance().getTalk(context, id);
            image.setImageDrawable(getResources().getDrawable(R.drawable.talk));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        if (conference.getStart() != null && conference.getEnd() != null) {
            horaire.setText(String.format(getResources().getString(R.string.periode),
                    sdf.format(conference.getStart()),
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(conference.getStart()),
                    DateFormat.getTimeInstance(DateFormat.SHORT).format(conference.getEnd())
            ));
        } else {
            horaire.setText(getResources().getString(R.string.pasdate));

        }
        if (conference instanceof Talk) {
            levelTitle.setText(getString(R.string.description_niveau));
            level.setText("[" + ((Talk) conference).getLevel() + "]");
        }
        else{
            levelTitle.setText(getString(R.string.description_votant));
            level.setText(""+((Lightningtalk) conference).getNbVotes());
        }
        name.setText(conference.getTitle());
        summary.setText(Html.fromHtml(Processor.process(conference.getSummary()).trim()));

        descriptif.setText(Html.fromHtml(Processor.process(conference.getDescription()).trim()), TextView.BufferType.SPANNABLE);
        Salle room = Salle.INCONNU;
        if (conference instanceof Talk) {
            room = Salle.getSalle(((Talk) conference).getRoom());
        }
        if (Salle.INCONNU != room) {
            salle.setText(String.format(getString(R.string.Salle), room.getNom()));
            if(room.getDrawable()!=0){
                salle.setBackgroundResource(room.getDrawable());
            }
            else{
                salle.setBackgroundColor(context.getResources().getColor(room.getColor()));
            }
            //TODO zoom salle
//            salle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    UIUtils.startActivity(Salle1Activity.class, talkActivity);
//                }
//            });
        }
    }

    /**
     * Icon change according to the session if it's present or not in the favorites
     */
    public void updateMenuItem(MenuItem item) {
        if (isTalkFavorite()) {
            //On affiche bouton pour l'enlever
            item.setTitle(R.string.description_favorite_del);
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_del_event));
            imageFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_important));
        }
        else {
            //On affiche bouton pour l'ajouter
            item.setTitle(R.string.description_favorite_add);
            item.setIcon(getResources().getDrawable(R.drawable.ic_action_add_event));
            imageFavorite.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_not_important));
        }
    }


    /**
     * Verifie si l'activité st dans les favoris
     */
    private boolean isTalkFavorite() {
        boolean trouve = false;
        SharedPreferences settings = getActivity().getSharedPreferences(UIUtils.PREFS_FAVORITES_NAME, 0);
        for (String key : settings.getAll().keySet()) {
            if (key.equals(String.valueOf(getArguments().getLong(UIUtils.ARG_ID)))) {
                trouve = true;
                break;
            }
        }
        return trouve;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.menu_favorites).setVisible(true);
        updateMenuItem(item);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorites) {
            //On recupere id
            SharedPreferences settings = getActivity().getSharedPreferences(UIUtils.PREFS_TEMP_NAME, 0);
            long id = settings.getLong("idTalk", 0L);
            if (id > 0) {
                //On sauvegarde le choix de l'utilsateur
                settings = getActivity().getSharedPreferences(UIUtils.PREFS_FAVORITES_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                if (isTalkFavorite()) {
                    //S'il l'est et on qu'on a cliquer sur le bouton on supprime
                    editor.remove(String.valueOf(id));
                    updateMenuItem(item);

                } else {
                    editor.putBoolean(String.valueOf(id), Boolean.TRUE);
                    updateMenuItem(item);
                }
                editor.commit();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
