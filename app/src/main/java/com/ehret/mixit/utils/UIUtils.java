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
package com.ehret.mixit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.Time;
import android.widget.Toast;

import com.ehret.mixit.R;
import com.ehret.mixit.domain.SendSocial;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Classe utilitaire
 */
public class UIUtils {

    private static final Time sTime = new Time();
    public static final long CONFERENCE_START_MILLIS = parseTime("2015-04-16T08:30:00.000-07:00");
    public static final long CONFERENCE_END_MILLIS = parseTime("2015-04-17T18:30:00.000-07:00");

    public static final String ARG_LIST_TYPE = "type_liste";
    public static final String ARG_LIST_FILTER = "type_filter";
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_ID = "id";
    public static final String PREFS_FAVORITES_NAME = "PrefFavorites";
    public static final String PREFS_TEMP_NAME = "PrefTemp";
    public final static String ARG_FILE_SAV = "Mixit2015";
    public final static String ARG_KEY_FIRST_TIME = "first_time";

    /**
     * Parse the given string as a RFC 3339 timestamp, returning the value as
     * milliseconds since the epoch.
     */
    private static long parseTime(String time) {
        sTime.parse3339(time);
        return sTime.toMillis(false);
    }

    /**
     * Verifie si la connectivite reseau est OK
     */
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    /**
     * Ouvre une Intent
     */
    public static boolean startActivity(Class activityClass, Context context) {
        return startActivity(activityClass, context, null);
    }


    /**
     * Ouvre une Intent
     */
    public static boolean startActivity(Class activityClass, Context context, Map<String, Object> parametres) {
        Intent i;
        i = new Intent(context, activityClass);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (parametres != null) {
            for (String key : parametres.keySet()) {
                Object param = parametres.get(key);
                if (param != null) {
                    if (param instanceof Long) {
                        i.putExtra(key, ((Long) param).longValue());
                    } else {
                        i.putExtra(key, param.toString());
                    }
                }
            }
        }
        context.startActivity(i);
        return true;
    }

    /**
     */
    public static boolean startActivity(Class activityClass, Activity activity, long id) {
        Intent i;
        i = new Intent(activity, activityClass);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(ARG_ID, id);
        activity.startActivity(i);
        return true;
    }

    /**
     * Permet de filtrer les intents proposées à l'utilisateur
     */
    public static boolean filterIntent(Activity activity, String type, Intent i) {
        List<ResolveInfo> resInfo = activity.getPackageManager().queryIntentActivities(i, 0);
        if (!resInfo.isEmpty()) {
            boolean found = false;
            for (ResolveInfo info : resInfo) {
                if (info.activityInfo.packageName.toLowerCase().contains(type)) {
                    i.setPackage(info.activityInfo.packageName);
                    found = true;
                    break;
                }
            }
            return found;
        }
        return false;
    }

    /**
     * Creation de la bonne date
     */
    public static Date createPlageHoraire(int jour, int heure, int minute) {
        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        calendar.set(2014, 4, jour, heure, minute , 0);
        return calendar.getTime();
    }

    /**
     * Permet d'envoyer un message en filtrant les intents
     */
    public static void sendMessage(Activity activity, SendSocial type) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.hastag));
        if (!UIUtils.filterIntent(activity, type.name(), i)) {
            Toast.makeText(activity.getBaseContext(), SendSocial.plus.equals(type) ? R.string.description_no_google : R.string.description_no_twitter, Toast.LENGTH_SHORT).show();
        }
        activity.startActivity(Intent.createChooser(i, "Share URL"));
    }

}
