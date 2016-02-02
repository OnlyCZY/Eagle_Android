package com.buoyantec.eagle_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.buoyantec.eagle_android.R;

/**
 * Created by kang on 15/12/31.
 * 其他子页面的适配器
 */
public class SystemStatusGridAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] images;
    private String[] texts;
    private GridView gridView;

    public SystemStatusGridAdapter(GridView gridView, Context c, Integer[] images, String[] texts) {
        this.gridView = gridView;
        this.mContext = c;
        this.images = images;
        this.texts = texts;
    }

    public int getCount() {
        int count = images.length;
        int remainder = count%3;
        int value = 0;
        if (count<=3) {
            value = 3;
        } else if (count>3 && remainder==0) {
            value = count;
        } else if (count>3 && remainder!=0) {
            value = 3-remainder+count;
        }
        return value;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item_system_status, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.sub_grid_view_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.sub_grid_view_image);

        if (position+1 <= images.length) {
            iv.setBackgroundResource(images[position]);
            tv.setText(texts[position]);
        } else {
            iv.setBackgroundResource(R.drawable.system_status_empty);
            tv.setText("");
        }

        return convertView;
    }
}