package com.tuya.smart.android.demo.camera;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.utils.Constants;
import com.tuya.smart.camera.middleware.ITuyaSmartCamera;
import com.tuya.smart.camera.middleware.TuyaSmartCameraFactory;
import com.tuya.smart.camera.middleware.view.TuyaMonitorView;
import com.tuya.smart.camera.tuyadeleagte.ICameraP2P;
import com.tuya.smart.camera.tuyadeleagte.OnDelegateCameraListener;
import com.tuya.smart.camera.tuyadeleagte.bean.TimePieceBean;
import com.tuya.tutk.OnTutkCameraListener;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnDelegateCameraListener {

    private static final String TAG = "MainActivity";
    private TuyaMonitorView mVideoView;
    ITuyaSmartCamera camera;
    private static final int ASPECT_RATIO_WIDTH = 9;
    private static final int ASPECT_RATIO_HEIGHT = 16;
    private String p2pId = "", p2pWd = "", localKey = "",
            mInitStr = "EEGDFHBAKJINGGJKFAHAFKFIGINJGFMEHIEOAACPBFIDKMLKCMBPCLONHCKGJGKHBEMOLNCGPAMC", mP2pKey = "nVpkO1Xqbojgr4Ks";
    private boolean isSpeaking = false;
    private int isPreviewMute = ICameraP2P.MUTE;
    private int isPlaybackMute = ICameraP2P.MUTE;
    private OnTutkCameraListener.ORDER_DEFINITION mDefinition;
    private ICameraP2P.PLAYMODE playmode = ICameraP2P.PLAYMODE.LIVE;   //0 live, -1 playback;

    protected Map<String, List<String>> mBackDataMonthCache;
    protected Map<String, List<TimePieceBean>> mBackDataDayCache;

        Button enableHDBtn;
        Button startSpeakBtn;

        private boolean mIsRunSoft;
        private int p2pType;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            mVideoView = findViewById(R.id.camera_video_view);
            mIsRunSoft = getIntent().getBooleanExtra("isRunsoft", false);
            p2pId = getIntent().getStringExtra("p2pId");
            p2pWd = getIntent().getStringExtra("p2pWd");
            localKey = getIntent().getStringExtra("localKey");
            p2pType = getIntent().getIntExtra("p2pType", 1);
            mVideoView.createVideoView(p2pType, mIsRunSoft);

            //播放器view最好宽高比设置16:9
            WindowManager windowManager = (WindowManager) this.getSystemService(WINDOW_SERVICE);
            int width = windowManager.getDefaultDisplay().getWidth();
            int height = width * ASPECT_RATIO_WIDTH / ASPECT_RATIO_HEIGHT;
            findViewById(R.id.camera_tutk_video_view_ll).setLayoutParams(new RelativeLayout.LayoutParams(width, height));

            Button connectBtn = findViewById(R.id.btn_connect);
            connectBtn.setOnClickListener(this);
            Button disconnectBtn = findViewById(R.id.btn_disconnect);
            disconnectBtn.setOnClickListener(this);
            Button startPreviewBtn = findViewById(R.id.btn_start_preview);
            startPreviewBtn.setOnClickListener(this);
            Button stopPreviewBtn = findViewById(R.id.btn_stop_preview);
            stopPreviewBtn.setOnClickListener(this);

            startSpeakBtn = findViewById(R.id.btn_start_speak);
            startSpeakBtn.setOnClickListener(this);
            Button snapShotBtn = findViewById(R.id.btn_snapshot);
            snapShotBtn.setOnClickListener(this);
            Button startRecordBtn = findViewById(R.id.btn_start_record);
            startRecordBtn.setOnClickListener(this);
            Button stopRecordBtn = findViewById(R.id.btn_stop_record);
            stopRecordBtn.setOnClickListener(this);

            Button enableMuteBtn = findViewById(R.id.btn_enableMute);
            enableMuteBtn.setOnClickListener(this);
            enableHDBtn = findViewById(R.id.btn_enableHD);
            enableHDBtn.setOnClickListener(this);

            Button connectPlaybackBtn = findViewById(R.id.btn_connect_playback);
            connectPlaybackBtn.setOnClickListener(this);

            Button startPlaybackBtn = findViewById(R.id.btn_start_playback);
            startPlaybackBtn.setOnClickListener(this);

            Button pausePlaybackBtn = findViewById(R.id.btn_pause_playback);
            pausePlaybackBtn.setOnClickListener(this);

            Button resumePlaybackBtn = findViewById(R.id.btn_resume_playback);
            resumePlaybackBtn.setOnClickListener(this);

            Button stopPlaybackBtn = findViewById(R.id.btn_stop_playback);
            stopPlaybackBtn.setOnClickListener(this);

            Button requestMonthPlaybackBtn = findViewById(R.id.btn_request_month_playback);
            requestMonthPlaybackBtn.setOnClickListener(this);
            Button requestDayPlaybackBtn = findViewById(R.id.btn_request_day_playback);
            requestDayPlaybackBtn.setOnClickListener(this);
            initCamera();  //初始化
            mBackDataMonthCache = new HashMap<>();
            mBackDataDayCache = new HashMap<>();

        }

        /**
         * 初始化
         */
        private void initCamera() {
            camera = TuyaSmartCameraFactory.generateTuyaSmartCamera(p2pType);
            camera.createDevice(p2pId, mInitStr, mP2pKey, mIsRunSoft);
            camera.registorOnDelegateCameraListener(this);
            camera.generateCameraView(mVideoView.createdView());
        }


        @Override
        protected void onDestroy() {
            super.onDestroy();
            camera.disconnect();
            TuyaSmartCameraFactory.onDestroyTuyaSmartCamera();
        }


        @Override
        public void onClick(View view) {
            if (R.id.btn_connect == view.getId()) {
//                int crcsum = CRC32.getChecksum("3c93e703fdc2447e".getBytes());
//                String topicId = "m/w/" + "6cf3352d08958d7e65i3u1";
//                byte[] bytes = IntToButeArray.intToByteArray(crcsum);
//                ITuyaHomeCamera homeCamera = TuyaHomeSdk.getCameraInstance();
//                homeCamera.publishWirelessWake(topicId, bytes);
                camera.connect(p2pId, p2pWd, localKey);
            } else if (R.id.btn_disconnect == view.getId()) {
                camera.disconnect();
            } else if (R.id.btn_start_preview == view.getId()) {
                camera.startPreview();
            } else if (R.id.btn_stop_preview == view.getId()) {
                camera.stopPreview();
            } else if (R.id.btn_snapshot == view.getId()) {
                String picPath = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/" + System.currentTimeMillis() + ".png";
                }
                camera.snapshot(picPath, this, ICameraP2P.PLAYMODE.LIVE);
            } else if (R.id.btn_start_record == view.getId()) {
                if (Constants.hasStoragePermission()) {
                    String picPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Camera/";
                    String fileName = System.currentTimeMillis() + ".mp4";
                    camera.startRecordLocalMp4WithoutAudio(picPath, fileName, this);
                } else {
                    Constants.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Constants.EXTERNAL_STORAGE_REQ_CODE, "open_storage");
                }
            } else if (R.id.btn_stop_record == view.getId()) {
                camera.stopRecordLocalMp4();
            } else if (R.id.btn_start_speak == view.getId()) {
                if (isSpeaking) {
                    int ret = camera.stopAudioTalk();
                    if (ret == 0) {
                        isSpeaking = false;
                    }
                } else {
                    if (Constants.hasRecordPermission()) {
                        camera.startAudioTalk();
                    } else {
                        Constants.requestPermission(this, Manifest.permission.RECORD_AUDIO, Constants.EXTERNAL_AUDIO_REQ_CODE, "open_recording");
                    }
                }
            } else if (R.id.btn_enableHD == view.getId()) {
                camera.getVideoClarity();
                camera.setVideoClarity(ICameraP2P.HD);
            } else if (R.id.btn_enableMute == view.getId()) {
                int mute = 0;
                if (ICameraP2P.PLAYMODE.LIVE == playmode) {
                    mute = isPreviewMute == ICameraP2P.MUTE ? ICameraP2P.UNMUTE : ICameraP2P.MUTE;
                } else {
                    mute = isPlaybackMute == ICameraP2P.MUTE ? ICameraP2P.UNMUTE : ICameraP2P.MUTE;
                }
                camera.setMute(playmode, mute, this);
            } else if (R.id.btn_connect_playback == view.getId()) {
                camera.connectPlayback();
            } else if (R.id.btn_request_month_playback == view.getId()) {
                camera.queryRecordDaysByMonth(2018, 12);
            } else if (R.id.btn_request_day_playback == view.getId()) {
                camera.queryRecordTimeSliceByDay(2018, 12, 17);
            } else if (R.id.btn_start_playback == view.getId()) {
                TimePieceBean timePieceBean = mBackDataDayCache.get("20181217").get(0);
                camera.startPlayBack(timePieceBean.getStartTime(), timePieceBean.getEndTime(), timePieceBean.getStartTime());
            } else if (R.id.btn_pause_playback == view.getId()) {
                camera.pausePlayBack();
            } else if (R.id.btn_resume_playback == view.getId()) {
                camera.resumePlayBack();
            } else if (R.id.btn_stop_playback == view.getId()) {
                camera.stopPlayBack();
            }

        }

        @Override
        public void onSnapshotSuccessCallback() {
            Log.d(TAG, "onSnapshotSuccessCallback");
        }

        @Override
        public void onSnapshotFailCallback() {
            Log.d(TAG, "onSnapshotFailCallback");
        }

        @Override
        public void onCreateDeviceSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "onCreateDeviceSuccess", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "onCreateDeviceSuccess");
        }

        @Override
        public void onCreateDeviceFail(int ret) {
            Log.d(TAG, "onCreateDeviceFail ret" + ret);
        }


        @Override
        public void onStopPreviewSuccess() {

        }

        @Override
        public void onStopPreviewFail() {

        }


        @Override
        public void connectFail(final String errorCode, String errorMsg) {
            Log.d(TAG, "connectFail errorCode" + errorCode);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "connectFail " + errorCode, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onChannel0StartSuccess() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "onChannel0StartSuccess", Toast.LENGTH_SHORT).show();
                }
            });
            Log.d(TAG, "onChannel0StartSuccess");
        }

        @Override
        public void onChannel1StartSuccess() {
            Log.d(TAG, "onChannel1StartSuccess");
        }

        @Override
        public void onChannelOtherStatus(int errorCode) {

            Log.d(TAG, "onChannelOtherStatus errorCode " + errorCode);
        }

        @Override
        public void onPreviewSuccess() {
            Log.d(TAG, "onPreviewSuccess");
        }

        @Override
        public void onPreviewFail(int errorCode) {
            Log.d(TAG, "onPreviewFail errorCode" + errorCode);
        }


        @Override
        public void onPlaybackEnterFail(String errorCode, String errorMsg) {

        }

        @Override
        public void onPlaybackStartSuccess() {

        }

        @Override
        public void onPlaybackStartFail(String errorCode, String errorMsg) {

        }

        @Override
        public void onPlaybackPauseSuccess() {

        }

        @Override
        public void onPlaybackPauseFail(String errorCode, String errorMsg) {

        }

        @Override
        public void onPlaybackResumeSuccess() {

        }

        @Override
        public void onPlaybackResumeFail(String errorCode, String errorMsg) {

        }

        @Override
        public void onDefinitionStatusCallback(boolean isQuery, int definition) {
            Log.d(TAG, "onDefinitionStatusCallback definition" + definition);
        }

        @Override
        public void onPlaybackEnd() {

        }

        @Override
        public void onPlaybackEndFail() {

        }

        @Override
        public void onMuteOperateSuccess(ICameraP2P.PLAYMODE playmode, int isMute) {
            isPreviewMute = isMute;
            Log.d(TAG, "onPreviewMuteOperateSuccess mute" + isPreviewMute);
        }

        @Override
        public void onMuteOperateFail(ICameraP2P.PLAYMODE playmode) {

        }


        @Override
        public void onQueryPlaybackDataSuccessByMonth(int year, int month, Object days) {
            Log.d(TAG, "onQueryPlaybackDataSuccessByMonth days" + days);
        }

        @Override
        public void onQueryPlaybackDataFailureByMonth(int errorCode, String errorMsg) {
            Log.d(TAG, "onQueryPlaybackDataFailureByMonth errorCode " + errorCode);
        }

        @Override
        public void onQueryPlaybackDataSuccessByDay(String yearmonthday, Object timePieceBeanList) {
            Log.d(TAG, "onQueryPlaybackDataSuccessByDay yearmonthday " + yearmonthday);
            mBackDataDayCache.put(yearmonthday, (List<TimePieceBean>) timePieceBeanList);
        }

        @Override
        public void onQueryPlaybackDataFailureByDay(int errorCode, String errorMsg) {
            Log.d(TAG, "onQueryPlaybackDataFailureByDay errorCode " + errorCode);
        }


        @Override
        public void onreceiveFrameDataCallback() {

        }

        @Override
        public void onSpeakSuccessCallback() {
            isSpeaking = true;
        }

        @Override
        public void onSpeakFailueCallback(int errorCode) {

        }

        @Override
        public void onStopSpeakSuccessCallback() {

        }

        @Override
        public void onStopSpeakFailueCallback(int errorCode) {

        }

        @Override
        public void receiveFrameDataForMediaCodec(int avChannel, byte[] buf, int length, int pFrmNo, byte[] pFrmInfoBuf, boolean isIframe, int codecId) {

        }

        @Override
        public void onReceiveFrameYUVData(int sessionId, ByteBuffer y, ByteBuffer u, ByteBuffer v, int width, int height, long timestamp, Object camera) {
            mVideoView.receiveFrameYUVData(sessionId, y, u, v, width, height, camera);
        }

        @Override
        public void onSessionStatusChanged(Object camera, int sessionId, int sessionStatus) {
            Log.d(TAG, "sessionId " + sessionId + "sessionStatus " + sessionStatus);
        }
    }
