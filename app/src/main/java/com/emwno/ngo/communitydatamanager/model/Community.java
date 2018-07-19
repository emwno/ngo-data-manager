package com.emwno.ngo.communitydatamanager.model;

import java.util.Date;
import java.util.List;

public class Community {

    private String objectId;
    private String name;
    private String leaderName;
    private String phone;
    private String address;
    private List<Community> treeData;
    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Community> getTreeData() {
        return treeData;
    }

    public void setTreeData(List<Community> treeData) {
        this.treeData = treeData;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

}