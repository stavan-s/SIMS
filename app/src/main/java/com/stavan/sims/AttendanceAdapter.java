package com.stavan.sims;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

class Attendance {
    String lecName;
    String status;

    Attendance() {}

    Attendance(String lecName, String status) {
        this.lecName = lecName;
        this.status = status;
    }
}

class ViewHolderAttendance {
    TextView lecNameDisplay;
    TextView statusDisplay;
}

public class AttendanceAdapter extends BaseAdapter {

    Context context;
    List<Attendance> attendanceList;

    public AttendanceAdapter(Context context, List<Attendance> attendanceList) {
        this.context = context;
        this.attendanceList = attendanceList;
    }

    @Override
    public int getCount() {
        return attendanceList.size();
    }

    @Override
    public Object getItem(int i) {
        return attendanceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;

        ViewHolderAttendance viewHolder = new ViewHolderAttendance();

        if(rowView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.custom_listview_attendance, null);

            viewHolder.lecNameDisplay = rowView.findViewById(R.id.attendance_listview_lecname);
            viewHolder.statusDisplay = rowView.findViewById(R.id.attendance_listview_status);
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolderAttendance) rowView.getTag();
        }

        viewHolder.lecNameDisplay.setText(attendanceList.get(i).lecName);

        String status = attendanceList.get(i).status;
        viewHolder.statusDisplay.setText(status);

        if(status.equals("A"))
            viewHolder.statusDisplay.setTextColor(Color.RED);
        else
            viewHolder.statusDisplay.setTextColor(Color.rgb(50,205,50));

        return rowView;
    }
}
