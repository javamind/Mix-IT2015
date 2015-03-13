package com.ehret.mixit.fragment;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.builder.ButtonGridBuilder;
import com.ehret.mixit.builder.TextViewGridBuilder;

import java.util.Date;

/**
 * builds the planning for a journey
 */
public class PlanningJourneyBuilder {

    private PlanningFragment planningFragment;
    private Context context;
    private int jour;
    private GridLayout calendarGrid;
    private TableLayout planningHoraireTableLayout;

    private PlanningJourneyBuilder(PlanningFragment planningFragment) {
        this.planningFragment = planningFragment;
        this.context = planningFragment.getActivity();
    }

    public static PlanningJourneyBuilder create(PlanningFragment planningFragment) {
        return new PlanningJourneyBuilder(planningFragment);
    }

    public PlanningJourneyBuilder jour(int jour) {
        this.jour = jour;
        return this;
    }

    public PlanningJourneyBuilder grid(GridLayout calendarGrid) {
        this.calendarGrid = calendarGrid;
        return this;
    }

    public PlanningJourneyBuilder with(TableLayout planningHoraireTableLayout) {
        this.planningHoraireTableLayout = planningHoraireTableLayout;
        return this;
    }

    /**
     * Ajoute un moment commun
     */
    public PlanningJourneyBuilder addViewEventCommun(int row, int temps, String text, final Date heure, int background) {
        Button textView = getButton(text, false, background, heure);
        setLayoutAndBorder(textView,
                new GridLayout.LayoutParams(GridLayout.spec(row, temps, GridLayout.FILL), GridLayout.spec(2, 2, GridLayout.FILL)),
                row >= 42, true, true, true, temps);
        calendarGrid.addView(textView);
        return this;
    }


    /**
     * Ajoute une conf
     */
    public PlanningJourneyBuilder addViewTalk(int row, int temps, String text, boolean title, int background, final Date heure) {
        Button textView = getButton(text, title, background, heure);
        setLayoutAndBorder(textView,
                new GridLayout.LayoutParams(GridLayout.spec(row, temps == 99 ? 1 : temps, GridLayout.FILL), GridLayout.spec(2, GridLayout.FILL)),
                false, true, true, false, temps);

        calendarGrid.addView(textView);
        return this;
    }

    /**
     * Ajoute un atlier
     */
    public PlanningJourneyBuilder addViewWorkshop(int row, int temps, String text, boolean title, int background, final Date heure) {
        Button textView = getButton(text, title, background, heure);
        setLayoutAndBorder(textView,
                new GridLayout.LayoutParams(GridLayout.spec(row, temps, GridLayout.FILL), GridLayout.spec(3, GridLayout.FILL)),
                false, true, true, true, temps);
        calendarGrid.addView(textView);
        return this;
    }

    /**
     * Ajout de la colonne heure
     */
    public PlanningJourneyBuilder addViewHeure() {
        for (int i = 0; i < 12; i++) {
            //Heure sur 4 lignes
            TextView textView = new TextViewGridBuilder()
                    .buildView(context)
                    .addAlignement(Gravity.CENTER)
                    .addText(String.valueOf(i + 8) + "H")
                    .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                    .addBackgroundDrawable(R.drawable.calendar_title_background)
                    .addTextColor(context.getResources().getColor(android.R.color.black))
                    .getView();
            setLayoutAndBorder(textView,
                    new GridLayout.LayoutParams(GridLayout.spec(i * 12, 12, GridLayout.FILL), GridLayout.spec(0, GridLayout.FILL)),
                    i == 11, true, true, false, 0);
            calendarGrid.addView(textView);
        }
        return this;
    }


    /**
     * Ajout de la colonne des quarts d'heure
     */
    public PlanningJourneyBuilder addViewQuartHeure() {
        for (int i = 0; i < 144; i++) {
            //Quart d'heure affiche juste un repère
            TextView textView = new TextViewGridBuilder()
                    .buildView(context)
                            //.addPadding(0, 0, 0)
                    .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal_mini))
                    .addBackgroundDrawable(R.drawable.calendar_title_background)
                    .addTextColor(context.getResources().getColor(android.R.color.black))
                    .getView();
            setLayoutAndBorder(textView,
                    new GridLayout.LayoutParams(GridLayout.spec(i, GridLayout.FILL), GridLayout.spec(1, GridLayout.FILL)),
                    i == 143, true, true, false, 99);
            calendarGrid.addView(textView);
        }
        return this;
    }

    /**
     * Ajoute un champ clickable
     */
    private Button getButton(String text, boolean title, int background, final Date heure) {
        Button textView = new ButtonGridBuilder()
                .buildView(context)
                .addText(text)
                .addAlignement(Gravity.CENTER)
                .addBold(true)
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .addBackgroundDrawable(background)
                .addTextColor(context.getResources().getColor(android.R.color.black))
                .getView();

        if (title) {
            textView.setAllCaps(true);
            textView.setBackgroundResource(R.drawable.calendar_title_background);
        }
        if (heure != null) {
            //Sur un clic on va faire un zoom sur une session
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    planningFragment.getPlanningPagerAdapter().refreshPlanningHoraire(jour, heure);
                }
            });
        }
        return textView;
    }


    private void setLayoutAndBorder(TextView textView, GridLayout.LayoutParams params, boolean borderBottom, boolean borderTop,
                                    boolean borderLeft, boolean borderRight, int hauteurcalculee) {
        params.bottomMargin = borderBottom ? 1 : 0;
        params.leftMargin = borderLeft ? 1 : 0;
        params.rightMargin = borderRight ? 1 : 0;
        params.topMargin = borderTop ? 1 : 0;
        textView.setLayoutParams(params);
        float facteur;
        switch (hauteurcalculee) {
            case 1:
                facteur = 2;
                break;
            case 2:
            case 3:
                facteur = 1.5f;
                break;
            case 6:
                facteur = 2;
                break;
            case 99:
                facteur = 0.4f;
                break;
            default:
                facteur = 1;

        }
        textView.getLayoutParams().height =
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, context.getResources().getInteger(R.integer.text_size_cal) * facteur,
                        context.getResources().getDisplayMetrics());
    }
}
