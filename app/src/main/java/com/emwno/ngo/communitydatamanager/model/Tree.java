package com.emwno.ngo.communitydatamanager.model;

import java.util.Date;

public class Tree {

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

}