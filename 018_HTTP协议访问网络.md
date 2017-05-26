1. HttpURLConnection

```java
public void sendRequest() {

        //������ҳ����ʱ�䣬����һ���߳�
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
         			 * Post �ύ
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

    // ���߳��в�����UI����,runOnUiThread �л������߳�
    public void display(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayContent.setText(str);
            }
        });
    }

2. OkHttp ����Դ�����
- ������� �� compile 'com.squareup.okhttp3:okhttp:3.8.0'

```java
public void sendReq() {
        new Thread() {
            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();
                // �����post�ύ����Ҫ��requestBody ����get������Ҫ
                RequestBody requestBody = new FormBody.Builder()
                        .add("username", "Shit802")
                        .add("password", "nsdfmywmxesy@")
                        .build();

                Request request = new Request.Builder()
                        .url("https://www.baidu.com")
                        .post(requestBody)          // get�ύ����Ҫ�÷�����Ĭ����get�ύ
                        .build();

                Response response = null;
                String responseData;
                try {
                    // newCall() ���� Call����, execute����Response����
                    response = client.newCall(request).execute();
                    responseData = response.body().string();  // ע�ⲻ��toString();
                    display(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
```
