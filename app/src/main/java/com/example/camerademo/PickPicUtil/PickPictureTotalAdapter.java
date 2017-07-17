package com.example.camerademo.PickPicUtil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.camerademo.R;

import java.util.List;

/**
 * Created by hupei on 2016/7/14.
 */
class PickPictureTotalAdapter extends CygAdapter<Picture> {

    public PickPictureTotalAdapter(Context context, List<Picture> objects) {
        super(context, R.layout.activity_pick_picture_total_list_item, objects);
    }

    @Override
    public void onBindData(CygViewHolder viewHolder, Picture item, int position) {
        viewHolder.setText(R.id.pick_picture_total_list_item_group_title, item.getFolderName());
        viewHolder.setText(R.id.pick_picture_total_list_item_group_count
                , Integer.toString(item.getPictureCount()) + " 张照片");
        ImageView imageView = viewHolder.findViewById(R.id.pick_picture_total_list_item_group_image);
        Glide.with(mContext).load(item.getTopPicturePath()).into(imageView);
        ImageView imageView2 = viewHolder.findViewById(R.id.pick_picture_total_list_item_group_image2);
        Glide.with(mContext).load(item.getSecondPicturePath()).into(imageView2);
        ImageView imageView3 = viewHolder.findViewById(R.id.pick_picture_total_list_item_group_image3);
        Glide.with(mContext).load(item.getSecondPicturePath()).into(imageView3);
    }
}
