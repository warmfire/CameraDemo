package com.example.camerademo;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.camerademo.ScanUtil.TestScanActivity;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity{

    Button btn_to_scan, btn2;
    TextView txt_result, txt_result2;

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_to_scan = (Button) findViewById(R.id.btn_to_scan);
        txt_result = (TextView) findViewById(R.id.txt_result);
        txt_result2 = (TextView) findViewById(R.id.txt_result2);
        btn2 = (Button) findViewById(R.id.btn2);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_to_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestScanActivity.class);
                startActivityForResult(intent, 100);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_result.getText().toString() == null || txt_result.getText().toString().equals("")){

                }else {
                    try {
                        URL url = new URL("http://api.avatardata.cn/Barcode/Lookup?key=f6c467024d7e4e539061ebcba191c2cb&barcode=" + txt_result.getText().toString());
                        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                        urlConn.setConnectTimeout(5 * 1000);
                        urlConn.setReadTimeout(5 * 1000);
                        urlConn.setUseCaches(true);
                        urlConn.setRequestMethod("GET");
                        urlConn.setRequestProperty("Content-Type", "application/json");
                        urlConn.addRequestProperty("Connection", "Keep-Alive");
                        urlConn.connect();
                        if (urlConn.getResponseCode() == 200) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while ((len = urlConn.getInputStream().read(buffer)) != -1) {
                                baos.write(buffer, 0, len);
                            }
                            baos.close();
                            urlConn.getInputStream().close();
                            byte[] byteArray = baos.toByteArray();
                            String result = new String(byteArray);
                            txt_result2.setText(result);
                        }
                        urlConn.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 100){
                txt_result.setText(data.getStringExtra("result"));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

}
