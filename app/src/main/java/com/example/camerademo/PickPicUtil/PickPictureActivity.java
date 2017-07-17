package com.example.camerademo.PickPicUtil;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.camerademo.CutPicUtil.CutPic;
import com.example.camerademo.R;
import com.example.camerademo.ScanUtil.TestScanActivity;
import com.example.camerademo.ScanUtil.Utils;
import com.yanzhenjie.durban.Controller;
import com.yanzhenjie.durban.Durban;

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
    private TextView txt_back, txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_picture);
        mGridView = (GridView) findViewById(R.id.child_grid);
        txt_back = (TextView) findViewById(R.id.txt_back);
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText(getIntent().getStringExtra("title"));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setResult(mList.get(position));
            }
        });
        txt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        if (mList.size() > 1) {
            SortPictureList sortList = new SortPictureList();
            Collections.sort(mList, sortList);
        }
        mAdapter = new PickPictureAdapter(PickPictureActivity.this, mList);
        mGridView.setAdapter(mAdapter);
    }

    private void setResult(String picturePath) {
        ArrayList<String> imagePathList = new ArrayList<String>();
        imagePathList.add(picturePath);
        CutPic.cropImage(PickPictureActivity.this, imagePathList);
    }

    public static void gotoActivity(Activity activity, ArrayList<String> childList, String title) {
        Intent intent = new Intent(activity, PickPictureActivity.class);
        intent.putExtra("title", title);
        intent.putStringArrayListExtra("data", childList);
        activity.startActivityForResult(intent, PickPictureTotalActivity.REQUEST_CODE_SELECT_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 200:
                    Intent intent = new Intent();
                    String imgUri = Durban.parseResult(data).get(0);
                    intent.putExtra("imgUri", imgUri);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                    break;
            }
        }
    }
}
