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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.custom_listview_doubts_page_students, null);

        CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.doubts_page_students_checkbox);
        TextView name = (TextView) rowView.findViewById(R.id.doubts_page_students_name);
        TextView doubt = (TextView) rowView.findViewById(R.id.doubts_page_students_doubt);

        checkBox.setChecked(doubtsList.get(i).cleared);
        checkBox.setEnabled(false);

        final String nameVal = doubtsList.get(i).name;
        name.setText(nameVal);

        final String doubtVal = doubtsList.get(i).doubt;
        doubt.setText(doubtVal);
        checkBox.setTag(i);

        return rowView;
    }
}
