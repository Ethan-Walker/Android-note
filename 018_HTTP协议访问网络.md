1. HttpURLConnection

```java
public void sendRequest() {

        //加载网页消耗时间，开启一个线程
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                StringBuilder builder = new StringBuilder();
                InputStream in = null;
                try {
                    URL url = new URL("https://www.baidu.com");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

         			/**
         			 * Post 提交
				     * connection.setRequestMethod("POST");
                     * DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                     * out.writeBytes("username=Shit802&password=nsdfmywmxesy@");
                    */
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(8000);

                    in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    String lastModified = connection.getHeaderField("Last-Modified");
                    long time = connection.getIfModifiedSince();
                    Date modifiedSince = new Date(time);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (builder != null) {
                    display(builder.toString());
                }
            }
        }.start();

    }

    // 子线程中不允许UI操作,runOnUiThread 切换到主线程
    public void display(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayContent.setText(str);
            }
        });
    }

2. OkHttp （开源组件）
添加依赖 ： compile 'com.squareup.okhttp3:okhttp:3.8.0'

> 基于同步请求：由于网络请求耗时，必须手动开启一个线程执行网络请求，否则会产生ANR问题

```java
Request request = new Request.Builder().url("").build();
Respnse response = okHttpClient.newCall(request).execute();
```

> 基于异步请求： okhttp3 提供了 Call.enqueue(okhttp3.Callback callback) 方法，请求对象放入请求队列中，在内部实现了子线程访问请求

```java
Request request = new Request.Builder().url("").build();
okHttpClient.newCall(request).enqueue(callback);
```

调用时，传入一个 okhttp3.Callback 实现类对象，**注意，重写的 onResponse()/onFailure()方法都在子线程中，不能处理UI组件**
例如：

```java
HttpUtil.java

    public static void sendOkHttpReq(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);           // 如果请求成功返回结果，将回调callback中重写的 onResponse 方法
    }

MainActivity.java
    sendOkHttpReq("",new okhttp3.Callback(){
        void onResponse(Call call, Response response){

        }
        void onFailure(... ){

        }
    });

 ```


```java
public void sendReq() {
        new Thread() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                // 如果是post提交，需要有requestBody 对象，get请求不需要
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", "Shit802")
                        .add("password", "nsdfmywmxesy@")
                        .build();

                Request request = new Request.Builder()
                        .url("https://www.baidu.com")
                        .post(requestBody)          // get提交不需要该方法，默认是get提交
                        .build();

                Response response = null;
                String responseData;
                try {
                    // newCall() 创建 Call对象, execute返回Response对象
                    response = client.newCall(request).execute();
                    responseData = response.body().string();  // 注意不是toString();
                    display(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
```

