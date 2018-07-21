package com.bloomers.tedxportsaidadmin.model;

import android.support.annotation.Keep;

@Keep
public class Speaker {

    private String description;
    private String prof_url;
    private String name;
    private String topic;

    public Speaker(){

    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getDescription() {
        return description;
    }

    public String getProf_url() {
        return prof_url;
    }

    public Speaker(String name, String topic, String description, String prof_url) {
        this.description = description;
        this.prof_url = prof_url;
        this.name = name;
        this.topic = topic;
    }
}
