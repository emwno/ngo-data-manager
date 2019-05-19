package com.emwno.ngo.datamanager.table.view;

import android.view.View;
import android.widget.TextView;

import com.emwno.ngo.datamanager.R;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class CellViewHolder extends AbstractViewHolder {

    private TextView textView;

    public CellViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.cell_data);
    }

    public void bind(String title) {
        textView.setText(title);
    }

}