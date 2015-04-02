package com.ehret.mixit.fragment;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.SalleActivity;
import com.ehret.mixit.builder.TableRowBuilder;
import com.ehret.mixit.builder.TextViewTableBuilder;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * builds the list of the rooms
 */
public class PlanningSalleBuilder {

    private Context context;
    private TableLayout salleTableLayout;

    private PlanningSalleBuilder(Context context) {
        this.context = context;
    }

    public static PlanningSalleBuilder create(Context context) {
        return new PlanningSalleBuilder(context);
    }

    public PlanningSalleBuilder with(TableLayout salleTableLayout) {
        this.salleTableLayout = salleTableLayout;
        return this;
    }

    public PlanningSalleBuilder createSalle(Salle salle1, Salle salle2, boolean dernierligne) {
        TableRow tableRow = new TableRowBuilder().buildTableRow(context)
                .addNbColonne(2)
                .addBackground(context.getResources().getColor(R.color.grey)).getView();
        createSalle(dernierligne, tableRow, salle1);
        createSalle(dernierligne, tableRow, salle2);
        salleTableLayout.addView(tableRow, TableRowBuilder.getLayoutParams());
        return this;
    }


    private void createSalle(boolean dernierligne, TableRow tableRow, final Salle salle) {
        tableRow.addView(new TextViewTableBuilder()
                .buildView(context)
                .addText(" ")
                .addBorders(true, false, dernierligne, true)
                .addPadding(4, 0, 4)
                .addBackground(context.getResources().getColor(salle.getColor()))
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .getView());

        TextView mySalle = new TextViewTableBuilder()
                .buildView(context)
                .addAlignement(Gravity.CENTER)
                .addText(salle.getNom())
                .addSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(R.integer.text_size_cal))
                .addBorders(true, true, dernierligne, true)
                .addPadding(4, 0, 4)
                .addBackground(context.getResources().getColor(android.R.color.white))
                .addTextColor(context.getResources().getColor(android.R.color.black))
                .getView();

        mySalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> parametres = new HashMap<>();
                parametres.put(UIUtils.ARG_KEY_ROOM, Integer.valueOf(salle.getEtage()));
                UIUtils.startActivity(SalleActivity.class, context, parametres);
            }
        });
        tableRow.addView(mySalle);
    }
}
