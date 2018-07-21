package com.bloomers.tedxportsaidadmin.model;


import android.support.annotation.Keep;

@Keep
public class parcel {

    private final String key;
    private final Object Value;

    public parcel(String key, String value) {
        this.key = key;
        Value = value;
    }

    public parcel(String key, Object value) {
        this.key = key;
        Value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return Value;
    }
}
