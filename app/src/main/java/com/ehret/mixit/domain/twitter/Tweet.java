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
package com.ehret.mixit.domain.twitter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.ehret.mixit.R;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


/**
 * Classe modelisant un tweet
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tweet {
    private String created_at = "Thu, 21 Mar 2014 20:16:34 +0000";

    private String from_user;
    private long from_user_id;
    private String from_user_id_str;
    private String from_user_name;
    private String geo;
    private long id;
    private String id_str;
    private String iso_language_code;
    private Map<String, String> metadata;
    private String profile_image_url;
    private String profile_image_url_https;
    private String source;
    private String text;
    private String to_user;
    private long to_user_id;
    private String to_user_id_str;
    private String to_user_name;
    private long in_reply_to_status_id;
    private String in_reply_to_status_id_str;
    @JsonIgnore
    private Bitmap imageToDisplay;

    public Bitmap getImageToDisplay() {
        return imageToDisplay;
    }

    public void setImageToDisplay(Bitmap imageToDisplay) {
        this.imageToDisplay = imageToDisplay;
    }


    public String getCreated_at() {
        return created_at;
    }

    public String getCreatedSince(Context context) {
        String date = created_at;
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "EEE, dd MMM yyyy HH:mm:ss ZZZZZ", Locale.ENGLISH);
            dateFormat.setLenient(false);
            try {
                Date mydate = dateFormat.parse(date);
                //On s'interesse a la difference de date avec maintenant
                long difference = System.currentTimeMillis() - mydate.getTime();
                long x = difference / 1000;
                long seconds = x % 60;
                x /= 60;
                long minutes = x % 60;
                x /= 60;
                long hours = x % 24;
                x /= 24;
                long days = x;

                if (days > 1)
                    return days + " " + context.getResources().getString(R.string.jours);
                if (days == 1)
                    return days + " " + context.getResources().getString(R.string.jour);
                if (hours > 1)
                    return hours + " " + context.getResources().getString(R.string.heures);
                if (hours == 1)
                    return hours + " " + context.getResources().getString(R.string.heure);
                if (minutes > 1)
                    return minutes + " " + context.getResources().getString(R.string.minutes);
                return "1 " + context.getResources().getString(R.string.minute);

            } catch (ParseException e) {
                Log.w(getClass().getName(), e);
            }
        }
        return "";
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public long getFrom_user_id() {
        return from_user_id;
    }

    public void setFrom_user_id(long from_user_id) {
        this.from_user_id = from_user_id;
    }

    public String getFrom_user_id_str() {
        return from_user_id_str;
    }

    public void setFrom_user_id_str(String from_user_id_str) {
        this.from_user_id_str = from_user_id_str;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getId_str() {
        return id_str;
    }

    public void setId_str(String id_str) {
        this.id_str = id_str;
    }

    public String getIso_language_code() {
        return iso_language_code;
    }

    public void setIso_language_code(String iso_language_code) {
        this.iso_language_code = iso_language_code;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
        if (this.profile_image_url != null) {
            try {
                URL url = new URL(profile_image_url);
                imageToDisplay = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                Log.w("Erreur lors de la recuperation de l'image " + this.profile_image_url, e);
            }
        }
    }

    public String getProfile_image_url_https() {
        return profile_image_url_https;
    }

    public void setProfile_image_url_https(String profile_image_url_https) {
        this.profile_image_url_https = profile_image_url_https;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public long getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(long to_user_id) {
        this.to_user_id = to_user_id;
    }

    public String getTo_user_id_str() {
        return to_user_id_str;
    }

    public void setTo_user_id_str(String to_user_id_str) {
        this.to_user_id_str = to_user_id_str;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public long getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(long in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_status_id_str() {
        return in_reply_to_status_id_str;
    }

    public void setIn_reply_to_status_id_str(String in_reply_to_status_id_str) {
        this.in_reply_to_status_id_str = in_reply_to_status_id_str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tweet tweet = (Tweet) o;

        return id == tweet.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
