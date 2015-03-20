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
import com.ehret.mixit.adapter.ListTalkForFilAdapter;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.model.ConferenceFacade;


public class FilDeLeauFragment extends Fragment {

    private ListView liste;

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
        ((HomeActivity) activity).onSectionAttached("title_fildeleau","color_planning");
    }

    /**
     * Recuperation des marques de la partie en cours
     */
    @Override
    public void onResume() {
        super.onResume();
        Context context = getActivity().getBaseContext();
        liste.setClickable(true);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Talk conf = (Talk) liste.getItemAtPosition(position);

                if("Workshop".equals(conf.getFormat())){
                    ((HomeActivity) getActivity()).changeCurrentFragment(
                            SessionDetailFragment.newInstance(TypeFile.workshops.toString(), conf.getId(),4), TypeFile.workshops.toString());
                }
                else if("Talk".equals(conf.getFormat()) || "Keynote".equals(conf.getFormat())){
                    ((HomeActivity) getActivity()).changeCurrentFragment(
                            SessionDetailFragment.newInstance(TypeFile.talks.toString(), conf.getId(),3), TypeFile.talks.toString());
                }
            }
        });

        liste.setAdapter(new ListTalkForFilAdapter(context, ConferenceFacade.getInstance().getWorkshopsAndTalks(context)));

    }
}
