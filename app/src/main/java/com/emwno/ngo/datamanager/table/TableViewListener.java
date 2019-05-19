package com.emwno.ngo.datamanager.table;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.evrencoskun.tableview.listener.ITableViewListener;

/**
 * Created on 11 Aug 2018.
 */
public interface TableViewListener extends ITableViewListener {

    default void onCellClicked(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    }

    default void onCellLongPressed(@NonNull RecyclerView.ViewHolder cellView, int column, int row) {
    }

    default void onColumnHeaderClicked(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    }

    default void onColumnHeaderLongPressed(@NonNull RecyclerView.ViewHolder columnHeaderView, int column) {
    }

    default void onRowHeaderClicked(@NonNull RecyclerView.ViewHolder rowHeaderView, int row) {
    }

    void onRowHeaderLongPressed(@NonNull RecyclerView.ViewHolder rowHeaderView, int
            row);


}
