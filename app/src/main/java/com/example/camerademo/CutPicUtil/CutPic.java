package com.example.camerademo.CutPicUtil;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.example.camerademo.PickPicUtil.PickPictureActivity;
import com.example.camerademo.R;
import com.example.camerademo.ScanUtil.Utils;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

import java.util.ArrayList;

/**
 * Created by warmfire on 2017-07-17.
 */

public class CutPic {

    public static void cropImage(Activity activity, ArrayList<String> imagePathList) {
        Durban.with(activity)
                .statusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark))
                .toolBarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .navigationBarColor(ContextCompat.getColor(activity, R.color.colorPrimary))
                .inputImagePaths(imagePathList)
                //.outputDirectory(cropDirectory)
                .maxWidthHeight(500, 500)
                .aspectRatio(1, 1)
                .compressFormat(Durban.COMPRESS_JPEG)
                .compressQuality(90)
                // Gesture: ROTATE, SCALE, ALL, NONE.
                .gesture(Durban.GESTURE_ALL)
                .controller(Controller.newBuilder()
                        .enable(false)
                        .rotation(true)
                        .rotationTitle(true)
                        .scale(true)
                        .scaleTitle(true)
                        .build())
                .requestCode(200)
                .start();
    }
}
