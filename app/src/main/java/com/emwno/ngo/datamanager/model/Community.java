package com.emwno.ngo.datamanager.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.emwno.ngo.datamanager.BR;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Community extends BaseObservable {

    private String objectId;
    private String name;
    private String leaderName;
    private String phone;
    private String address;
    private Soil soilType;
    private double cultivableArea;
    private String treeSoldTo;
    private double treeSoldFor;
    private int treeReadyToBeSold;
    private List<Pest> pests;
    private List<TreeData> treeData;
    private Date treeDateReceived = new Date();

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

    @Bindable
    public Soil getSoilType() {
        return soilType;
    }

    public void setSoilType(Soil soilType) {
        this.soilType = soilType;
        notifyPropertyChanged(BR.soilType);
    }

    public double getCultivableArea() {
        return cultivableArea;
    }

    public void setCultivableArea(double cultivableArea) {
        this.cultivableArea = cultivableArea;
    }

    public String getTreeSoldTo() {
        return treeSoldTo;
    }

    public void setTreeSoldTo(String treeSoldTo) {
        this.treeSoldTo = treeSoldTo;
    }

    public double getTreeSoldFor() {
        return treeSoldFor;
    }

    public void setTreeSoldFor(double treeSoldFor) {
        this.treeSoldFor = treeSoldFor;
    }

    public int getTreeReadyToBeSold() {
        return treeReadyToBeSold;
    }

    public void setTreeReadyToBeSold(int treeReadyToBeSold) {
        this.treeReadyToBeSold = treeReadyToBeSold;
    }

    @Bindable
    public List<Pest> getPests() {
        return pests;
    }

    public void setPests(List<Pest> pests) {
        this.pests = pests;
        notifyPropertyChanged(BR.pests);
    }

    public List<TreeData> getTreeData() {
        return treeData;
    }

    public void setTreeData(List<TreeData> treeData) {
        this.treeData = treeData;
    }

    public String getTreeDateReceived() {
        return new SimpleDateFormat("dd/MM/yyyy").format(treeDateReceived);
    }

    public void setTreeDateReceived(String treeDateReceived) {
        try {
            this.treeDateReceived = new SimpleDateFormat("dd/MM/yyyy").parse(treeDateReceived);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    @Override
    public String toString() {
        return "Community{" +
                "\n  name='" + name + '\'' +
                "\n  leaderName='" + leaderName + '\'' +
                "\n  phone='" + phone + '\'' +
                "\n  address='" + address + '\'' +
                "\n  soilType=" + soilType +
                "\n  cultivableArea=" + cultivableArea +
                "\n  pests=" + pests +
                "\n  pests=" + treeData +
                "\n treeDateReceived='" + treeDateReceived + '\'' +
                "\n}";
    }

}