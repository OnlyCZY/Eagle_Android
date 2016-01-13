package com.buoyantec.eagle_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kang on 15/12/31.
 * 应用首页的Grid适配器
 */
public class MainGridAdapter extends BaseAdapter {
    private Context mContext;
    private Integer[] images;
    private String[] texts;
    private GridView gridView;

    public MainGridAdapter(GridView gridView, Context c, Integer[] images, String[] texts) {
        this.gridView = gridView;
        this.mContext = c;
        this.images = images;
        this.texts = texts;
    }

    public int getCount() {
        return images.length;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.grid_item, parent, false);
        }
        TextView tv = BaseViewHolder.get(convertView, R.id.grid_view_text);
        ImageView iv = BaseViewHolder.get(convertView, R.id.grid_view_image);

        iv.setBackgroundResource(images[position]);
        tv.setText(texts[position]);
        //添加徽章提示信息
        if (position == 1) {
            BadgeView badge = new BadgeView(mContext, iv);
            badge.setText("3");
            badge.setBadgeMargin(0);
            badge.show();
        }

        return convertView;
    }
}