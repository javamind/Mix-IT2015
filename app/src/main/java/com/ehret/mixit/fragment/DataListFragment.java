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
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.model.MembreFacade;

public class DataListFragment extends Fragment {

    private static final String ARG_LIST_TYPE = "type_liste";
    private static final String ARG_LIST_FILTER = "type_filter";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private ListView liste;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DataListFragment newInstance(String typeAppel, String filterQuery, int sectionNumber) {
        DataListFragment fragment = new DataListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LIST_TYPE, typeAppel);
        args.putString(ARG_LIST_FILTER, filterQuery);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
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
                "title_" + getArguments().getString(ARG_LIST_TYPE),
                "color_" + getArguments().getString(ARG_LIST_TYPE),
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    /**
     * Recuperation des marques de la partie en cours
     */
    @Override
    public void onResume() {
        super.onResume();

        switch (TypeFile.getTypeFile(getArguments().getString(ARG_LIST_TYPE))) {
            case members:
                afficherMembre(true);
                break;
            case staff:
                afficherMembre(false);
                break;
            case sponsor:
                afficherMembre(false);
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
                afficherMembre(false);

        }

    }


    /**
     * Affichage des conferences
     *
     * @param partial
     */
    private void afficherMembre(boolean partial) {
        Context context = getActivity().getBaseContext();

        liste.setClickable(true);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO detail
                Membre membre = (Membre) liste.getItemAtPosition(position);
//                Map<String, Object> parameters = new HashMap<String, Object>(2);
//                parameters.put(UIUtils.MESSAGE, membre.getId());
//                parameters.put(UIUtils.TYPE, typeAppel);
//                //UIUtils.startActivity(MembreActivity.class, mActivity, parameters);
            }
        });
        //On trie la liste retournée

        liste.setAdapter(
                new ListMembreAdapter(
                        context,
                        MembreFacade.getInstance().getMembres(
                                context,
                                getArguments().getString(ARG_LIST_TYPE),
                                getArguments().getString(ARG_LIST_FILTER))));
    }

    /**
     * Affichage des confs
     */
    private void afficherConference() {
        Context context = getActivity().getBaseContext();
        liste.setClickable(true);
//        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Conference conf = (Conference) liste.getItemAtPosition(position);
//                Map<String, Object> parameters = new HashMap<String, Object>(2);
//                parameters.put(UIUtils.MESSAGE, conf.getId());
//                if (conf instanceof Lightningtalk) {
//                    parameters.put(UIUtils.TYPE, TypeFile.lightningtalks.name());
//                } else if (conf instanceof Talk && ((Talk) conf).getFormat().equals("Workshop")) {
//                    parameters.put(UIUtils.TYPE, TypeFile.workshops.name());
//                } else {
//                    parameters.put(UIUtils.TYPE, TypeFile.talks.name());
//                }
//                UIUtils.startActivity(TalkActivity.class, mActivity, parameters);
//            }
//        });
        switch (TypeFile.getTypeFile( getArguments().getString(ARG_LIST_TYPE))) {
            case workshops:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getWorkshops(context, getArguments().getString(ARG_LIST_FILTER))));
                break;
            case talks:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getTalks(context, getArguments().getString(ARG_LIST_FILTER))));
                break;
            case lightningtalks:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getLightningTalks(context, getArguments().getString(ARG_LIST_FILTER))));
                break;
            default:
                liste.setAdapter(new ListTalkAdapter(context, ConferenceFacade.getInstance().getFavorites(context, getArguments().getString(ARG_LIST_FILTER))));

        }
    }
}
