package com.emwno.ngo.datamanager.model;

import java.util.Date;

public class TreeData {

    private String objectId;
    private Tree tree;
    private int treeNumberReceived;
    private int treeNumberAlive;
    private int treeNumberSold;
    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public int getTreeNumberReceived() {
        return treeNumberReceived;
    }

    public void setTreeNumberReceived(int treeNumberReceived) {
        this.treeNumberReceived = treeNumberReceived;
    }

    public int getTreeNumberAlive() {
        return treeNumberAlive;
    }

    public void setTreeNumberAlive(int treeNumberAlive) {
        this.treeNumberAlive = treeNumberAlive;
    }

    public int getTreeNumberSold() {
        return treeNumberSold;
    }

    public void setTreeNumberSold(int treeNumberSold) {
        this.treeNumberSold = treeNumberSold;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public String toString() {
        return "\nTreeData{" +
                "\n tree=" + tree +
                "\n treeNumberReceived=" + treeNumberReceived +
                "\n treeNumberAlive=" + treeNumberAlive +
                "\n}";
    }

}