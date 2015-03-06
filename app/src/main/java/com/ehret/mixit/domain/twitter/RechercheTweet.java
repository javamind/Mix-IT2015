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


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

/**
 * Objet contenant les informations de recherche renvoyé par twitter
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RechercheTweet {

    private double completed_in;
    private long max_id;
    private long max_id_str;
    private String next_page;
    private long page;
    private String query;
    private String refresh_url;
    private List<Tweet> results;
    private long results_per_page;
    private long since_id;
    private String since_id_str;

    public long getResults_per_page() {
        return results_per_page;
    }

    public void setResults_per_page(long results_per_page) {
        this.results_per_page = results_per_page;
    }

    public long getSince_id() {
        return since_id;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public String getSince_id_str() {
        return since_id_str;
    }

    public void setSince_id_str(String since_id_str) {
        this.since_id_str = since_id_str;
    }

    public double getCompleted_in() {
        return completed_in;
    }

    public void setCompleted_in(double completed_in) {
        this.completed_in = completed_in;
    }

    public long getMax_id() {
        return max_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public long getMax_id_str() {
        return max_id_str;
    }

    public void setMax_id_str(long max_id_str) {
        this.max_id_str = max_id_str;
    }

    public String getNext_page() {
        return next_page;
    }

    public void setNext_page(String next_page) {
        this.next_page = next_page;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRefresh_url() {
        return refresh_url;
    }

    public void setRefresh_url(String refresh_url) {
        this.refresh_url = refresh_url;
    }

    public List<Tweet> getResults() {
        return results;
    }

    public void setResults(List<Tweet> results) {
        this.results = results;
    }

}
