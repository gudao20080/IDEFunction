package com.greatmap.idefunction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScanCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_code);
    }

    private void initData() {
        getSupportActionBar().setTitle("扫码");
    }
}
