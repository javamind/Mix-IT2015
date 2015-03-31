package com.ehret.mixit.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.model.ConferenceFacade;
import com.ehret.mixit.utils.UIUtils;
import com.ehret.mixit.view.SlidingTabLayout;

import java.util.Date;
import java.util.List;


public class PlanningFragment extends Fragment {

    /**
     * Remember the time when the user select a slot
     */
    private static final String STATE_SELECTED_TIME_0 = "selected_slot_time0";
    private static final String STATE_SELECTED_TIME_1 = "selected_slot_time1";

    private PlanningPagerAdapter planningPagerAdapter;

    private boolean mCreated = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planning, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If the user come on this fragment after a click on the back button this method is not called
        mCreated = true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached("title_section_planning","color_planning");
    }

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link PlanningPagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        planningPagerAdapter = new PlanningPagerAdapter();
        viewPager.setAdapter(planningPagerAdapter);

        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
    }

    protected PlanningFragment getInstance() {
        return this;
    }

    public PlanningPagerAdapter getPlanningPagerAdapter() {
        return planningPagerAdapter;
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link com.ehret.mixit.view.SlidingTabLayout}.
     */
    protected class PlanningPagerAdapter extends PagerAdapter {

        private PlanningPlageBuilder planningPlageBuilderJour0;
        private PlanningPlageBuilder planningPlageBuilderJour1;
        private PlanningJourneyBuilder planningJourneyBuilderJour0;
        private PlanningJourneyBuilder planningJourneyBuilderJour1;

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 2;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(android.view.ViewGroup, int)} is the
         * same object as the {@link android.view.View} added to the {@link android.support.v4.view.ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link com.ehret.mixit.view.SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.calendrier_jour1);
            }
            return getString(R.string.calendrier_jour2);
        }

        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.item_planning, container, false);

            // Add the newly created View to the ViewPager
            container.addView(view);

            TableLayout salleTableLayout = (TableLayout) view.findViewById(R.id.salleTableLayout);
            PlanningSalleBuilder
                    .create(getActivity())
                    .with(salleTableLayout)
                    .createSalle(Salle.SALLE1, Salle.SALLE2, false)
                    .createSalle(Salle.SALLE3, Salle.SALLE4, false)
                    .createSalle(Salle.SALLE5, Salle.SALLE6, false)
                    .createSalle(Salle.SALLE7, Salle.SALLE8, false)
                    .createSalle(Salle.SALLE9, Salle.INCONNU, true);

            TableLayout planningHoraireTableLayout = (TableLayout) view.findViewById(R.id.planningHoraireTableLayout);
            final GridLayout calendarGrid = (GridLayout) view.findViewById(R.id.planningGrid);
            calendarGrid.removeAllViews();
            calendarGrid.setColumnCount(4);
            calendarGrid.setRowCount(144);
            calendarGrid.setUseDefaultMargins(true);
            calendarGrid.setBackgroundColor(getResources().getColor(R.color.black));


            //Par defaut on affiche la premiere session de la premier journee
            int day = 16;
            if (position == 0) {
                planningPlageBuilderJour0 = PlanningPlageBuilder.create(getInstance()).with(planningHoraireTableLayout);
                planningJourneyBuilderJour0 = PlanningJourneyBuilder.create(getInstance()).jour(0).grid(calendarGrid);
                dessinerCalendrierJour0(planningHoraireTableLayout);
            } else {
                day = 17;
                planningPlageBuilderJour1 = PlanningPlageBuilder.create(getInstance()).with(planningHoraireTableLayout);
                planningJourneyBuilderJour1 = PlanningJourneyBuilder.create(getInstance()).jour(1).grid(calendarGrid);
                dessinerCalendrierJour1(planningHoraireTableLayout);
            }
            Date heure = mCreated ?
                    UIUtils.createPlageHoraire(day, 8, 30) :
                    new Date(PreferenceManager.getDefaultSharedPreferences(getActivity()).getLong(position==0 ? STATE_SELECTED_TIME_0 :STATE_SELECTED_TIME_1, 0));
            refreshPlanningHoraire(position, heure);

            if(position!=0){
                mCreated = false;
            }

            //On rearange la largeur des colonnes
            ViewTreeObserver vto = calendarGrid.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Double calcul = (calendarGrid.getWidth() - ((double) calendarGrid.getWidth()) / 4) * 0.51;
                    for (int i = 0; i < calendarGrid.getChildCount(); i++) {
                        View view = calendarGrid.getChildAt(i);
                        if (view instanceof Button) {
                            ((Button) view).setWidth(calcul.intValue());
                        } else if (view instanceof TextView) {
                            //On ne regarde que les colonnes fusionnées
                            if (((GridLayout.LayoutParams) view.getLayoutParams()).columnSpec.equals(GridLayout.spec(2, 2, GridLayout.FILL))) {
                                ((TextView) view).setWidth(calcul.intValue() * 2);
                            }
                        }
                    }
                    ViewTreeObserver obs = calendarGrid.getViewTreeObserver();
                    obs.removeOnGlobalLayoutListener(this);
                }
            });

            // Return the View
            return view;
        }

        /**
         * Destroy the item from the {@link android.support.v4.view.ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        /**
         * Permet d'afficher le planning lie a la plage selectionnee
         */
        public void refreshPlanningHoraire(int jour, Date heure) {
            //We save the time
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong((jour == 0) ? STATE_SELECTED_TIME_0 : STATE_SELECTED_TIME_1, heure.getTime());
            editor.apply();

            PlanningPlageBuilder planningPlageBuilderJour = (jour == 0) ? planningPlageBuilderJour0 : planningPlageBuilderJour1;

            List<Conference> confs = ConferenceFacade.getInstance().getConferenceSurPlageHoraire(heure, getActivity());

            planningPlageBuilderJour
                    .nbConfSurPlage(confs.size())
                    .reinit(heure)
                    .createPlage(confs, 1)
                    .createPlage(confs, 2)
                    .createPlage(confs, 3)
                    .createPlage(confs, 4)
                    .createPlage(confs, 5)
                    .createPlage(confs, 6)
                    .createPlage(confs, 7)
                    .createPlage(confs, 8);
        }


        protected void dessinerCalendrierJour0(TableLayout planningHoraireTableLayout) {
            planningJourneyBuilderJour0
                    .with(planningHoraireTableLayout)
                    .addViewHeure()
                    .addViewQuartHeure()
                    .addViewEventCommun(0, 3, " ", null, R.drawable.button_empty_background, false)
                    .addViewEventCommun(3, 9, getResources().getString(R.string.calendrier_accueil), UIUtils.createPlageHoraire(16, 8, 15), R.drawable.button_pause_background, false)
                    .addViewEventCommun(12, 3, getResources().getString(R.string.calendrier_orga), UIUtils.createPlageHoraire(16, 9, 0), R.drawable.button_pause_background, true)
                    .addViewEventCommun(15, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(16, 9, 15), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(20, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(16, 9, 40), R.drawable.button_pause_background, true)
                    .addViewTalk(24, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(16, 10, 0), false)
                    .addViewTalk(34, 4, getResources().getString(R.string.calendrier_pause), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(16, 10, 50), true)
                    .addViewTalk(38, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(16, 11, 10), false)
                    .addViewWorkshop(24, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(16, 10, 0), false)
                    .addViewTalk(48, 12, getResources().getString(R.string.calendrier_repas), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(16, 12, 0), false)
                    .addViewWorkshop(48, 6, getResources().getString(R.string.calendrier_repas), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(16, 12, 0), false)
                    .addViewWorkshop(54, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(16, 12, 30), false)
                    .addViewTalk(60, 6, getString(R.string.calendrier_ligthning_small), false, R.drawable.button_ligtalk_background, UIUtils.createPlageHoraire(16, 13, 0), false)
                    .addViewTalk(66, 2, getString(R.string.calendrier_presses), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(16, 13, 30), true)
                    .addViewTalk(68, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(16, 13, 40), false)
                    .addViewEventCommun(78, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(16, 14, 30), R.drawable.button_pause_background, true)
                    .addViewWorkshop(82, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(16, 14, 50), false)
                    .addViewTalk(82, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(16, 14, 50), false)
                    .addViewTalk(92, 4, getString(R.string.calendrier_pause), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(16, 15, 40), true)
                    .addViewTalk(96, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(16, 16, 0), false)
                    .addViewEventCommun(106, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(16, 16, 50), R.drawable.button_pause_background, true)
                    .addViewEventCommun(110, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(16, 17, 10), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(115, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(16, 17, 35), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(120, 12, " ", null, R.drawable.button_empty_background, false)
                    .addViewEventCommun(132, 12, getResources().getString(R.string.calendrier_partie), UIUtils.createPlageHoraire(16, 19, 0), R.drawable.button_ligtalk_background, false);

        }

        protected void dessinerCalendrierJour1(TableLayout planningHoraireTableLayout) {
            planningJourneyBuilderJour1
                    .with(planningHoraireTableLayout)
                    .addViewHeure()
                    .addViewQuartHeure()
                    .addViewEventCommun(0, 6, " ", null, R.drawable.button_empty_background, false)
                    .addViewEventCommun(6, 6, getResources().getString(R.string.calendrier_accueil), UIUtils.createPlageHoraire(17, 8, 15), R.drawable.button_pause_background, false)
                    .addViewEventCommun(12, 3, getResources().getString(R.string.calendrier_orga), UIUtils.createPlageHoraire(17, 9, 0), R.drawable.button_pause_background, true)
                    .addViewEventCommun(15, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(17, 9, 15), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(20, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(17, 9, 40), R.drawable.button_pause_background, true)
                    .addViewTalk(24, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(17, 10, 0), false)
                    .addViewTalk(34, 4, getResources().getString(R.string.calendrier_pause), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(17, 10, 50), true)
                    .addViewTalk(38, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(17, 11, 10), false)
                    .addViewWorkshop(24, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(17, 10, 0), false)
                    .addViewTalk(48, 12, getResources().getString(R.string.calendrier_repas), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(17, 12, 0), false)
                    .addViewWorkshop(48, 6, getResources().getString(R.string.calendrier_repas), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(17, 12, 0), false)
                    .addViewWorkshop(54, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(17, 12, 30), false)
                    .addViewTalk(60, 6, getString(R.string.calendrier_keynote), false, R.drawable.button_ligtalk_background, UIUtils.createPlageHoraire(17, 13, 0), true)
                    .addViewTalk(66, 2, getString(R.string.calendrier_presses), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(17, 13, 30), true)
                    .addViewTalk(68, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(17, 13, 40), false)
                    .addViewEventCommun(78, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(17, 14, 30), R.drawable.button_pause_background, true)
                    .addViewWorkshop(82, 24, getResources().getString(R.string.calendrier_atelier), false, R.drawable.button_workshop_background, UIUtils.createPlageHoraire(17, 14, 50), false)
                    .addViewTalk(82, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(17, 14, 50), false)
                    .addViewTalk(92, 4, getString(R.string.calendrier_pause), false, R.drawable.button_pause_background, UIUtils.createPlageHoraire(17, 15, 40), true)
                    .addViewTalk(96, 10, getResources().getString(R.string.calendrier_conf_small), false, R.drawable.button_talk_background, UIUtils.createPlageHoraire(17, 16, 0), false)
                    .addViewEventCommun(106, 4, getResources().getString(R.string.calendrier_pause), UIUtils.createPlageHoraire(17, 16, 50), R.drawable.button_pause_background, true)
                    .addViewEventCommun(110, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(17, 17, 10), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(115, 5, getResources().getString(R.string.calendrier_keynote), UIUtils.createPlageHoraire(17, 17, 35), R.drawable.button_ligtalk_background, false)
                    .addViewEventCommun(120, 2, getResources().getString(R.string.calendrier_cloture), UIUtils.createPlageHoraire(17, 18, 0), R.drawable.button_ligtalk_background, true)
                    .addViewEventCommun(122, 22, " ", null, R.drawable.button_empty_background, false);
        }


    }


}
