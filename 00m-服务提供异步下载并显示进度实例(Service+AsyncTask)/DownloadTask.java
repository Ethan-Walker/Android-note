package com.example.ethanwalker.bestparctice;

import android.media.audiofx.LoudnessEnhancer;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by EthanWalker on 2017/5/27.
 */

public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    private static final String TAG = "DownloadTask";
    private static final int TYPE_SUCCESS = 0;
    private static final int TYPE_FAILED = 1;
    private static final int TYPE_PAUSED = 2;
    private static final int TYPE_CANCELED = 3;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int  lastProgress = 0;
    private long downloadedLength = 0;
    private File file;
    private DownloadListener listener;

    public DownloadTask(DownloadListener listener) {
        this.listener = listener;
    }

    // execute(params)
    @Override
    protected Integer doInBackground(String... params) {
        // 下载网页内容，返回下载结果（成功？失败？暂停）

        // 1. 找到保存文件位置，获取文件算出已下载的文件长度

        String url = params[0];
        String fileName = url.substring(url.lastIndexOf("/")); // 文件名为 /index.html
        String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        // 拼接文件路径
        file = new File(directory + fileName);

        if (file.exists()) {
            Log.e(TAG, "文件已存在!" );
            downloadedLength = file.length();
 /*           file.delete();
            return 6;*/
        }
        // 2. 提前发起HTTP请求，获取到内容长度
        long contentLength = getContentLength(params[0]);

        // 3. 比较已下载长度和内容总长度
        if (contentLength == 0) { // ContentLength 为返回的 字节数 Byte
            return TYPE_FAILED;
        } else if (downloadedLength >= contentLength) {
            return TYPE_SUCCESS;
        }
        // 4. 发起HTTP请求，从未下载处(断点处)开始读取
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + downloadedLength + "-")   // 从某个字节处开始下载
                .url(url)
                .build();
        Response response = null;
        InputStream in = null;
        RandomAccessFile accessFile = null;

        try {
            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(downloadedLength); // 从上次中断(已经写好)的位置继续写，如果没有这句，则从文件头开始覆盖下载

            response = client.newCall(request).execute();
            if (response != null) {
                in = response.body().byteStream();
                byte[] bys = new byte[1024];
                int len = 0;

                while ((len = in.read(bys)) != -1) {

                    if (isCanceled) {
                        Log.e(TAG, "isCanceled");
                    //return 语句返回之前会执行finally语句，return执行完，此线程终止
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        Log.e(TAG, "isPaused");
                        return TYPE_PAUSED;
                    }
                    Log.e(TAG, "doInBackground: ");
                    accessFile.write(bys,0,len);   //RandomAccessFile
                    int progress = (int) ((downloadedLength += len) * 100 / contentLength);
                    publishProgress(progress);
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        } catch (IOException e) {
            Log.e(TAG, "出现异常" );
            e.printStackTrace();
        } finally {
            Log.e(TAG, "finally 被执行");
            try {

                if (in != null) {
                    in.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
                if (isCanceled && file != null) {
                    file.delete();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    private long getContentLength(String str) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(str)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body().contentLength();    // 返回的是字节数
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];
        Log.e("downloadProgress:", progress + "");
        if (progress > lastProgress) {
            listener.onProgress(progress);
            lastProgress = progress;
        }
        Log.e("lastProgress=>",lastProgress+"");
    }

    @Override
    protected void onPostExecute(Integer integer) {
        switch (integer) {
            case TYPE_CANCELED:
                lastProgress = 0;
                listener.onCanceled();
                break;
            case TYPE_FAILED:
                lastProgress = 0;
                listener.onFailed();
                break;
            case TYPE_PAUSED:
                listener.onPaused(lastProgress);
                break;
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public void pausedDowoload() {
        isPaused = true;

    }

    public void cancelDownload() {
        Log.e(TAG, "cancelDownload: " );
        isCanceled = true;
       
    }
}
