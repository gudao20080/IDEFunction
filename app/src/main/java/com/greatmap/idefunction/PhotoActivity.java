package com.greatmap.idefunction;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 图片展示
 */
public class PhotoActivity extends AppCompatActivity {

    @Bind(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString(Constant.KEY_TITLE, "图片");
            getSupportActionBar().setTitle(title);
            Uri uri = extras.getParcelable(Constant.KEY_URI);
            Glide.with(this).load(uri).into(mIv);
        }
    }
}
