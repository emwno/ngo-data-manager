package com.emwno.ngo.datamanager.table.view;

import android.view.View;
import android.widget.TextView;

import com.emwno.ngo.datamanager.R;
import com.evrencoskun.tableview.adapter.recyclerview.holder.AbstractViewHolder;

public class ColumnHeaderViewHolder extends AbstractViewHolder {

    private TextView textView;

    public ColumnHeaderViewHolder(View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.column_header_textView);
    }

    public void bind(String title) {
        textView.setText(title);
    }

}