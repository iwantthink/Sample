package com.hypers.www.sample;

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

    public static final String CONNECTION_URL =
            "https://m.irs01.com/hmt?_t=i&_z=m";
    //    public static final String CONNECTION_URL = "https://www.baidu.com";
    String http_req = "http://office.hypers.com.cn/hmt?_t=i&_z=m";
    public static String PRE_URL = "https://m.irs01.com/hmt?_t=i&_z=m";
    public static String PRE_REQ_URL = "https://m.irs01.com/imt?_t=i&_z=m";
    public static String ONLINE_CONFIG_URL = "https://m.irs01.com/hmt_pro/project/";
    String date = "{\"client_data_list\":[{\"mac1\":\"27d0c7ba1353c99ce20ef0d19d394620\",\"model\":\"GT-I9300\",\"ts\":\"1491817450270\",\"char\":\"\",\"is_jail_break\":true,\"app_version\":\"1.0.2\",\"mac\":\"4adf9b3762ff4b683f0353018b9b6a94\",\"type\":\"client_data\",\"app_name\":\"Hmtdemo\",\"lang\":\"zh\",\"lac\":\"43042\",\"network\":\"WIFI\",\"_openudid\":\"9cf6f3145ec9fce3\",\"androidid\":\"5a3592ceba4eebcd37b6ad27380d81a5\",\"is_mobile_device\":true,\"device_name\":\"Samsung GT-I9300\",\"have_gps\":true,\"_mac\":\"5c:0a:5b:b1:3e:d5\",\"have_gravity\":true,\"sv\":\"1.0.16\",\"_imei\":\"353818055991309\",\"app_code\":\"3\",\"lat\":\"31.257785\",\"os\":\"0\",\"lon\":\"121.484951\",\"useragent\":\"Hmtdemo_1.0.2\",\"cell_id\":\"11451695\",\"phone_type\":1,\"sr\":\"720x1280\",\"imei\":\"5c7c020549532e2b65eb2c6f3d4e27bc\",\"sd\":\"\",\"package_name\":\"hmtdemo.hmt.com.hmtdemo.wandoujia.debug\",\"_ua\":\"hmt_9KW6JRVT\",\"device_id\":\"b4a8d3943233d9d96a2e1ed25ecaa2a0\",\"muid\":\"\",\"have_wifi\":true,\"channel_id\":\"hmt\",\"os_version\":\"4.4\",\"v\":\"1.7.1\",\"androidid1\":\"9cf6f3145ec9fce3\",\"have_bt\":true,\"manufacturer\":\"samsung\",\"producer\":\"m0zc\",\"aaid\":\"\",\"_androidid\":\"9cf6f3145ec9fce3\",\"mccmnc\":\"\",\"imsi\":\"\"}]}";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        updateNetBytes();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                MyMessage myMessage = NetworkUitlity.get(CONNECTION_URL);
//                Log.d("MainActivity", "myMessage.getMsg():" + myMessage.getMsg());
//                Log.d("MainActivity", "myMessage.isFlag():" + myMessage.isFlag());
//
//            }
//        }).start();
    }

    @OnClick(R.id.btn_get)
    public void onClick() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                MyMessage myMessage = NetworkUitlity.get(ONLINE_CONFIG_URL + "xx.config");
                boolean resullt = NetworkUitlity.post(PRE_URL, date, "all_data");
                Log.d("MainActivity", "resullt:" + resullt);
//                Log.d("MainActivity", "myMessage.getMsg():" + myMessage.getMsg());
//                Log.d("MainActivity", "myMessage.isFlag():" + myMessage.isFlag());

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
