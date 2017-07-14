package com.example.camerademo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 手机图片列表中的详细
 * Created by hupei on 2016/7/7.
 */
public class PickPictureActivity extends Activity {
    private GridView mGridView;
    private List<String> mList;//此相册下所有图片的路径集合
    private PickPictureAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);
        mGridView = (GridView) findViewById(R.id.child_grid);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(mList.get(position));
            }
        });
        processExtraData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        processExtraData();
    }

    private void processExtraData() {
        Bundle extras = getIntent().getExtras();
        if (extras == null)
            return;
        mList = extras.getStringArrayList("data");

        Toast.makeText(PickPictureActivity.this, mList.size(), Toast.LENGTH_LONG).show();
        if (mList.size() > 1) {
            SortPictureList sortList = new SortPictureList();
            Collections.sort(mList, sortList);
        }
        mAdapter = new PickPictureAdapter(PickPictureActivity.this, mList);
        mGridView.setAdapter(mAdapter);
    }

    private void setResult(String picturePath) {
        Intent intent = new Intent();
        intent.putExtra(PickPictureTotalActivity.EXTRA_PICTURE_PATH, picturePath);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public static void gotoActivity(Activity activity, ArrayList<String> childList) {
        Intent intent = new Intent(activity, PickPictureActivity.class);
        intent.putStringArrayListExtra("data", childList);
        activity.startActivityForResult(intent, PickPictureTotalActivity.REQUEST_CODE_SELECT_ALBUM);
    }
}
