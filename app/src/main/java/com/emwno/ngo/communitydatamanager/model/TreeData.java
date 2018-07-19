package com.emwno.ngo.communitydatamanager.model;

import java.util.Date;

public class TreeData {

    private String objectId;
    private Tree tree;
    private int treeCount;
    private Date dateReceived;
    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public Integer getTreeCount() {
        return treeCount;
    }

    public void setTreeCount(Integer treeCount) {
        this.treeCount = treeCount;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

}