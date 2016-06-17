package com.greatmap.idefunction;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.KeyEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.greatmap.idefunction.network.DataAccessUtil;
import com.greatmap.idefunction.util.L;
import com.greatmap.idefunction.util.T;
import com.greatmap.idefunction.zxing.GenerateActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_ALBUM_IMAGE = 2;

    @Bind(R.id.btn_notification)
    Button mBtnNotification;
    @Bind(R.id.btn_showToast)
    Button mBtnShowToast;
    @Bind(R.id.btn_photograph)
    Button mBtnPhotograph;
    @Bind(R.id.btn_gallery)
    Button mBtnGallery;
    @Bind(R.id.btn_upload_file)
    Button mBtnUploadFile;
    @Bind(R.id.btn_showPhoto)
    Button mBtnShowPhoto;
    @Bind(R.id.btn_open_web)
    Button mBtnOpenWeb;
    @Bind(R.id.btn_send_sms)
    Button mBtnSendSms;
    @Bind(R.id.btn_dial)
    Button mBtnDial;
    @Bind(R.id.btn_audio_record)
    Button mBtnAudioRecord;
    @Bind(R.id.btn_media_record)
    Button mBtnMediaRecord;
    @Bind(R.id.btn_scan_code)
    Button mBtnScanCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
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

    /**
     * 通知媒体库有新图片加入，这样使图片在Android的相册或其它应用都能生效
     */
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void initView() {
        mBtnNotification.setOnClickListener(this);
        mBtnShowToast.setOnClickListener(this);
        mBtnPhotograph.setOnClickListener(this);
        mBtnOpenWeb.setOnClickListener(this);
        mBtnShowPhoto.setOnClickListener(this);
        mBtnGallery.setOnClickListener(this);
        mBtnDial.setOnClickListener(this);
        mBtnSendSms.setOnClickListener(this);
        mBtnAudioRecord.setOnClickListener(this);
        mBtnMediaRecord.setOnClickListener(this);
        mBtnScanCode.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notification:
                showNotification();
                break;
            case R.id.btn_showToast:
                showToast("这是一个错误");
                break;
            case R.id.btn_photograph:
                photoGraph();
                break;
            case R.id.btn_showPhoto:
                String photoUrl = "http://img4.imgtn.bdimg.com/it/u=2467748852,1434223916&fm=21&gp=0.jpg";
                Uri uri = Uri.parse(photoUrl);
                showPhoto("网络图片", uri);
                break;
            case R.id.btn_open_web:
                openWeb("百度", "https://www.baidu.com/");
                break;
            case R.id.btn_gallery:
                getGalleryAlbum();
                break;

            case R.id.btn_upload_file:
//                uploadImage();
                break;

            case R.id.btn_dial:
                dial("123222222");
                break;

            case R.id.btn_send_sms:
                sendSms(this, "123333333", "ddddd");
                break;

            case R.id.btn_audio_record:
                startActivity(new Intent(this, AudioRecordActivity.class));
                break;
            case R.id.btn_media_record:
                startActivity(new Intent(this, VideoRecordActivity.class));
                break;
            case R.id.btn_scan_code:
                startActivity(new Intent(this, GenerateActivity.class));

                break;
        }
    }

    public static void sendSms(Context context, String phoneNumber, String content) {
        if (TextUtils.isEmpty(phoneNumber)) {
            T.show("号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(content)) {
            T.show("短信内容不能为空");
            return;
        }

        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        context.startActivity(intent);
    }

    /**
     * 跳转至拨号界面
     *
     * @param phone
     */
    public void dial(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);

    }

    public void uploadImage(File file) {
        Observable<ResponseBody> observable = DataAccessUtil.upload(file);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        T.show(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String s = responseBody.string();
                            L.d(s);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 拍照
     */
    private void photoGraph() {
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
     * 图片选择
     */
    private void getGalleryAlbum() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_ALBUM_IMAGE);
    }

    String mCurrentPhotoPath;

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

    private void openWeb(String title, String url) {
        Intent intent = new Intent(this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constant.KEY_TITLE, title);
        bundle.putString(Constant.KEY_URL, url);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    private void showToast(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotification() {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher, 3)
                .setContentTitle("标题")
                .setContentText("这是通知内容")
                .setAutoCancel(true);

//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle.setBigContentTitle("大标题");
//        inboxStyle.addLine("aaa");
//        inboxStyle.addLine("bbb");
//        builder.setStyle(inboxStyle);

        Notification notification = builder.build();
        nm.notify(1, notification);
    }
}
