package com.hypers.www.sample;

import android.content.pm.ApplicationInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv_rx)
    TextView mTvRx;
    @BindView(R.id.tv_tx)
    TextView mTvTx;
    @BindView(R.id.btn_get)
    Button mBtnGet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        updateNetBytes();
    }

    private void updateNetBytes() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ApplicationInfo pif = getPackageManager().getApplicationInfo(
                            getPackageName(), 0);
                    int uid = pif.uid;
                    Log.d("MainActivity", "TrafficStats.getTotalRxBytes():" +
                            TrafficStats.getTotalRxBytes() / 1024 + "KB");
                    Log.d("MainActivity", "TrafficStats.getTotalTxBytes():" +
                            TrafficStats.getTotalTxBytes() / 1024 + "KB");
                    Log.d("MainActivity", "TrafficStats.getUidRxBytes(uid):" +
                            TrafficStats.getUidRxBytes(uid) / 1024 + "KB");
                    Log.d("MainActivity", "TrafficStats.getUidTxBytes(uid):" +
                            TrafficStats.getUidTxBytes(uid) / 1024 + "KB");

//                    NetworkStatsManager nsm = (NetworkStatsManager) getSystemService(Context.NETWORK_STATS_SERVICE);
//                    nsm.querySummary(ConnectivityManager.TYPE_WIFI,
//                            uid+"",
//                            Long.MIN_VALUE,
//                            Long.MAX_VALUE);
                    //俩者相加就是应用所使用的流量
                    mTvRx.setText(pif.uid + "rx bytes = " +
                            TrafficStats.getUidRxBytes(pif.uid));
                    mTvTx.setText(pif.uid + "tx bytes  = " +
                            TrafficStats.getUidTxBytes(pif.uid));


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btn_get)
    public void onClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://www.baidu.com/s?wd=what&rsv_spt=1&rsv_iqid=0xe9ba1d4e0002930e&issp=1&f=8&rsv_bp=0&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_sug3=3&rsv_sug1=1&rsv_sug7=001&rsv_sug2=0&inputT=960&rsv_sug4=1661&rsv_sug=9");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.connect();
//                    InputStream is = connection.getInputStream();
                    String respStr = connection.getResponseMessage();
                    Log.d("MainActivity", "connection.getResponseCode():" + connection.getResponseCode());
                    Log.d("MainActivity", "respStr = " + respStr);


                    updateNetBytes();
                } catch (Exception e) {
                    Log.e("MainActivity", "e:" + e);
                }
            }
        }).start();
    }

    private byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;

    }
}
