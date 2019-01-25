package com.tuya.smart.android.demo.camera;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tuya.smart.android.demo.R;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTutkSoft;
    private Button btnTutkHead;
    private Button btnMonitor;

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
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        intent = new Intent(this, MainActivity.class);
        if (R.id.btn_tutk_soft_button == view.getId()) {
            intent.putExtra("isRunsoft", true);
            intent.putExtra("p2pId", "xxxxx");
            intent.putExtra("p2pWd", "xxxx");
            intent.putExtra("localKey", "xxxx");
            intent.putExtra("p2pType", 1);
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
