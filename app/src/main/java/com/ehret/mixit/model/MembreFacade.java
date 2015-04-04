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
package com.ehret.mixit.model;

import android.content.Context;
import android.util.Log;
import android.util.LongSparseArray;

import com.ehret.mixit.domain.TypeFile;
import com.ehret.mixit.domain.people.Interet;
import com.ehret.mixit.domain.people.Membre;
import com.ehret.mixit.utils.FileUtils;
import com.ehret.mixit.utils.Utils;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Comparator;
import java.util.List;

/**
 * Le but de ce fichier est de s'interfacer avec le fichier Json gerant les
 * différents membres.
 */
public class MembreFacade {
    /**
     * Factory Json
     */
    private JsonFactory jsonFactory;
    /**
     * Objetc mapper permettant de faire le binding entre le JSON et les objets
     */
    private ObjectMapper objectMapper;
    /**
     * Instance du singleton
     */
    private static MembreFacade membreFacade;

    private final static String TAG = "MembreFacade";

    private static LongSparseArray<Membre> membres = new LongSparseArray<>();

    private static LongSparseArray<Membre> speaker = new LongSparseArray<>();

    private static LongSparseArray<Membre> staff = new LongSparseArray<>();

    private static LongSparseArray<Membre> sponsors = new LongSparseArray<>();

    private static LongSparseArray<Interet> interets = new LongSparseArray<>();


    /**
     * Permet de vider le cache de données
     */
    public void viderCache() {
        membres.clear();
        speaker.clear();
        staff.clear();
        sponsors.clear();
        interets.clear();
    }

    /**
     * Permet de vider le cache de données
     */
    public void viderCacheSpeakerStaffSponsor() {
        speaker.clear();
        staff.clear();
        sponsors.clear();
        interets.clear();
    }

    /**
     * Permet de vider le cache de données
     */
    public void viderCacheMembres() {
        membres.clear();
        interets.clear();
    }

    /**
     * Constructeur prive car singleton
     */
    private MembreFacade() {
        //Creation de nos objets
        this.jsonFactory = new JsonFactory();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retourne le singleton
     */
    public static MembreFacade getInstance() {
        if (membreFacade == null) {
            membreFacade = new MembreFacade();
        }
        return membreFacade;
    }

    public List<Membre> getMembres(Context context, String typeAppel, String filtre) {
        if (TypeFile.members.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, membres);
            return Ordering.from(getComparatorByName()).sortedCopy(filtrerMembre(membres, filtre));
        } else if (TypeFile.staff.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, staff);
            return Ordering.from(getComparatorByName()).sortedCopy(filtrerMembre(staff, filtre));
        } else if (TypeFile.sponsor.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, sponsors);
            return Ordering.from(getComparatorByLevel()).reverse().compound(getComparatorByName()).sortedCopy(filtrerMembre(sponsors, filtre));
        } else if (TypeFile.speaker.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, speaker);
            return Ordering.from(getComparatorByName()).sortedCopy(filtrerMembre(speaker, filtre));
        }
        return null;
    }

    /**
     * Filtre la liste des membres
     */
    private List<Membre> filtrerMembre(LongSparseArray<Membre> talks, final String filtre) {
        return FluentIterable.from(Utils.asList(talks)).filter(new Predicate<Membre>() {
            @Override
            public boolean apply(Membre input) {
                return (filtre == null ||
                        (input.getFirstName() != null && input.getFirstName().toLowerCase().contains(filtre.toLowerCase())) ||
                        (input.getLastName() != null && input.getLastName().toLowerCase().contains(filtre.toLowerCase())) ||
                        (input.getShortdesc() != null && input.getShortdesc().toLowerCase().contains(filtre.toLowerCase())));
            }
        }).toList();
    }

    /**
     * Comparaison par nom
     */
    private Comparator<Membre> getComparatorByLevel() {
        return new Comparator<Membre>() {
            @Override
            public int compare(Membre m1, Membre m2) {
                if (m1.getLevel() == null) {
                    return 1;
                }
                if (m2.getLevel() == null) {
                    return -1;
                }
                return m1.getLevel().compareTo(m2.getLevel());
            }
        };
    }

    /**
     * Comparaison par nom
     */
    private Comparator<Membre> getComparatorByName() {
        return new Comparator<Membre>() {
            @Override
            public int compare(Membre m1, Membre m2) {
                if (m1.getLastName() == null) {
                    return 1;
                }
                if (m2.getLastName() == null) {
                    return -1;
                }
                return m1.getLastName().compareTo(m2.getLastName());
            }
        };
    }

    /**
     * Permet de recuperer la liste des membres
     */
    private void getMapMembres(Context context, String type, LongSparseArray<Membre> membres) {
        if (membres.size()==0) {
            InputStream is = null;
            List<Membre> membreListe = null;
            JsonParser jp;
            try {
                //On regarde si fichier telecharge
                File myFile = FileUtils.getFileJson(context, TypeFile.getTypeFile(type));
                if (myFile == null) {
                    //On prend celui inclut dans l'archive
                    is = FileUtils.getRawFileJson(context, TypeFile.getTypeFile(type));
                } else {
                    is = new FileInputStream(myFile);
                }
                jp = this.jsonFactory.createJsonParser(is);
                membreListe = this.objectMapper.readValue(jp, new TypeReference<List<Membre>>() {
                });
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de la recuperation des " + type, e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Impossible de fermer le fichier " + type, e);
                    }
                }
            }
            //On transforme la liste en Map
            if (membreListe != null) {
                for (Membre m : membreListe) {
                    membres.put(m.getId(), m);
                }
            }
        }
    }

    public Membre getMembre(Context context, String typeAppel, Long key) {
        if (TypeFile.members.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, membres);
            return membres.get(key);
        } else if (TypeFile.staff.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, staff);
            return staff.get(key);
        } else if (TypeFile.sponsor.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, sponsors);
            return sponsors.get(key);
        } else if (TypeFile.speaker.name().equals(typeAppel)) {
            getMapMembres(context, typeAppel, speaker);
            return speaker.get(key);
        }
        return null;
    }

    public Interet getInteret(Context context, Long id) {
        if (interets.size()==0) {
            InputStream is = null;
            List<Interet> interetListe = null;
            JsonParser jp;
            try {
                //On regarde si fichier telecharge
                File myFile = FileUtils.getFileJson(context, TypeFile.getTypeFile(TypeFile.interests.name()));
                if (myFile == null) {
                    //On prend celui inclut dans l'archive
                    is = FileUtils.getRawFileJson(context, TypeFile.getTypeFile(TypeFile.interests.name()));
                } else {
                    is = new FileInputStream(myFile);
                }
                jp = this.jsonFactory.createJsonParser(is);
                interetListe = this.objectMapper.readValue(jp, new TypeReference<List<Interet>>() {
                });
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de la recuperation des interets", e);
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Impossible de fermer le fichier ", e);
                    }
                }
            }
            //On transforme la liste en Map
            if (interetListe != null) {
                for (Interet m : interetListe) {
                    interets.put(m.getId(), m);
                }
            }
        }
        return interets.get(id);
    }
}
