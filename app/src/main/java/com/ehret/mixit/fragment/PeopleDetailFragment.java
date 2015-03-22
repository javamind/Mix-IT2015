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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Interet;
import com.ehret.mixit.domain.people.Link;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.FileUtils;
import com.ehret.mixit.utils.UIUtils;
import com.github.rjeschke.txtmark.Processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Activity permettant d'afficher les informations sur une personne participant à Mix-IT
 */
public class PeopleDetailFragment extends Fragment {

    private ImageView profileImage;
    private ImageView logoImage;
    private TextView membreUserName;
    private TextView personDesciptif;
    private TextView personShortDesciptif;
    private TextView membreEntreprise;
    private LinearLayout interestLayout;
    private LinearLayout linkLayout;
    private LinearLayout sessionLayout;
    private LayoutInflater mInflater;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PeopleDetailFragment newInstance(String typeAppel, Long message, int sectionNumber) {
        PeopleDetailFragment fragment = new PeopleDetailFragment();
        Bundle args = new Bundle();
        args.putString(UIUtils.ARG_LIST_TYPE, typeAppel);
        args.putLong(UIUtils.ARG_ID, message);
        args.putInt(UIUtils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_people, container, false);

        this.membreUserName = (TextView) rootView.findViewById(R.id.membre_user_name);
        this.personDesciptif = (TextView) rootView.findViewById(R.id.membre_desciptif);
        this.personShortDesciptif = (TextView) rootView.findViewById(R.id.membre_shortdesciptif);
        this.membreEntreprise = (TextView) rootView.findViewById(R.id.membre_entreprise);
        this.profileImage = (ImageView) rootView.findViewById(R.id.membre_image);
        this.logoImage = (ImageView) rootView.findViewById(R.id.membre_logo);
        this.interestLayout = (LinearLayout) rootView.findViewById(R.id.personInteretFragment);
        this.linkLayout = (LinearLayout) rootView.findViewById(R.id.personLinkFragment);
        this.sessionLayout = (LinearLayout) rootView.findViewById(R.id.personSessionFragment);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                "title_detail_" + getArguments().getString(UIUtils.ARG_LIST_TYPE),
                "color_" + getArguments().getString(UIUtils.ARG_LIST_TYPE));
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
        Membre membre = MembreFacade.getInstance().getMembre(context, getArguments().getString(UIUtils.ARG_LIST_TYPE), id);
        if(membre==null){
            membre = MembreFacade.getInstance().getMembre(context, TypeFile.members.name(), id);
        }

        addPeopleInfo(membre);
        addPeopleLink(membre);
        addPeopleSession(membre);
        addPeopleInterrest(membre);
    }

    private void addPeopleInfo(Membre membre){
        Context context = getActivity().getBaseContext();

        if(membre!=null) {
            this.membreUserName.setText(membre.getCompleteName());
            this.membreEntreprise.setText(membre.getCompany());
            this.personDesciptif.setText(Html.fromHtml(Processor.process(membre.getLongdesc().trim())), TextView.BufferType.SPANNABLE);
            this.personShortDesciptif.setText(Html.fromHtml(Processor.process(membre.getShortdesc().trim())));
        }
        else{
            this.membreUserName.setText("Inconnu");
            this.membreEntreprise.setText("Inconnu");
            this.personDesciptif.setText("");
            this.personShortDesciptif.setText("");
        }
        Bitmap image = null;
        //Si on est un sponsor on affiche le logo
        if(membre!=null && membre.getLevel()!=null && membre.getLevel().length()>0){
            image = FileUtils.getImageLogo(context, membre);
            profileImage.setImageBitmap(image);
            logoImage.setImageBitmap(image);
            logoImage.setVisibility(View.VISIBLE);
        }
        else{
            logoImage.setVisibility(View.INVISIBLE);
        }
        if (image == null) {
            //Recuperation de l'mage liee au profil
            image = FileUtils.getImageProfile(context, membre);
            if (image == null) {
                profileImage.setImageDrawable(context.getResources().getDrawable(R.drawable.person_image_empty));
            }
        }
        if(image!=null){
            profileImage.setImageBitmap(image);
        }
    }

    private void addPeopleLink(Membre membre){
        //On vide les éléments
        linkLayout.removeAllViews();

        //On affiche les liens que si on a recuperer des choses
        if (membre!=null && membre.getSharedLinks() != null && !membre.getSharedLinks().isEmpty()) {

            //On ajoute un table layout
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout tableLayout = new TableLayout(getActivity().getBaseContext());
            tableLayout.setLayoutParams(tableParams);

            if(mInflater!=null && membre.getSharedLinks().size()>0) {
                for (final Link link : membre.getSharedLinks()) {
                    RelativeLayout row = (RelativeLayout) mInflater.inflate(R.layout.item_link, tableLayout, false);
                    row.setBackgroundResource(R.drawable.row_transparent_background);
                    //Dans lequel nous allons ajouter le contenu que nous faisons mappé dans
                    TextView link_text = (TextView) row.findViewById(R.id.link_text);
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
            }
            else{
                RelativeLayout row = (RelativeLayout) mInflater.inflate(R.layout.item_link, tableLayout, false);
                row.setBackgroundResource(R.drawable.row_transparent_background);
                //Dans lequel nous allons ajouter le contenu que nous faisons mappé dans
                TextView link_text = (TextView) row.findViewById(R.id.link_text);
                link_text.setText("Aucun lien");
                link_text.setBackgroundColor(Color.TRANSPARENT);
                tableLayout.addView(row);
            }
            linkLayout.addView(tableLayout);
        }
    }

    private void addPeopleSession(Membre membre){
        //On recupere aussi la liste des sessions de l'utilisateur
        List<Conference> conferences = ConferenceFacade.getInstance().getSessionMembre(membre, getActivity());

        //On vide les éléments
        sessionLayout.removeAllViews();

        //On affiche les liens que si on a recuperer des choses
        if (conferences != null && !conferences.isEmpty()) {
            //On ajoute un table layout
            TableLayout.LayoutParams tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            TableLayout tableLayout = new TableLayout(getActivity().getBaseContext());
            tableLayout.setLayoutParams(tableParams);

            if(mInflater!=null) {
                for (final Conference conf : conferences) {
                    LinearLayout row = (LinearLayout) mInflater.inflate(R.layout.item_talk, tableLayout, false);
                    row.setBackgroundResource(R.drawable.row_transparent_background);
                    //Dans lequel nous allons ajouter le contenu que nous faisons mappé dans
                    TextView horaire = (TextView) row.findViewById(R.id.talk_horaire);
                    TextView talkImageText = (TextView) row.findViewById(R.id.talkImageText);
                    TextView talkSalle = (TextView) row.findViewById(R.id.talk_salle);
                    ImageView imageFavorite= (ImageView) row.findViewById(R.id.talk_image_favorite);

                    ((TextView) row.findViewById(R.id.talk_name)).setText(conf.getTitle());
                    ((TextView) row.findViewById(R.id.talk_shortdesciptif)).setText(conf.getSummary().trim());

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
                        if ("Workshop".equals(((Talk) conf).getFormat())) {
                            talkImageText.setText("Atelier");
                        } else {
                            talkImageText.setText("Talk");
                        }
                    } else {
                        talkImageText.setText("L.Talk");
                    }

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TypeFile typeFile = null;
                            int page = 6;
                            if (conf instanceof Talk) {
                                if ("Workshop".equals(((Talk) conf).getFormat())) {
                                    typeFile = TypeFile.workshops;
                                    page = 4;
                                } else {
                                    typeFile = TypeFile.talks;
                                    page = 3;
                                }
                            } else {
                                typeFile = TypeFile.lightningtalks;
                            }
                            ((HomeActivity) getActivity()).changeCurrentFragment(
                                    SessionDetailFragment.newInstance(
                                            typeFile.toString(),
                                            conf.getId(),
                                            page),
                                    typeFile.toString());
                        }
                    });

                    //On regarde si la conf fait partie des favoris
                    SharedPreferences settings = getActivity().getSharedPreferences(UIUtils.PREFS_FAVORITES_NAME, 0);
                    boolean trouve = false;
                    for (String key : settings.getAll().keySet()) {
                        if (key.equals(String.valueOf(conf.getId()))) {
                            trouve = true;
                            imageFavorite.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_action_important));
                            break;
                        }
                    }
                    if (!trouve) {
                        imageFavorite.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_action_not_important));
                    }
                    tableLayout.addView(row);
                }
            }
            sessionLayout.addView(tableLayout);
        }
    }

    private void addPeopleInterrest(Membre membre){
        //On vide les éléments
        interestLayout.removeAllViews();

        //On affiche les liens que si on a recuperer des choses
        if (membre != null && membre.getInterests() != null && !membre.getInterests().isEmpty()) {

            StringBuilder interets = new StringBuilder();
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
                    .addTextColor(getResources().getColor(R.color.black))
                    .getView();
            text.setSingleLine(false);
            interestLayout.addView(text);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (isPeopleMemberFragment()) {
            menu.findItem(R.id.menu_profile).setVisible(true);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_profile) {
        //    final Long myid = id;
            Toast.makeText(getActivity(), "TODO", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setMessage(getString(R.string.description_link_user))
//                    .setPositiveButton(R.string.dial_oui, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            //On recupere les favoris existant si on le demande
//                            SharedPreferences settings = getActivity().getSharedPreferences(UIUtils.PREFS_TEMP_NAME, 0);
//                            SharedPreferences.Editor editor = settings.edit();
//                            editor.putLong("idMemberForFavorite", myid);
//                            editor.commit();
//                            appelerSynchronizer(myid, true);
//                        }
//                    })
//                    .setNeutralButton(R.string.dial_cancel, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            //On ne fait rien
//                        }
//                    });
//            builder.create();
//            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isPeopleMemberFragment(){
        return getArguments().getString(UIUtils.ARG_LIST_TYPE).equals(TypeFile.members.toString());
    }

}
