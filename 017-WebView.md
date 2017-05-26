1. 添加权限		<uses-permission android:name="android.permission.INTERNET" />
2.

```xml
<WebView
    android:id="@+id/web_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

3.

```java
        WebView webView  = (WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());   // 作用：当从一个网页跳转到另一个网页时，仍然使用webview，而不是使用系统浏览器
        webView.loadUrl("http://www.baidu.com");
```
