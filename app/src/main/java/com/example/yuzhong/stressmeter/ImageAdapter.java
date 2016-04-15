package com.example.yuzhong.stressmeter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by Yuzhong on 2016/4/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int[] mThumbIds;

    public ImageAdapter(Context c) {
        mContext = c;
        mThumbIds = PSM.getGridById(1);
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(320, 320));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0, 0, 0, 0);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    public void changePicture(int num){
        mThumbIds = PSM.getGridById(num);
    }

    public int[] getmThumbIds(){
        return mThumbIds;
    }
    // references to our images
//    private Integer[] mThumbIds = {
//            R.drawable.psm_alarm_clock, R.drawable.psm_alarm_clock2,
//            R.drawable.psm_angry_face, R.drawable.fish_normal017,
//            R.drawable.ic_menu_camera, R.drawable.ic_menu_gallery,
//            R.drawable.ic_menu_manage, R.drawable.ic_menu_send,
//            R.drawable.ic_menu_share, R.drawable.psm_baby_sleeping,
//            R.drawable.psm_alarm_clock, R.drawable.psm_barbed_wire2,
//            R.drawable.psm_cat, R.drawable.psm_clutter,
//            R.drawable.psm_dog_sleeping, R.drawable.psm_lake3,
//    };
}
