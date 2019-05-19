package com.emwno.ngo.datamanager;

import com.emwno.ngo.datamanager.model.Pest;
import com.emwno.ngo.datamanager.model.TreeData;

import java.util.List;

/**
 * Created on 23 Jul 2018.
 */
public class DataBindingHelper {

    public static String pestListToString(List<Pest> list) {
        if (list.size() == 0) return "";
        StringBuilder b = new StringBuilder();
        for (Pest pest : list) {
            b.append(pest.getType());
            b.append(", ");
        }
        b.delete(b.length() - 2, b.length() - 1);
        return b.toString();
    }

    public static String treeListToString(List<TreeData> list) {
        if (list.size() == 0) return "";
        StringBuilder b = new StringBuilder();
        for (TreeData treeData : list) {
            b.append(treeData.getTree().getType());
            b.append(", ");
        }
        b.delete(b.length() - 2, b.length() - 1);
        return b.toString();
    }

    public static String treeReceivedListToString(List<TreeData> list) {
        if (list.size() == 0) return "";
        StringBuilder b = new StringBuilder();
        for (TreeData treeData : list) {
            b.append(treeData.getTreeNumberReceived());
            b.append(", ");
        }
        b.delete(b.length() - 2, b.length() - 1);
        return b.toString();
    }

    public static String treeAliveListToString(List<TreeData> list) {
        if (list.size() == 0) return "";
        StringBuilder b = new StringBuilder();
        for (TreeData treeData : list) {
            b.append(treeData.getTreeNumberAlive());
            b.append(", ");
        }
        b.delete(b.length() - 2, b.length() - 1);
        return b.toString();
    }

}
