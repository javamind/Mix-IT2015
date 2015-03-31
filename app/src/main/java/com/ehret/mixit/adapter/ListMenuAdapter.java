/*
 * Copyright 2015 Guillaume EHRET
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ehret.mixit.R;
import com.ehret.mixit.domain.Menu;

import java.util.List;

/**
 * Adapter permettant l'affichage des données dans la liste des membres
 */
public class ListMenuAdapter extends BaseAdapter {
    private List<Menu> datas;
    private Context context;

    public ListMenuAdapter(Context context, List<Menu> datas) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Menu getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
            holder = new ViewHolder();
            holder.color = (TextView) convertView.findViewById(R.id.menu_color);
            holder.label = (TextView) convertView.findViewById(R.id.menu_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Menu menu = datas.get(position);
        holder.label.setText(context.getString(menu.getLabel()));
        holder.color.setText(context.getString(R.string.blank));
        holder.color.setBackgroundColor(context.getResources().getColor(menu.getColorResource()));
        return convertView;
    }

    static class ViewHolder {
        TextView label;
        TextView color;

    }

}
