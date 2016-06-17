package com.greatmap.idefunction;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.greatmap.idefunction.util.L;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AudioRecordActivity extends AppCompatActivity {
    private static String mFileName = null;
    @Bind(R.id.btn_record)
    Button mBtnRecord;
    @Bind(R.id.btn_play)
    Button mBtnPlay;

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        ButterKnife.bind(this);

        mFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + "audioRecord.3gp";

        initData();
    }

    private void initData() {
        getSupportActionBar().setTitle("录音");

        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnRecord.isSelected()) {
                    stopRecording();
                    mBtnRecord.setSelected(false);
                    mBtnRecord.setText("开始录音");
                }else {
                    startRecording();
                    mBtnRecord.setSelected(true);
                    mBtnRecord.setText("结束录音");
                }
            }
        });

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBtnPlay.isSelected()) {
                    stopPlaying();
                    mBtnPlay.setSelected(false);
                    mBtnPlay.setText("开始播放");
                } else {
                    startPlaying();
                    mBtnPlay.setSelected(true);
                    mBtnPlay.setText("结束播放");
                }
            }
        });
    }

    public void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecorder.start();
    }


    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mBtnPlay.setSelected(false);
                    mBtnPlay.setText("开始播放");
                    L.d("播放完成");
                }
            });
            mPlayer.prepareAsync();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopPlaying() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRecording();
        stopPlaying();
    }
}
