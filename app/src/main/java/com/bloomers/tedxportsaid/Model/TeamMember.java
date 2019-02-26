package com.bloomers.tedxportsaid.Model;


import android.support.annotation.Keep;

@Keep
public class TeamMember {

    private String desc;
    private String name;

    private String profile_url;

    private String team;

    public TeamMember() {

    }

    public String getDesc() {
        return desc;
    }

    public TeamMember(String name, String team, String desc, String profile_url) {
        this.desc = desc;
        this.name = name;
        this.profile_url = profile_url;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public String getTeam() {
        return team;
    }
}
