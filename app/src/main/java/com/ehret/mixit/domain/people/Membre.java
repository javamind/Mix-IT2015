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
package com.ehret.mixit.domain.people;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Participant à Mix-it : staff, speaker...
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Membre {

    private long id;
    private String firstName;
    private String lastName;
    private String login;
    private String company;
    private String shortdesc;
    private String longdesc;
    private String urlimage;
    private String nbConsults;

    private List<Long> linkers;
    private List<Link> sharedLinks;
    private List<Long> links;
    private List<Long> interests;
    private String logo;
    private String level;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Long> getInterests() {
        return interests;
    }

    public void setInterests(List<Long> interests) {
        this.interests = interests;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Long> getLinks() {
        return links;
    }

    public void setLinks(List<Long> links) {
        this.links = links;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCompleteName() {
        if(firstName ==null || "".equals(firstName)){
            return lastName !=null ? lastName.toUpperCase() : "unknown";
        }
        return lastName.toUpperCase() + " " + firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName ==null ? "unknown" : lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getLongdesc() {
        return longdesc;
    }

    public void setLongdesc(String longdesc) {
        this.longdesc = longdesc;
    }

    public String getUrlimage() {
        return urlimage;
    }

    public void setUrlimage(String urlimage) {
        this.urlimage = urlimage;
    }

    public String getNbConsults() {
        return nbConsults;
    }

    public void setNbConsults(String nbConsults) {
        this.nbConsults = nbConsults;
    }

    public List<Long> getLinkers() {
        return linkers;
    }

    public void setLinkers(List<Long> linkers) {
        this.linkers = linkers;
    }

    public List<Link> getSharedLinks() {
        return sharedLinks;
    }

    public void setSharedLinks(List<Link> sharedLinks) {
        this.sharedLinks = sharedLinks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Membre speaker = (Membre) o;

        return id == speaker.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
