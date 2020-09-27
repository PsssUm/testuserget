package com.psssum.docs.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psssum.docs.R;
import com.psssum.docs.utils.ConvertUtils;
import com.skydoves.powermenu.MenuBaseAdapter;
import com.skydoves.powermenu.PowerMenuItem;

public class MenuAdapter extends MenuBaseAdapter<PowerMenuItem> {

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_icon_menu, viewGroup, false);
        }

        PowerMenuItem item = (PowerMenuItem) getItem(index);

        final TextView title = view.findViewById(R.id.itemTitleTV);
        int left = Math.round(ConvertUtils.convertDpToPixel(20f, context));
        int top = Math.round(ConvertUtils.convertDpToPixel(12f, context));
        int topFirst = Math.round(ConvertUtils.convertDpToPixel(24f, context));
        int right = Math.round(ConvertUtils.convertDpToPixel(20f, context));
        int bot = Math.round(ConvertUtils.convertDpToPixel(12f, context));
        int lastItemBottom = Math.round(ConvertUtils.convertDpToPixel(24f, context));

        if (index == 1){
            title.setPadding(left, top, right, lastItemBottom);
        } else {
            title.setPadding(left, topFirst, right, bot);
        }

        title.setText(item.getTitle());
        return super.getView(index, view, viewGroup);
    }
}