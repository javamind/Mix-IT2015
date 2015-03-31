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
package com.ehret.mixit.domain;

/**
 * Différents type de fichier pouvant être affichés à l'utilsiateur
 */
public enum TypeFile {
    speaker,
    staff,
    talks,
    test,
    workshops,
    members,
    lightningtalks,
    sponsor,
    favorites,
    interests;

    public static TypeFile getTypeFile(String value) {
        for (TypeFile typeFile : values()) {
            if (typeFile.name().equals(value)) {
                return typeFile;
            }
        }
        return null;
    }
}
