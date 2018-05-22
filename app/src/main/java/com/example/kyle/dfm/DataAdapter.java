package com.example.kyle.dfm;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static java.security.AccessController.getContext;

/**
 * Created by Kyle Frederick on 8/21/2017.
 */

public class DataAdapter extends ArrayAdapter<Data> {

        public DataAdapter(Context context, int resource, List<Data> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_view_data, parent, false);
            }
            TextView dataName = (TextView) convertView.findViewById(R.id.dataName);
            Data data = getItem(position);
            dataName.setText(data.getDataName());
            return convertView;
        }
    }

