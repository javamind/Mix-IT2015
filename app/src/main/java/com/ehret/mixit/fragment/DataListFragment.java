package com.ehret.mixit.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.adapter.ListMembreAdapter;
import com.ehret.mixit.adapter.ListTalkAdapter;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.model.MembreFacade;
import com.ehret.mixit.utils.UIUtils;

public class DataListFragment extends Fragment {

    private ListView liste;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DataListFragment newInstance(String typeAppel, String filterQuery, int sectionNumber) {
        DataListFragment fragment = new DataListFragment();
        Bundle args = new Bundle();
        args.putString(UIUtils.ARG_LIST_TYPE, typeAppel);
        args.putString(UIUtils.ARG_LIST_FILTER, filterQuery);
        args.putInt(UIUtils.ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_datalist, container, false);

        //Handle with layout
        this.liste = (ListView) rootView.findViewById(R.id.liste_content);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                "title_" + getArguments().getString(UIUtils.ARG_LIST_TYPE),
                "color_" + getArguments().getString(UIUtils.ARG_LIST_TYPE),
                getArguments().getInt(UIUtils.ARG_SECTION_NUMBER));
    }

    /**
     * updates the list
     */
    private void updateList() {
        switch (TypeFile.getTypeFile(getArguments().getString(UIUtils.ARG_LIST_TYPE))) {
            case members:
                afficherMembre();
                break;
            case staff:
                afficherMembre();
                break;
            case sponsor:
                afficherMembre();
                break;
            case talks:
                afficherConference();
                break;
            case workshops:
                afficherConference();
                break;
            case lightningtalks:
                afficherConference();
                break;
            case favorites:
                afficherConference();
                break;
            default:
                //Par defaut on affiche les speakers
                afficherMembre();

        }
    }

    /**
     * Recuperation des marques de la partie en cours
     */
    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }


    /**
     * Affichage des conferences
     */
    private void afficherMembre() {
        Context context = getActivity().getBaseContext();

        liste.setClickable(true);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Membre membre = (Membre) liste.getItemAtPosition(position);
                ((HomeActivity) getActivity()).changeCurrentFragment(
                        PeopleDetailFragment.newInstance(
                                getArguments().getString(UIUtils.ARG_LIST_TYPE),
                                membre.getId(),
                                getArguments().getInt(UIUtils.ARG_SECTION_NUMBER)),
                        getArguments().getString(UIUtils.ARG_LIST_TYPE));
            }
        });

        //On trie la liste retournée
        liste.setAdapter(
                new ListMembreAdapter(
                        context,
                        MembreFacade.getInstance().getMembres(
                                context,
                                getArguments().getString(UIUtils.ARG_LIST_TYPE),
                                getArguments().getString(UIUtils.ARG_LIST_FILTER))));
    }

    /**
     * Affichage des confs
     */
    private void afficherConference() {
        Context context = getActivity().getBaseContext();
        liste.setClickable(true);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Conference conf = (Conference) liste.getItemAtPosition(position);
                ((HomeActivity) getActivity()).changeCurrentFragment(
                        SessionDetailFragment.newInstance(
                                getArguments().getString(UIUtils.ARG_LIST_TYPE),
                                conf.getId(),
                                getArguments().getInt(UIUtils.ARG_SECTION_NUMBER)),
                        getArguments().getString(UIUtils.ARG_LIST_TYPE));
            }
        });
        String filter = getArguments().getString(UIUtils.ARG_LIST_FILTER);

        switch (TypeFile.getTypeFile(getArguments().getString(UIUtils.ARG_LIST_TYPE))) {
            case workshops:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getWorkshops(context, filter)));
                break;
            case talks:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getTalks(context, filter)));
                break;
            case lightningtalks:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getLightningTalks(context, filter)));
                break;
            default:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getFavorites(context, filter)));

        }
    }
}
