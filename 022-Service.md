# Service
> 服务一般适用于向应用程序提供某个特定功能,在Service子类中
服务中程序默认都是在主线程中执行

## 1. 创建服务MyService()
1. 选择重写方法

- IBinder onBind(Intent Intent): 抽象方法,必须重写, 当绑定服务时回调该方法:  bindService(Intent,ServiceConnection,int flags). 返回Binder子类对象, 返回结果传给绑定该服务的ServiceConnection子类中的 onServiceConnected(IBinder service) 参数中

- onCreate():  创建服务时调用
- onStartCommand(): 启动服务时调用 startService(intent)
- onDestroy(): 终止/解绑服务时调用


2. 创建内部类，继承 Binder, 内部实现MyService提供的服务，可以理解为为服务绑定具体的功能, 则外部活动可以通过 创建Binder对象, 调用其内部方法, 得到想要的服务


3. 创建内部类DownloadBinder 实例, 并在 onBind()方法中返回该实例

```java
public class MyService extends Service {
	private  DownloadBinder mBinder  = new DownloadBinder();
	public IBinder onBind(Intent intent) {
		return mBinder;
    }
    class DownloadBinder extends Binder {
        public void startDownload() {

        }
        public void stopDownload() {

        }
    }
}
```

## 2. 在AndroidManifest.xml 中注册

```java
<service
    android:name="com.example.ethanwalker.bestparctice.DownloadService"
    android:enabled="true"
    android:exported="true">
</service>

```
## 3. 在活动使用自定义服务
1. 通过ServiceConnection子类对象,使得活动与服务进行连接, 重写抽象方法
- onServiceConnected:  主活动中调用bindService():回调->onBind()->onServiceConnected，连接成功之后，在该方法中通过 Binder 子类调用自定义Service提供的功能

```java
private ServiceConnection connection = new ServiceConnection() {
        // service 是MyService中的 onBind 回调的返回结果
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            downloadBinder = (MyService.DownloadBinder) service;
            downloadBinder.startDownload();
            downloadBinder.getProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

```



2. 启动和终止服务, 绑定和解绑服务

```java
 Intent intent2 = new Intent(this, MyService.class);
```

	1. startService(intent2);   首次启动，回调 onCreate()和 onStartCommand(),非首次只回调onStartCommand()
	2. stopService(intent2);     MyService 中的 onDestroy()回调

	3. bindService(intent3, connection, BIND_AUTO_CREATE);
		- 如果Service未启动，先回调 onCreate()， 如果已经启动不再回调
		- 然后MyService中的onBind 方法会回调，返回Binder对象作为参数service 传入onServiceConnected
		- 回调 ServiceConnection 中的 onServiceConnected 方法

	4. unbindService(connection);   MyService中的 onDestroy 会回调

注意: 如果onStartCommand()被启动，则要想解绑服务必须 stopService() + unbindService() 配合使用才能回调 onDestroy() 方法

补充：Service 类中包含 stopService() 方法，用于停止自身服务


## 4. 扩展

### 1. 使用前台服务

```java
class MyService extends Service{
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "onCreate: ");
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setContentTitle("标题")
                        .setContentText("内容")
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
        startForeground(1, notification);   // 显示通知，设置为前台
    }
}
```
### 2. IntentService
1. 使用普通的Service处理耗时操作，需要创建新线程
> 普通的Service 在启动之后，除非手动停止 stopService() /stopSelf() / onbindService(),要不然会一直处于运行状态
> 有时候需要实现异步的、执行完自动停止的服务，则需要在执行异步任务的子线程中 调用 stopService()

```java
class MyService extends Service{
	int onStartCommand(...){
	new Thread(new Runnable(){
		public void run(){
			....
			stopSelf();
		}
	}).start();
	}
}

```

2. 使用IntentService 代替上述操作
 	- onHandleIntent: 内部的操作是在新线程中执行，且执行完之后，自动调用 stopSelf()关闭当前服务
	- 构造方法:  IntentService 只有有参构造，一般传入子类的类名即可

```java
// 简化服务中的耗时操作，开启一个线程执行耗时操作
public class MyIntentService extends IntentService {

    private static final String TAG = "MyIntentService";

    //提供无参构造函数，调用父类有参构造函数
    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy:" );
    }
}

```
