package com.ehret.mixit.fragment;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ehret.mixit.HomeActivity;
import com.ehret.mixit.R;
import com.ehret.mixit.builder.TableRowBuilder;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.domain.talk.Lightningtalk;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.model.MembreFacade;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * builds the planning for a time slot
 */
public class PlanningPlageBuilder {

    private PlanningFragment planningFragment;
    private Context context;
    private TableLayout planningHoraireTableLayout;
    private int nbConfSurPlage;
    private Date heure;

    private PlanningPlageBuilder(PlanningFragment planningFragment) {
        this.planningFragment = planningFragment;
        this.context = planningFragment.getActivity();
    }

    public static PlanningPlageBuilder create(PlanningFragment planningFragment) {
        return new PlanningPlageBuilder(planningFragment);
    }

    public PlanningPlageBuilder with(TableLayout planningHoraireTableLayout) {
        this.planningHoraireTableLayout = planningHoraireTableLayout;
        return this;
    }

    public PlanningPlageBuilder nbConfSurPlage(int nbConfSurPlage) {
        this.nbConfSurPlage = nbConfSurPlage;
        return this;
    }

    public PlanningPlageBuilder reinit(Date heure) {
        this.heure = heure;
        //deux tableaux juxtaposer
        //Un d'une colonne pour gérer l'heure
        planningHoraireTableLayout.removeAllViews();

        //On affiche le planning 30min par 30min
        TableRow tableRow = createTableRow();

        tableRow.addView(new TextViewTableBuilder()
                .buildView(context)
                .addText(String.format(context.getString(R.string.calendrier_planninga), DateFormat.getTimeInstance(DateFormat.SHORT).format(heure)))
                .addAlignement(Gravity.CENTER)
                .addBorders(true, true, false, true)
                .addPadding(4, 0, 4)
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal_title))
                .addSpan(2)
                .addNbLines(2)
                .addBold(true)
                .addTextColor(R.color.black)
                .addBackground(context.getResources().getColor(R.color.blue))
                .addBackgroundDrawable(R.drawable.planning_horaire_background)
                .getView());
        planningHoraireTableLayout.addView(tableRow, TableRowBuilder.getLayoutParams());

        return this;
    }

    public PlanningPlageBuilder createPlage(List<Conference> confs, int index) {
        if (nbConfSurPlage >= index) {
            Conference c = confs.get(index - 1);

            Salle salle = Salle.getSalle(c.getRoom());
            if (c instanceof Talk) {
                Talk t = (Talk) c;
                char code = t.getFormat() != null ? t.getFormat().charAt(0) : 'T';
                createPlanningSalle("(" + code + ") " + c.getTitle(), salle.getColor(), c);
            } else {
                createPlanningSalle("(L) " + c.getTitle(), salle.getColor(), c);
            }
            StringBuilder buf = new StringBuilder();
            if (c.getSpeakers() != null) {
                for (Long id : (List<Long>) c.getSpeakers()) {
                    Membre m = MembreFacade.getInstance().getMembre(context, TypeFile.speaker.name(), id);
                    if (m != null && m.getCompleteName() != null) {
                        if (!buf.toString().equals("")) {
                            buf.append(", ");
                        }
                        buf.append(m.getCompleteName());
                    }

                }
            }
            createPresentateurSalle(true, buf.toString(), salle.getColor(), c);

        }
        return this;
    }

    /**
     * Creation d'une ligne
     */
    private TableRow createTableRow() {
        return new TableRowBuilder().buildTableRow(context)
                .addNbColonne(2)
                .addBackground(context.getResources().getColor(R.color.grey)).getView();
    }

    /**
     * Creation du planning salle
     */
    private void createPlanningSalle(String nom, int color, final Conference conf) {
        TableRow tableRow = createTableRow();
        addEventOnTableRow(conf, tableRow);
        TextView textView = new TextViewTableBuilder()
                .buildView(context)
                .addText(" \n ")
                .addNbLines(2)
                .addNbMaxLines(2)
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .addBackground(context.getResources().getColor(color))
                .getView();
        tableRow.addView(textView);

        TextView button = new TextViewTableBuilder()
                .buildView(context)
                .addAlignement(Gravity.CENTER)
                .addText(nom + " \n ")
                .addBorders(true, true, false, true)
                .addPadding(8, 8, 4)
                .addBold(true)
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .addNbLines(2)
                .addNbMaxLines(2)
                .addTextColor(context.getResources().getColor(android.R.color.black))
                .getView();
        button.setBackgroundResource(R.drawable.selector_planning_tab);

        //textView.setMaxWidth(tableRow.getWidth()-4);
        tableRow.addView(button);
        planningHoraireTableLayout.addView(tableRow, TableRowBuilder.getLayoutParams());
    }

    /**
     * Ajoute un event pour zoomer sur le detail d'une plage horaire
     */
    private void addEventOnTableRow(final Conference conf, TableRow tableRow) {

        //En fonction du type de talk nous ne faisons pas la même chose
        if (conf instanceof Lightningtalk) {
            //Pour la les lightning on affiche la liste complete
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) planningFragment.getActivity()).changeCurrentFragment(
                            SessionDetailFragment.newInstance(TypeFile.lightningtalks.toString(), conf.getId(), 6),
                            TypeFile.lightningtalks.toString());
                }
            });
        } else {
            Talk t = (Talk) conf;
            //Pour les talks on ne retient que les talks et workshop
            char code = t.getFormat() != null ? t.getFormat().charAt(0) : 'T';
            if (code == 'T' || code == 'W' || code == 'K') {
                tableRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int num = 3;
                        TypeFile type = TypeFile.talks;
                        if (((Talk) conf).getFormat().charAt(0) == 'W') {
                            num = 4;
                            type = TypeFile.workshops;
                        }

                        ((HomeActivity) planningFragment.getActivity()).changeCurrentFragment(
                                SessionDetailFragment.newInstance(type.name(), conf.getId(), num),
                                type.name());
                    }
                });
            }
        }
    }

    /**
     * Ajout presentateur
     */
    private void createPresentateurSalle(boolean dernierligne, String nom, int color, final Conference conf) {
        TableRow tableRow = createTableRow();
        addEventOnTableRow(conf, tableRow);
        tableRow.addView(new TextViewTableBuilder()
                .buildView(context)
                .addText(" ")
                .addBackground(context.getResources().getColor(color))
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .getView());
        TextView button = new TextViewTableBuilder()
                .buildView(context)
                .addAlignement(Gravity.CENTER)
                .addText(nom)
                .addBorders(true, true, dernierligne, false)
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .addPadding(8, 8, 4)
                .addBackground(context.getResources().getColor(android.R.color.white))
                .addTextColor(context.getResources().getColor(R.color.grey_dark))
                .getView();
        button.setBackgroundResource(R.drawable.selector_planning_tab);
        tableRow.addView(button);

        planningHoraireTableLayout.addView(tableRow, TableRowBuilder.getLayoutParams());
    }
}
