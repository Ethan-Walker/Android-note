package com.example.ethanwalker.service;

/**
 * Created by EthanWalker on 2017/6/10.
 */

public interface DownloadListener {

    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused(int progress);

    void onCanceled();
}
