package com.example.ethanwalker.bestparctice;

/**
 * Created by EthanWalker on 2017/5/27.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused(int progress);

    void onCanceled();
}
