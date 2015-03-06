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
package com.ehret.mixit.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Le but de ce fichier est de récupérer les fichiers Json distant pour les sauvegarder
 * en local
 */
public class Synchronizer {
    private final static String TAG = "Synchronizer";

    /**
     * Permet de télécharger un fichier JSON distant
     *
     * @param context
     * @param urlFile
     * @param typeFile
     * @return
     */
    public static boolean downloadJsonFile(Context context, String urlFile, TypeFile typeFile) {
        InputStream inputStream = null;
        FileOutputStream fileOutput = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlFile);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(60000);
            fileOutput = new FileOutputStream(FileUtils.createFileJson(context, typeFile));
            inputStream = new BufferedInputStream(urlConnection.getInputStream());
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
        } catch (IOException e) {
            Log.e(TAG, "Impossible de récupérer le fichier ", e);
            return false;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Impossible de fermer le fichier d'entree", e);
                }
            }
            if (fileOutput != null) {
                try {
                    fileOutput.close();
                } catch (IOException e) {
                    Log.e(TAG, "Impossible de fermer le fichier de sortie", e);
                }
            }
        }
        return true;
    }

    /**
     * Telechargement d'une image
     *
     * @param mURL
     * @param ofile
     * @throws Exception
     */
    public static void downloadImage(Context context, String mURL, String ofile) {
        InputStream in = null;
        FileOutputStream out = null;
        URLConnection urlConn = null;
        try {
            //Emplacement final
            File emplacement = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), ofile + ".jpg");
            if (emplacement.exists()) {
                emplacement.delete();
            }
            emplacement.createNewFile();
            URL url = new URL(mURL);
            urlConn = url.openConnection();
            in = urlConn.getInputStream();
            out = new FileOutputStream(emplacement);
            int c;
            byte[] b = new byte[1024];
            while ((c = in.read(b)) != -1)
                out.write(b, 0, c);
        } catch (IOException e) {
            Log.e(TAG, "Impossible de récupérer l'image  " + mURL, e);
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e(TAG, "Impossible de fermer l'image  " + mURL, e);
                }
            if (out != null)
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "Impossible de fermer l'image  " + mURL, e);
                }
        }
    }
}
