# BroadCast

## 全局广播
### 1. 静态注册广播
1. 创建`BroadCast`的子类，重写 `onReceive`方法，`onReceive`内是接收到广播时的操作
2. 在`AndroidMainFest`内，添加 `receiver`
   enabled:  表示是否启用该接收器
   exported：表示是否允许该广播接收其他程序的广播
   默认都为true
   intent-filter中的action ，指明该广播接收器 指定接收的广播类型，可以是系统的广播，也可以是自定义的

   **系统广播：很多时候访问系统广播都要有访问权限。**
   例： 
  1. 接收网络信息的广播：
  - 在`intent-filter` 中添加 `<action android:name = android.net.conn.CONNECTIVITY_CHANGE>`
  - 需要在 `AndroidMainFest.xml` 中添加 访问权限
`<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
 2. 接收开机启动的广播：
 - 在`intent-filter`中添加` <action android:name="android.intent.action.BOOT_COMPLETED" />`
 - 然后添加访问权限
   `<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />`

  ```
<receiver
            android:name=".MyBroadCastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter android:priority="100">
                <action android:name="com.example.ethanwalker.MYBROADCASTRECEIVER" />
            </intent-filter>
</receiver>
  ```

静态创建广播可以不需手动创建，**file->new -> other -> broadcast receiver**
### 2. 动态注册广播
1. 定义广播接收器类，重写 onReceive 方法
2. 在Activity 中注册 广播接收器
	1.  创建`IntentFilter` 对象，设置为 只接受网络改变的广播消息
        `intentFilter = new IntentFilter();`
	    ` intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");`
    2.   创建广播接收器对象
    `networkChangeReceiver = new NetworkChangeReceiver();`
    3. 注册广播接收器，将`IntentFilter`与其绑定，即接收器只接收所有网络改变的广播消息
	  ` registerReceiver(networkChangeReceiver, intentFilter);`
3. 手动发送广播
	
	```java
	Intent intent = new Intent("com.example.ethanwalker.MYBROADCASTRECEIVER");
sendBroadCast(intent); 
	```
#### 对比动态和静态注册
- 静态注册的广播无法取消，而动态注册的广播接收器可以随时取消。

### 3. 有序和标准广播
前面默认的都是标准的广播，即发送的广播能被所有监听该广播的接收器接收. 但多个接收器接收广播的顺序是未知的（随机）。 我们可以明确指定发送的是有序广播，这样接收器就能通过指定不同的优先级控制接收器的接收顺序，这就是有序广播。
有序广播的重要特征：
- 某一接收器可以截断该广播，即阻止后序的接收器接收该广播
- 多个接收器可以指定接收器的优先级

和标准广播的代码区别:
- `sendBroadCast(intent) ->  sendOrderedBroadCast(intent,null)`
-  在广播接收器的 <intent-filter> 内添加`priority` 属性，指明优先级

	```xml
<receiver>
	    <intent-filter android:priority="100">
		 ...
	    </intent-filter>
</receiver>
	```
- 如果是动态注册广播，调用`IntentFilter`对象的 `setPriority()`设置优先级
- 先接受的广播可以在 onReceive 方法中截断广播的传播
    `abortBroadcast();`

## 本地广播

本地广播接收器默认只能接收本程序内的广播，发送的广播范围仅限于本程序内

代码与全局广播的区别：
- 定义LocalBroadcastManager对象
		  `LocalBroadcastManager localBroadcastManager =  LocalBroadcastManager.getInstance(this);`
- 发送广播时，调用 `LocalBroadcastManager` 的 `sendBroadCast` 方法
	 `localBroadcastManager.sendBroadcast(intent);`
- 注册广播时，调用 `LocalBroadcastManager`的 `registerReceiver`
	`localBroadcastManager.registerReceiver(localReceiver, intentFilter);`



## 广播实现强制下线功能原理
分析：用户登录后，如果监听到下线通知，无论在该应用程序的哪个活动中，都应该弹出对话框，提醒用户。假设该通知是以广播的形式发出，不可能在每个Activity 中都得创建该广播接收器，故创建一个基类  BaseActivity,让该应用程序的所有Activity 都继承该BaseActivity，在该BaseActivity中创建 广播接收器，同时注册，并实现对 该广播的处理（弹出对话框）



		 
