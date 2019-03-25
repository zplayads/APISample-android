package com.zplay.playable.vastdemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zplay.playable.vastdemo.bean.InLineBean;
import com.zplay.playable.vastdemo.bean.MediaFilesBean;
import com.zplay.playable.vastdemo.bean.TrackingEventsBean;
import com.zplay.playable.vastdemo.bean.VAST;
import com.zplay.playable.vastdemo.bean.VideoClicksBean;
import com.zplay.playable.vastdemo.report.VastEventReport;
import com.zplay.playable.vastdemo.utils.BeanHelper;
import com.zplay.playable.vastdemo.utils.ResFactory;
import com.zplay.playable.vastdemo.utils.WindowSizeUtils;

import java.io.File;
import java.util.List;

public class VastVideoFullScreenActivity extends Activity {
    private static final String TAG = "VastVideoActivity";

    private VoiceMode voiceMode;
    private MediaPlayer player;

    private int vWidth;
    private int vHeight;
    private boolean isResume;
    private boolean isPlayerPause;
    private boolean isReplay;
    private boolean isComplete;

    private FrameLayout mediaPlayer;
    private FrameLayout fatherContainer;
    private TextView countdownTextView;
    private ImageView voiceImageView;
    private ImageView downloadImageView;

    private boolean countdownBreakFlag;

    private VAST vast;
    private String mediaPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fatherContainer = new FrameLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        addContentView(fatherContainer, params);

        Intent intent = this.getIntent();
        vast = (VAST) intent.getSerializableExtra("vast");
        mediaPath = intent.getStringExtra("path");

        initMedia();

    }

    public void initMedia() {
        if (vast == null || mediaPath == null || mediaPath.length() == 0) {
            Log.d(TAG, "视频还没有加载好");
            return;
        }
        try {
            File file = new File(mediaPath);
            if (!file.exists()) {
                Log.d(TAG, "视频文件丢失了");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FrameLayout browser = createBrowser(mediaPath);
        mediaPlayer = createMediaPlayer();

        FrameLayout.LayoutParams params_b = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fatherContainer.addView(browser, params_b);

        FrameLayout.LayoutParams params_m = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        fatherContainer.addView(mediaPlayer, params_m);

        //应对部分机型下出现有声音无图像问题
        new Handler().postDelayed(new Runnable() {
            public void run() {
                playSetPathAndPrepareAsync(mediaPath);
            }
        }, 300);
    }

    private void playSetPathAndPrepareAsync(String mediaPath) {
        try {
            player.setDataSource(mediaPath);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "playSetPathAndPrepareAsync error" + e);
        }
    }

    @SuppressLint("RtlHardcoded")
    @SuppressWarnings("deprecation")
    private FrameLayout createBrowser(final String mediaPath) {
        FrameLayout browserFather = new FrameLayout(VastVideoFullScreenActivity.this);

        //关闭落地页按钮
        ImageView iv_cancel = new ImageView(VastVideoFullScreenActivity.this);
        Drawable zplayad_media_close = ResFactory.getDrawableByAssets("zplayad_btn_close", VastVideoFullScreenActivity.this);
        iv_cancel.setBackgroundDrawable(zplayad_media_close);

        iv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMedia();
                countdownBreakFlag = true;
            }
        });

        FrameLayout.LayoutParams param_cancel = new FrameLayout.LayoutParams(WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35),
                WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35));
        param_cancel.gravity = Gravity.TOP | Gravity.RIGHT;
        param_cancel.rightMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        param_cancel.topMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        browserFather.addView(iv_cancel, param_cancel);


        //重播按钮
        ImageView iv_replay = new ImageView(VastVideoFullScreenActivity.this);
        Drawable zplayad_btn_replay = ResFactory.getDrawableByAssets("zplayad_media_replay", VastVideoFullScreenActivity.this);
        iv_replay.setBackgroundDrawable(zplayad_btn_replay);

        iv_replay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer = createMediaPlayer();
                FrameLayout.LayoutParams params_m = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                fatherContainer.addView(mediaPlayer, params_m);
//                  player.start();
                startCountdown();
                playSetPathAndPrepareAsync(mediaPath);
                onMediaRePlay();
                if (downloadImageView != null) {
                    VastVideoFullScreenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            downloadImageView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        FrameLayout.LayoutParams param_replay = new FrameLayout.LayoutParams(WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35),
                WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35));
        param_replay.gravity = Gravity.TOP | Gravity.LEFT;
        param_replay.leftMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        param_replay.topMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        browserFather.addView(iv_replay, param_replay);
        return browserFather;
    }

    @SuppressLint("RtlHardcoded")
    @SuppressWarnings("deprecation")
    private FrameLayout createMediaPlayer() {
        final SurfaceView surfaceView = new SurfaceView(VastVideoFullScreenActivity.this);
        final Display currDisplay = VastVideoFullScreenActivity.this.getWindowManager().getDefaultDisplay();
        SurfaceHolder holder = surfaceView.getHolder();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        FrameLayout playerFather = new FrameLayout(VastVideoFullScreenActivity.this);
        playerFather.setBackgroundColor(0xff000000);
        playerFather.setClickable(true);

        //父控件
        FrameLayout surFather = new FrameLayout(VastVideoFullScreenActivity.this);
        FrameLayout.LayoutParams param_sf = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        param_sf.gravity = Gravity.CENTER;
        playerFather.addView(surFather, param_sf);
        surFather.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handleUIClick();
            }
        });

        // 视频本身
        surFather.addView(surfaceView);

        // UI控件

        // 倒计时文字
        countdownTextView = new TextView(VastVideoFullScreenActivity.this);
        countdownTextView.setTextSize(14);
        countdownTextView.setTextColor(0xffffffff);
        Drawable zplayad_media_countdownbg = ResFactory.getDrawableByAssets("zplayad_media_countdownbg", VastVideoFullScreenActivity.this);
        countdownTextView.setBackgroundDrawable(zplayad_media_countdownbg);
        countdownTextView.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams param_countdown = new FrameLayout.LayoutParams(
                WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 30), WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 30));
        param_countdown.gravity = Gravity.BOTTOM | Gravity.LEFT;
        param_countdown.bottomMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 4);
        param_countdown.leftMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 4);
        surFather.addView(countdownTextView, param_countdown);

        // 静音键按钮
        voiceImageView = new ImageView(VastVideoFullScreenActivity.this);
        voiceMode = VoiceMode.MODE_VOICE;
        Drawable zplayad_media_novoice = ResFactory.getDrawableByAssets("zplayad_media_voice", VastVideoFullScreenActivity.this);
        voiceImageView.setBackgroundDrawable(zplayad_media_novoice);
        voiceImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (voiceMode == VoiceMode.MODE_VOICE) {
                    player.setVolume(0, 0);
                    voiceImageView.setBackgroundDrawable(ResFactory.getDrawableByAssets("zplayad_media_novoice", VastVideoFullScreenActivity.this));
                    voiceMode = VoiceMode.MODE_NO_VOICE;
                } else {
                    player.setVolume(1f, 1f);
                    voiceImageView.setBackgroundDrawable(ResFactory.getDrawableByAssets("zplayad_media_voice", VastVideoFullScreenActivity.this));
                    voiceMode = VoiceMode.MODE_VOICE;
                }
            }
        });
        FrameLayout.LayoutParams param_voice = new FrameLayout.LayoutParams(
                WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 30), WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 30));
        param_voice.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        param_voice.bottomMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 4);
        param_voice.rightMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 4);
        surFather.addView(voiceImageView, param_voice);

        //关闭按钮
        ImageView iv_cancel = new ImageView(VastVideoFullScreenActivity.this);
        Drawable zplayad_media_close = ResFactory.getDrawableByAssets("zplayad_btn_close", VastVideoFullScreenActivity.this);
        iv_cancel.setBackgroundDrawable(zplayad_media_close);
        iv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMedia();
                countdownBreakFlag = true;
            }
        });

        FrameLayout.LayoutParams param_cancel = new FrameLayout.LayoutParams(WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35),
                WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 35));
        param_cancel.gravity = Gravity.TOP | Gravity.RIGHT;
        param_cancel.rightMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        param_cancel.topMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 6);
        surFather.addView(iv_cancel, param_cancel);
        iv_cancel.setVisibility(View.VISIBLE);
        //下载按钮
        downloadImageView = new ImageView(VastVideoFullScreenActivity.this);
        Drawable zplayad_media_download = ResFactory.getDrawableByAssets("zplayad_media_displayDown", VastVideoFullScreenActivity.this);
        downloadImageView.setBackgroundDrawable(zplayad_media_download);
        downloadImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onDisplayingMediaPageClick();
                handleClickAction();
            }
        });
        FrameLayout.LayoutParams param_download = new FrameLayout.LayoutParams(WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 88), WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this,
                30));
        param_download.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        param_download.bottomMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 4);
        param_download.rightMargin = WindowSizeUtils.dip2px(VastVideoFullScreenActivity.this, 54);
        surFather.addView(downloadImageView, param_download);
        downloadImageView.setVisibility(View.VISIBLE);


        holder.addCallback(new Callback() {
            @Override
            public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
                // 当Surface尺寸等参数改变时触发
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                // 在这里我们指定MediaPlayer在当前的Surface中进行播放
                player.setDisplay(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.d(TAG, "surfaceDestroyed Surface销毁");
            }
        });
        // 为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 下面开始实例化MediaPlayer对象

        player.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer player) {
                // 当MediaPlayer播放完成后触发
                if (isResume) {
                    Log.i(TAG, "播放完成");
                    if (!isReplay) {
                        isComplete = true;
                        onMediaPlayComplete();
                    }
                    isReplay = true;
                    countdownBreakFlag = false;
                    countdownTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        player.setOnErrorListener(new OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.i(TAG, "Play Error:::onError called");
                switch (what) {
                    case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                        Log.i(TAG, "Play Error:::MEDIA_ERROR_SERVER_DIED");
                        break;
                    case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                        Log.i(TAG, "Play Error:::MEDIA_ERROR_UNKNOWN");
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        player.setOnInfoListener(new OnInfoListener() {

            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                // 当一些特定信息出现或者警告时触发
                switch (what) {
                    case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                        break;
                    case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                        break;
                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                        break;
                    case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                        break;
                }
                return false;
            }
        });
        player.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                // 当prepare完成后，该方法触发，在这里我们播放视频
                int duration = player.getDuration();

                // 首先取得video的宽和高
                vWidth = player.getVideoWidth();
                vHeight = player.getVideoHeight();

                // 根据视频大小与屏幕大小的差异进行等比缩放
                float wRatio = (float) vWidth / (float) currDisplay.getWidth();
                float hRatio = (float) vHeight / (float) currDisplay.getHeight();

                // 选择大的一个进行缩放
                float ratio = Math.max(wRatio, hRatio);

                vWidth = (int) Math.ceil((float) vWidth / ratio);
                vHeight = (int) Math.ceil((float) vHeight / ratio);

                // 设置surfaceView的布局参数
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(vWidth, vHeight);
                params.gravity = Gravity.CENTER;
                surfaceView.setLayoutParams(params);

                // 然后开始播放视频
                player.start();
                onMediaPlay();
                showUI5s();
                startCountdown();
            }
        });
        player.setOnSeekCompleteListener(new OnSeekCompleteListener() {

            @Override
            public void onSeekComplete(MediaPlayer mp) {
                // seek操作完成时触发
                Log.i(TAG, "Seek Completion onSeekComplete called");
            }
        });
        player.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

            @Override
            public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                // 当video大小改变时触发
                // 这个方法在设置player的source后至少触发一次
                Log.i(TAG, "Video Size Change onVideoSizeChanged called");
            }
        });
        return playerFather;
    }

    void handleUIClick() {
        if (voiceImageView.getVisibility() == View.VISIBLE) {
            hideUI();
        } else {
            showUI5s();
        }
    }

    void hideUI() {
        voiceImageView.setVisibility(View.GONE);
    }

    void showUI5s() {
        voiceImageView.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                long uiFlag = System.currentTimeMillis();
                long thisThread = uiFlag;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (thisThread == uiFlag) {
                    Log.i(TAG, "执行自动隐藏");
                    VastVideoFullScreenActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            voiceImageView.setVisibility(View.GONE);
                        }
                    });
                } else {
                    Log.i(TAG, "本次自动隐藏失效");
                }
            }
        }).start();
    }

    private void closeMedia() {
        onMediaDismiss();
        this.finish();
    }

    private void startCountdown() {
        countdownBreakFlag = false;
        final int evDispTime = player.getDuration() / 1000;
        countdownTextView.setText(String.format("%ss", String.valueOf(evDispTime)));
        new Thread(new Runnable() {
            int showtime;

            @Override
            public void run() {
                showtime = evDispTime;
                while (showtime > 0) {
                    if (countdownBreakFlag) {
                        break;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (isResume) {
                        showtime -= 1;
                        VastVideoFullScreenActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (countdownTextView != null) {
                                    countdownTextView.setText(String.format("%ss", String.valueOf(showtime)));
                                }
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public void resume() {
        isResume = true;
        if (player != null && isPlayerPause) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    try {
                        mediaPlayer.bringToFront();
                        isPlayerPause = false;
                        player.start();
                        reportVideoResume(player.getCurrentPosition());
                    } catch (Exception e) {
                        Log.i(TAG, "mediaPlayer start: " + e);
                    }
                }
            }, 300);
        }
    }

    public void pause() {
        isResume = false;
        try {
            if (player != null && player.isPlaying()) {
                player.pause();
                isPlayerPause = true;
                reportVideoPause(player.getCurrentPosition());
            }
        } catch (Exception e) {
            Log.e(TAG, "mediaPlayer pause: " + e);
        }
    }

    @Override
    protected void onResume() {
        if (vast != null) {
            MediaFilesBean.MediaFile mediaFile = BeanHelper.getMediaFiles(vast).getMediaFiles().get(0);
            int width = Integer.valueOf(mediaFile.getWidth());
            int height = Integer.valueOf(mediaFile.getHeight());
            if (width > height) {
                //设置为横屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            } else if (width < height) {
                //设置为竖屏
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            }
        }
        super.onResume();
        resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pause();
    }

    //*****************************************上报*****************************************//
    private void onMediaRePlay() {

    }

    private void reportVideoPause(int currentPlayedPosition) {
        List<TrackingEventsBean.Tracking> trackList = BeanHelper.getTrackingEvents(vast).getTrackingList();
        for (TrackingEventsBean.Tracking tracking : trackList) {
            if (tracking.getEvent().equals("pause")) {
                Log.i(TAG, "TrackingEvent pause url:" + tracking.getUrl());
                eventReport(tracking.getUrl());
            }
        }
    }

    private void reportVideoResume(int currentPlayedPosition) {
        List<TrackingEventsBean.Tracking> trackList = BeanHelper.getTrackingEvents(vast).getTrackingList();
        for (TrackingEventsBean.Tracking tracking : trackList) {
            if (tracking.getEvent().equals("resume")) {
                Log.i(TAG, "TrackingEvent resume url:" + tracking.getUrl());
                eventReport(tracking.getUrl());
            }
        }
    }

    private void onMediaPlayComplete() {
        List<TrackingEventsBean.Tracking> trackList = BeanHelper.getTrackingEvents(vast).getTrackingList();
        for (TrackingEventsBean.Tracking tracking : trackList) {
            if (tracking.getEvent().equals("complete")) {
                Log.i(TAG, "TrackingEvent complete url:" + tracking.getUrl());
                eventReport(tracking.getUrl());
            }
        }
    }

    private void onMediaPlay() {
        InLineBean inLineBean = BeanHelper.getInLine(vast);
        String impression = inLineBean.getImpression();
        eventReport(impression);

        List<TrackingEventsBean.Tracking> trackList = BeanHelper.getTrackingEvents(vast).getTrackingList();
        for (TrackingEventsBean.Tracking tracking : trackList) {
            if (tracking.getEvent().equals("start")) {
                Log.i(TAG, "TrackingEvent start url:" + tracking.getUrl());
                eventReport(tracking.getUrl());
            }
        }
    }

    private void onMediaDismiss() {
        if (player != null) {
            player.release();
        }

        List<TrackingEventsBean.Tracking> trackList = BeanHelper.getTrackingEvents(vast).getTrackingList();

        if (!isComplete) {
            for (TrackingEventsBean.Tracking tracking : trackList) {
                if (tracking.getEvent().equals("skip")) {
                    Log.i(TAG, "TrackingEvent skip url:" + tracking.getUrl());
                    eventReport(tracking.getUrl());
                }
            }
        }

        for (TrackingEventsBean.Tracking tracking : trackList) {
            if (tracking.getEvent().equals("closeLinear")) {
                Log.i(TAG, "TrackingEvent closeLinear url:" + tracking.getUrl());
                eventReport(tracking.getUrl());
            }
        }
    }

    /**
     * 播放视频中点击下载上报
     **/
    private void onDisplayingMediaPageClick() {
        Log.i(TAG, "aaa onDisplayingMediaPageClick");
        List<VideoClicksBean.ClickTracking> clickTrackings = BeanHelper.getVideoClicks(vast).getClickTrackings();

        for (VideoClicksBean.ClickTracking tracking : clickTrackings) {
            Log.i(TAG, "TrackingEvent click tracking url:" + tracking.getUrl());
            eventReport(tracking.getUrl());
        }
    }

    private void handleClickAction() {
        VideoClicksBean videoClicksBean = BeanHelper.getVideoClicks(vast);
        String clickUrl = videoClicksBean.getClickThrough();
        Log.i(TAG, "跳转url ：" + clickUrl);
        Uri uri = Uri.parse(clickUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        PackageManager manager = getPackageManager();
        List<ResolveInfo> activities = manager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        Log.i(TAG, "总共有" + activities.size() + "个打浏览器可以打开 ");
        try {
            startActivity(intent);
        } catch (Exception e) {
            Log.i(TAG, "无法找到系统浏览器，使用内置浏览器");
        }
    }


    private enum VoiceMode {
        MODE_VOICE, MODE_NO_VOICE
    }

    @Override
    public void onBackPressed() {
    }

    public final void eventReport(String url) {
        if (url != null && url.length() > 0) {
            new VastEventReport().execute(url);
        } else {
            Log.i(TAG, "event Report list is null");
        }
    }
}
