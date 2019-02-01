
package com.tuya.smart.android.demo.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.tuya.smart.android.base.ApiParams;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.camera.bean.CameraInfoBean;
import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.sdk.api.IRequestCallback;

import java.util.HashMap;
import java.util.Map;

import static com.tuya.smart.android.demo.device.common.CommonDeviceDebugPresenter.INTENT_DEVID;
import static com.tuya.smart.android.demo.device.common.CommonDeviceDebugPresenter.INTENT_LOCALKEY;
import static com.tuya.smart.android.demo.device.common.CommonDeviceDebugPresenter.INTENT_P2P_TYPE;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTutkSoft;
    private Button btnTutkHead;
    private Button btnMonitor;

    private String localkey;
    private String devId;
    private int p2pType;

    private CameraInfoBean infoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        btnTutkSoft = findViewById(R.id.btn_tutk_soft_button);
        btnTutkHead = findViewById(R.id.btn_tutk_head_button);
        btnMonitor = findViewById(R.id.btn_monitor_button);
        btnTutkSoft.setOnClickListener(this);
        btnTutkHead.setOnClickListener(this);
        btnMonitor.setOnClickListener(this);
        Intent intent = getIntent();
        localkey = intent.getStringExtra(INTENT_LOCALKEY);
        devId = intent.getStringExtra(INTENT_DEVID);
        p2pType = intent.getIntExtra(INTENT_P2P_TYPE, -1);
        getApi();
    }


    private void getApi() {
        Map postData = new HashMap();
        postData.put("devId", devId);
        TuyaHomeSdk.getRequestInstance().requestWithApiName("tuya.m.ipc.config.get", "1.0",
                postData, new IRequestCallback() {
                    @Override
                    public void onSuccess(Object o) {
                        infoBean = JSONObject.parseObject(o.toString(),CameraInfoBean.class);
                        Log.d("onSuccess", o.toString());
                    }

                    @Override
                    public void onFailure(String s, String s1) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        if (R.id.btn_tutk_soft_button == view.getId()) {
            intent.putExtra("isRunsoft", true);
            intent.putExtra("p2pId", infoBean.getP2pId());
            intent.putExtra("p2pWd", infoBean.getPassword());
            intent.putExtra("localKey", localkey);
            intent.putExtra("p2pType", p2pType);
        } else if (R.id.btn_tutk_head_button == view.getId()) {
            intent.putExtra("isRunsoft", false);
            intent.putExtra("p2pId", "xxxx");
            intent.putExtra("p2pWd", "xxxx");
            intent.putExtra("localKey", "xxx");
            intent.putExtra("p2pType", 1);
        } else if (R.id.btn_monitor_button == view.getId()) {
            intent.putExtra("isRunsoft", false);
            intent.putExtra("p2pId", "TYQJAA-008631-TYNSM");
            intent.putExtra("p2pWd", "ad355ab0");
            intent.putExtra("localKey", "692e7c170ce0f5de");
            intent.putExtra("p2pType", 2);
        }
        startActivity(intent);
    }

}
