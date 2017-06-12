1. Android 中的 Application类，在每次程序启动时，系统就会对该类初始化，我们可以通过订制自己的 Application类，方便管理全局信息

```java

public class MyApplication extends Application {

    private static final String TAG = "MyApplication";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mContext = getApplicationContext();
        Log.e(TAG, "onCreate: " );
    }

    public static Context getContext(){
        return mContext;
    }
}


```

2. 在AndroidManifest.xml 中的 application 标签内配置自定义 MyApplication

```xml
<application
    android:name="com.exmaple.a14_extensionskill.MyApplication"
```
目的是告诉系统，在启动的时候初始化自定义的 MyApplication类，而不是默认的Application

