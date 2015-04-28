package com.example.bob.apka;

/**
 * Created by Bob on 2015-01-29.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends ArrayAdapter<Data> {

    private final Context con;
    List<Data> val;

    public MyAdapter(Context c, List<Data> v) {
        super(c, R.layout.item, v);
        con = c;
        val = v;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inf.inflate(R.layout.item, parent, false);

        if(val.size()>0) {
            TextView text = (TextView) row.findViewById(R.id.textView);
            text.setText(val.get(position).a);
            TextView text1 = (TextView) row.findViewById(R.id.textView2);
            text1.setText(val.get(position).b);
            TextView text2 = (TextView) row.findViewById(R.id.textView3);
            text2.setText(val.get(position).c);
        }
        return row;
    }

    public void removeAll() {
     val.clear();
    }

}
