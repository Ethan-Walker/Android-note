# ���߳�
## 1. �����̵߳Ļ�������
1. �̳�Thread,��д run ����

```java
class MyThread extends Thread{
	@Override
	public void run(){

	}
}

������
new MyThread().start();

```

2. ʵ��Runnable�ӿ�, ��д run����

```java
class MyThread implements Runnable{
	@Override
	public void run(){

	}
}
new Thread(MyThread).start();

```

Ҳ���������ڲ�����ʵ��:

```java
new Thread(new Runnable(){
	@Override
	public void run(){

	}
}).start();
```

## 2. ���߳��������̼߳��ͨ��

> ���߳����ǲ���ֱ�Ӹ���UI������ݵ�, ֻ�ܼ�Ӹ���

1. �л������߳�, ����runOnUiThread

```java
 runOnUiThread(new Runnable() {
    @Override
    public void run() {
        textView.setText(text);
    }
});
```

2. �����߳��л�ȡ�����̵߳�Handlerʵ��, ��ͨ����ʵ�������̷߳�����Ϣ

**MainActivity.java**

```java
 // �������̵߳� Handler ����
 Handler mainHandler = new Handler(){
	@Override
	public void handleMessage(Message msg) {
	    if(msg.what == 0x000){
	        Log.e(TAG, "handleMessage:" );
	        String data = msg.getData().getString("responseData");
	        textView.setText(data);
	    }
	}
};
```

**MyHandler.java**

```java
public class MyHandler extends Thread {

	@Override
    public void run() {
        Looper.prepare();  // ��ʼ�� MessageQueue��Looper����
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch(msg.what){
                    case 0x100:
                        Log.e(TAG, "handleMessage: " );
                        sendReq(msg.getData().getString("url"));
                        break;
                    default:
                        break;
                }
            }
        };
        Looper.loop();    // ������ѭ����MessageQueue��ȡ��Ϣ
    }

    private void sendToMainThread(String responseData) {
        Message message = new Message();
        message.what = 0x000;
        Bundle bundle = new Bundle();
        bundle.putString("responseData",responseData);
        message.setData(bundle);
        MainActivity2.mainHandler.sendMessage(message);
    }

}
```

**ͬ��, ���߳�Ҳ���������߳��з�����Ϣ**

## 3. �첽���� AsyncTask
1. ��������̳� AsyncTask<params,progress,result>
  - params:  ����ʵ������execute(params) ����ʱ����Ĳ���,���������� doInBackground(Object[] params) ��
  - progress:  ��ִ̨������ʱ�������ʾ���ȣ�����������ָ��������Ϊ������ʾ���ͣ�������: Integer
  - result: ����ֵ�����ͣ���doInBackground()��������ֵ�����ͣ����ص�ֵ������doPostExecute(result)�Ĳ�����
2. ��д��������
  - void  onPreExecute(): ��ִ���첽����ǰ���ø÷���, һ�����ڳ�ʼ���������ȵ�.
  - Result doInBackground(Object[] params) : �÷����ڵ����в����������߳���ִ��, ����ֱ�Ӵ������ʾUI, ������ִ�н������غ�,�߳̽���, һ������ִ���첽����
  - void onPostExecute(Result result): ����ķ������غ󣬽������÷����Ĳ���,�÷��������߳���ִ��
  - void onProgressUpdate(Progress... values): ��doInBackground()�е���publishProgress(progress)�ͻ�ص��÷���, ����ʵʱ��ʾ�������
  - һ�㹹�췽���ᴫ����Context ����

3. ��������ʵ��,����execute����

```java
	MyAsyncTask task = new MyAsyncTask(this);
	task.execute(params);
```
**ע�⣺ ����doInBackground���������߳���ִ�У����������������߳���ִ�У��ʿ���ֱ�Ӳ���UI���**

