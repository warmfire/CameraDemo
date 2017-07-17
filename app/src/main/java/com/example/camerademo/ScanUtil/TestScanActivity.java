package com.example.camerademo.ScanUtil;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.camerademo.CutPicUtil.CutPic;
import com.example.camerademo.PickPicUtil.PickPictureTotalActivity;
import com.example.camerademo.R;

import java.util.ArrayList;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class TestScanActivity extends Activity implements QRCodeView.Delegate {
    private static final String TAG = TestScanActivity.class.getSimpleName();
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    private QRCodeView mQRCodeView;
    private boolean isOpen = false;
    private ImageView imageView;
    private TextView back, xiangce;
    private boolean isFirst = true;

    private String imgPath = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scan);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.setDelegate(this);
        imageView = (ImageView) findViewById(R.id.flashlight);
        back = (TextView) findViewById(R.id.txt_back);
        xiangce = (TextView) findViewById(R.id.choose_qrcde_from_gallery);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickPictureTotalActivity.gotoActivity(TestScanActivity.this);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOpen){
                    isOpen = false;
                    mQRCodeView.closeFlashlight();
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.close));
                }else{
                    isOpen = true;
                    mQRCodeView.openFlashlight();
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.open));
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.showScanRect();
        mQRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.i(TAG, "result:" + result);
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        vibrate();
        mQRCodeView.startSpot();
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mQRCodeView.showScanRect();
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 102:
                    String imgUri = data.getStringExtra("imgUri");
                    doCut(imgUri);
                    break;
            }
        }
    }

    public void doCut(final String picturePath){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(picturePath);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TestScanActivity.this);
                    String title = "未识别出结果";
                    String content = "是否将图片进行裁剪后再次扫码？";
                    String btnok = "尝试一下";
                    if(!isFirst) {
                        title = "又失败了";
                        content = "还想继续尝试吗？";
                        btnok = "再次尝试";
                    }
                    builder.setTitle(title); //设置标题
                    builder.setMessage(content);
                    builder.setPositiveButton(btnok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ArrayList<String> imagePathList = new ArrayList<String>();
                            imagePathList.add(imgPath);
                            CutPic.cropImage(TestScanActivity.this, imagePathList);
                            isFirst = false;
                        }
                    });
                    builder.setNegativeButton("算了吧", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.putExtra("result", "未识别出结果");
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    });
                    builder.create().show();

                } else {
                    Toast.makeText(TestScanActivity.this, result, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("result", result);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

}