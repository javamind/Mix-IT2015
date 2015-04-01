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
package com.ehret.mixit.domain.talk;

import com.ehret.mixit.domain.Salle;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Les lightning talk ont plusieurs particularités dont le nb de votes
 */
public class Lightningtalk extends Conference<Lightningtalk> {

    private int nbVotes;

    public int getNbVotes() {
        return nbVotes;
    }

    public void setNbVotes(int nbVotes) {
        this.nbVotes = nbVotes;
    }

    @Override
    public String getRoom() {
        return Salle.SALLE7.getNom();
    }

    @Override
    public Date getStart() {
        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        calendar.set(2015, 3, 16, 13, 0 , 0);
        return calendar.getTime();
    }

    @Override
    public Date getEnd() {
        Calendar calendar = Calendar.getInstance(Locale.FRANCE);
        calendar.set(2015, 3, 16, 13, 30 , 0);
        return calendar.getTime();
    }
}
