package com.emwno.ngo.datamanager.model;

import java.util.Date;

import io.realm.RealmObject;

public class Pest extends RealmObject {

    private String objectId;
    private String type;
    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public String toString() {
        return type;
    }

}