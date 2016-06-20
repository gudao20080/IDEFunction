package com.greatmap.idefunction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.greatmap.idefunction.util.T;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 与js交互
 */
public class InteractionActivity extends AppCompatActivity {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_ALBUM_IMAGE = 2;
    @Bind(R.id.wv)
    WebView mWv;
    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction);
        ButterKnife.bind(this);
        initWebViewParams();
    }

    private void initWebViewParams() {
        WebSettings settings = mWv.getSettings();
        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(true);

        mWv.addJavascriptInterface(new InteractionInterface(), "android");
        mWv.setWebViewClient(new WebViewClient());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Uri uri = Uri.parse(mCurrentPhotoPath); //获取拍照后图片的Uri
                showPhoto("图片", uri);
            } else if (requestCode == REQUEST_ALBUM_IMAGE) {
                Uri uri = data.getData();
                showPhoto("图片", uri);
            }
        }
    }

    class InteractionInterface {
        /**
         * 发送短信
         *
         * @param phone   手机号
         * @param content 短信内容
         */
        @JavascriptInterface
        public void sendSms(String phone, String content) {
            if (TextUtils.isEmpty(phone)) {
                T.show("号码不能为空");
                return;
            }
            if (TextUtils.isEmpty(content)) {
                T.show("短信内容不能为空");
                return;
            }

            Uri uri = Uri.parse("smsto:" + phone);
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", content);
            startActivity(intent);
        }

        /**
         * 跳转至拨号界面
         *
         * @param phone 手机号
         */
        @JavascriptInterface
        public void dial(String phone) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);

        }

        /**
         * 文字提示
         * @param content 提示的内容
         */
        @JavascriptInterface
        public void showToast(final String content) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(InteractionActivity.this, content, Toast.LENGTH_SHORT).show();
                }
            });
        }

        /**
         * 拍照
         */
        @JavascriptInterface
        public void photoGraph() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        /**
         * 打开网页
         * @param title 标题
         * @param url   链接地址
         */
        @JavascriptInterface
        public void openWeb(String title, String url) {
            Intent intent = new Intent(InteractionActivity.this, WebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constant.KEY_TITLE, title);
            bundle.putString(Constant.KEY_URL, url);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        /**
         * 图片选择
         */
        @JavascriptInterface
        public void getGalleryAlbum() {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_ALBUM_IMAGE);
        }

        /**
         * 展示网络图片
         * @param title 标题
         * @param url  图片链接
         */
        public void showWebPhoto(String title, String  url) {
            if (!TextUtils.isEmpty(url)) {
                Uri uri = Uri.parse(url);
                Bundle bundle = new Bundle();
                bundle.putString(Constant.KEY_TITLE, title);
                bundle.putParcelable(Constant.KEY_URI, uri);
                Intent intent = new Intent(InteractionActivity.this, PhotoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    /**
     * 创建图片临时保存位置
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + imageFile.getAbsolutePath();
        return imageFile;
    }

    private void showPhoto(String title, Uri uri) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_TITLE, title);
        bundle.putParcelable(Constant.KEY_URI, uri);
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWv.canGoBack()) {
            mWv.goBack();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
