package com.zplay.playable.vastdemo.file;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MediaDownload extends AsyncTask<String, Void, String> {
    private static String TAG = "MediaDownload";
    private Activity activity;

    public MediaDownload(Activity activity) {
        MediaDownload.this.activity = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        try {
            String dir = getSDRootDIR(activity) + "/.apisample/vast/media/";
            File file_dir = new File(dir);
            if (!file_dir.exists()) {
                file_dir.mkdirs();
            }
            final String ext = getFileExtensionName(strings[0]);
            final String fileName = System.currentTimeMillis() + "." + ext;
            final String filePath = dir + "/" + fileName;
            boolean success = downloadFile(strings[0], filePath);
            if (success) {
                return filePath;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "downloadMedia error : " + e);
            return null;
        }
    }

    private boolean downloadFile(String url, String path) {
        FileOutputStream fos = null;
        try {
            Uri uri = Uri.parse(url);
            String scheme = uri.getScheme();

            URL u = new URL(url);

            File parentFile = new File(path).getParentFile();
            if (!parentFile.exists()) {
                boolean mkdirs = parentFile.mkdirs();
                if (!mkdirs) {
                    return false;
                }
            }
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(1000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            fos = new FileOutputStream(path);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            return true;

        } catch (Exception e) {
            Log.e(TAG, "downloadFile error : " + e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage() + e);
                }
            }
        }
        return false;
    }

    /**
     * 获取sd卡的根目录
     *
     * @return
     */
    public final static String getSDRootDIR(Context context) {
        String cachePath = "";
        try {
            if (context != null) {
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                    cachePath = context.getExternalCacheDir().getPath();
                } else {
                    cachePath = context.getCacheDir().getPath();
                }
            }
        } catch (Exception e) {
            Log.e("YumiMobi", "getSDRootDIR error", e);
        }
        return cachePath;
    }

    public static String getFileExtensionName(String file) {
        int i = file.lastIndexOf('.');
        int leg = file.length();
        return (i > 0 ? (i + 1) == leg ? " " : file.substring(i + 1,
                file.length()) : " ");
    }


    public interface DownloadListener {
        void onProgressChange(int pro);

        void onDownloadComplete(String dataPath);
    }
}
