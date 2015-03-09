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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.FileUtils;
import com.ehret.mixit.utils.UIUtils;
import com.github.rjeschke.txtmark.Processor;

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
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_people, container, false);

        this.membreUserName = (TextView) rootView.findViewById(R.id.membre_user_name);
        this.personDesciptif = (TextView) rootView.findViewById(R.id.membre_desciptif);
        this.personShortDesciptif = (TextView) rootView.findViewById(R.id.membre_shortdesciptif);
        this.membreEntreprise = (TextView) rootView.findViewById(R.id.membre_entreprise);
        this.profileImage = (ImageView) rootView.findViewById(R.id.membre_image);
        this.logoImage = (ImageView) rootView.findViewById(R.id.membre_logo);

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
