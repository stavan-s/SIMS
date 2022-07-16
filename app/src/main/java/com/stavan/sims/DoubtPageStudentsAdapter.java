package com.stavan.sims;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class DoubtPageStudentsAdapter extends BaseAdapter {

    List<Doubt> doubtsList;
    Context context;

    public DoubtPageStudentsAdapter(Context context, List<Doubt> doubtsList) {
        this.context = context;
        this.doubtsList = doubtsList;
    }

    @Override
    public int getCount() {
        return doubtsList.size();
    }

    @Override
    public Object getItem(int i) {
        return doubtsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean isCleared(int position) {
        return doubtsList.get(position).isCleared();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;

        ViewHolderDoubtPage viewHolder = new ViewHolderDoubtPage();

        if(rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.custom_listview_doubts_page_students, null);

            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.doubts_page_students_checkbox);
            viewHolder.name = (TextView) rowView.findViewById(R.id.doubts_page_students_name);
            viewHolder.doubt = (TextView) rowView.findViewById(R.id.doubts_page_students_doubt);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolderDoubtPage) rowView.getTag();
        }

        viewHolder.checkBox.setChecked(doubtsList.get(i).cleared);
        viewHolder.checkBox.setEnabled(false);

        final String name = doubtsList.get(i).name;
        viewHolder.name.setText(name);

        final String doubt = doubtsList.get(i).doubt;
        viewHolder.doubt.setText(doubt);
        viewHolder.checkBox.setTag(i);

        return rowView;
    }
}
