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
package com.ehret.mixit.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.domain.Salle;
import com.ehret.mixit.domain.talk.Conference;
import com.ehret.mixit.domain.talk.Talk;
import com.ehret.mixit.utils.UIUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Adapter permettant de gérer l'affichage dans la liste des talks
 *
 * @param <T>
 */
public class ListTalkForFilAdapter<T extends Conference> extends BaseAdapter {
    private List<T> datas;
    private Context context;
    private int mSize;
    private int mSizeHoraire;

    public ListTalkForFilAdapter(Context context, List<T> datas) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Conference getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SimpleDateFormat sdf = new SimpleDateFormat("EEE");

    public int getCurrentSelection(){
        Date date = new Date();
        for(int i=0 ; i<getCount() ; i++){
            Conference conf = getItem(i);
            if(conf.getEnd()!=null && conf.getEnd().compareTo(date) > 0){
                return i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_talk, parent, false);
            holder = new ViewHolder();
            holder.imageFavorite = (ImageView) convertView.findViewById(R.id.talk_image_favorite);
            holder.langImage = (ImageView) convertView.findViewById(R.id.talk_image_language);
            holder.name = (TextView) convertView.findViewById(R.id.talk_name);
            holder.descriptif = (TextView) convertView.findViewById(R.id.talk_shortdesciptif);
            holder.horaire = (TextView) convertView.findViewById(R.id.talk_horaire);
            holder.talkImageText = (TextView) convertView.findViewById(R.id.talkImageText);
            holder.talkSalle = (TextView) convertView.findViewById(R.id.talk_salle);
            holder.container2 = (FrameLayout) convertView.findViewById(R.id.container2);
            holder.container3 = (RelativeLayout) convertView.findViewById(R.id.container3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Talk conf = (Talk) datas.get(position);

        if (holder.container2.getLayoutParams().height > 0) {
            mSize = holder.container2.getLayoutParams().height;
            mSizeHoraire = holder.horaire.getLayoutParams().height;
        }

        if (conf.getTitle() == null) {
            //In this case this just a time marker
            holder.name.setText(null);
            holder.descriptif.setText(null);
            holder.talkImageText.setText(null);
            holder.talkSalle.setText(null);
            holder.imageFavorite.setImageDrawable(null);
            holder.horaire.setText(null);

            if (conf.getFormat() != null && "day1".equals(conf.getFormat())) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_home));
                holder.name.setText(context.getResources().getString(R.string.calendrier_jour1));
                holder.name.setTextColor(context.getResources().getColor(R.color.white));
            } else if (conf.getFormat() != null && "day2".equals(conf.getFormat())) {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_home));
                holder.name.setText(context.getResources().getString(R.string.calendrier_jour1));
                holder.name.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.color_planning_time));
                holder.name.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(conf.getEnd()));
                holder.name.setTextColor(context.getResources().getColor(R.color.black));
            }

            holder.horaire.getLayoutParams().height = 0;
            holder.talkSalle.getLayoutParams().height = 0;
            holder.container2.getLayoutParams().height = 0;
            holder.descriptif.getLayoutParams().height = 0;
        } else {
            //We control hour
            if(conf.getEnd()==null || conf.getEnd().compareTo(new Date()) < 0){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.selector_fildeleau_oldsession));
            }
            else{
                convertView.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                convertView.setBackground(context.getResources().getDrawable(R.drawable.selector_fildeleau_session));
            }

            if(conf.getLanguage()!=null && "en".equals(conf.getLanguage())){
                holder.langImage.setImageDrawable(context.getResources().getDrawable(R.drawable.en));
            }
            else{
                holder.langImage.setImageDrawable(context.getResources().getDrawable(R.drawable.fr));
            }
            holder.horaire.getLayoutParams().height = mSizeHoraire;
            holder.talkSalle.getLayoutParams().height = mSizeHoraire;
            holder.container2.getLayoutParams().height = mSize;
            holder.descriptif.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.name.setTextColor(context.getResources().getColor(R.color.black));

            if (conf.getStart() != null && conf.getEnd() != null) {
                holder.horaire.setText(String.format(context.getResources().getString(R.string.periode),
                        sdf.format(conf.getStart()),
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(conf.getStart()),
                        DateFormat.getTimeInstance(DateFormat.SHORT).format(conf.getEnd())
                ));
            } else {
                holder.horaire.setText(context.getResources().getString(R.string.pasdate));

            }

            holder.name.setText(conf.getTitle());
            if (conf.getSummary() != null) {
                holder.descriptif.setText(Html.fromHtml(conf.getSummary().trim()));
            }

            Salle salle = Salle.INCONNU;
            if (Salle.INCONNU != Salle.getSalle(conf.getRoom())) {
                salle = Salle.getSalle(conf.getRoom());
            }
            holder.talkSalle.setText(String.format(context.getResources().getString(R.string.Salle), salle.getNom()));
            holder.talkSalle.setBackgroundColor(context.getResources().getColor(salle.getColor()));

            if ("Workshop".equals(((Talk) conf).getFormat())) {
                holder.talkImageText.setText("Atelier");
                holder.talkImageText.setTextColor(context.getResources().getColor(R.color.color_workshops));
            } else {
                holder.talkImageText.setText("Talk");
                holder.talkImageText.setTextColor(context.getResources().getColor(R.color.color_talks));
            }

            //On regarde si la conf fait partie des favoris
            SharedPreferences settings = context.getSharedPreferences(UIUtils.PREFS_FAVORITES_NAME, 0);
            boolean trouve = false;
            for (String key : settings.getAll().keySet()) {
                if (key.equals(String.valueOf(conf.getId()))) {
                    trouve = true;
                    holder.imageFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_important));
                    break;
                }
            }
            if (!trouve) {
                holder.imageFavorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_action_not_important));
            }

        }

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView descriptif;
        TextView horaire;
        TextView talkImageText;
        TextView talkSalle;
        ImageView imageFavorite;
        FrameLayout container2;
        RelativeLayout container3;
        ImageView langImage;

    }

}
