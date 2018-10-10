package com.example.kyle.dfm;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<AddressData> {

    public AddressAdapter(Context context, int resource, List<AddressData> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_view_address, parent, false);
        }
        TextView dataName = (TextView) convertView.findViewById(R.id.addressName);
        AddressData addressData = getItem(position);
        dataName.setText(addressData.getAddressName());
        return convertView;
    }
}


