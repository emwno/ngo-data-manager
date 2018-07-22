package com.emwno.ngo.communitydatamanager.model;

import java.util.Date;
import java.util.List;

public class Community {

    private String objectId;
    private String name;
    private String leaderName;
    private String phone;
    private String address;
    private Soil soilType;
    private int cultivableArea;
    private List<Pest> pests;
    private List<Community> treeData;

    private Date created;
    private Date updated;

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
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

    public Soil getSoilType() {
        return soilType;
    }

    public void setSoilType(Soil soilType) {
        this.soilType = soilType;
    }

    public int getCultivableArea() {
        return cultivableArea;
    }

    public void setCultivableArea(int cultivableArea) {
        this.cultivableArea = cultivableArea;
    }

    public List<Pest> getPests() {
        return pests;
    }

    public void setPests(List<Pest> pests) {
        this.pests = pests;
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

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}