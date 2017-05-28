# 多线程
## 1. 创建线程的基本方法
1. 继承Thread,重写 run 方法

```java
class MyThread extends Thread{
	@Override
	public void run(){

	}
}

启动：
new MyThread().start();

```

2. 实现Runnable接口, 重写 run方法

```java
class MyThread implements Runnable{
	@Override
	public void run(){

	}
}
new Thread(MyThread).start();

```

也可用匿名内部类来实现:

```java
new Thread(new Runnable(){
	@Override
	public void run(){

	}
}).start();
```

## 2. 子线程中与主线程间的通信

> 子线程中是不能直接更新UI组件内容的, 只能间接更新

1. 切换到主线程, 调用runOnUiThread

```java
 runOnUiThread(new Runnable() {
    @Override
    public void run() {
        textView.setText(text);
    }
});
```

2. 在子线程中获取到主线程的Handler实例, 再通过该实例向主线程发送消息

**MainActivity.java**

```java
 // 创建主线程的 Handler 对象
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
        Looper.prepare();  // 初始化 MessageQueue、Looper对象
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
        Looper.loop();    // 启动，循环向MessageQueue中取消息
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

**同理, 主线程也可以向子线程中发送消息**

## 3. 异步处理 AsyncTask
1. 创建子类继承 AsyncTask<params,progress,result>
  - params:  子类实例调用execute(params) 方法时传入的参数,参数将传到 doInBackground(Object[] params) 中
  - progress:  后台执行任务时，如果显示进度，可以在这里指定泛型作为进度显示类型，如数字: Integer
  - result: 返回值的类型，即doInBackground()方法返回值的类型，返回的值将传入doPostExecute(result)的参数中
2. 重写几个方法
  - void  onPreExecute(): 在执行异步任务前调用该方法, 一般用于初始化进度条等等.
  - Result doInBackground(Object[] params) : 该方法内的所有操作都在子线程中执行, 不能直接处理和显示UI, 当方法执行结束返回后,线程结束, 一般用于执行异步任务
  - void onPostExecute(Result result): 上面的方法返回后，结果传入该方法的参数,该方法在主线程中执行
  - void onProgressUpdate(Progress... values): 在doInBackground()中调用publishProgress(progress)就会回调该方法, 用于实时显示任务进度
  - 一般构造方法会传入活动的Context 对象

3. 创建该类实例,调用execute方法

```java
	MyAsyncTask task = new MyAsyncTask(this);
	task.execute(params);
```
**注意： 除了doInBackground方法在子线程中执行，其他方法都在主线程中执行，故可以直接操作UI组件**

