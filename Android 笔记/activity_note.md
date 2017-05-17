# Activity #

## 1.向下一个活动传送数据 ##
**SecondActivity:**

按钮点击事件中：

			public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
                intent.putExtra("name","王犇");
                startActivity(intent);
		

**ThirdActivity:**

`onCreate` 方法中（即当页面加载时）

		Intent intent = getIntent();  // 获取传递进来的intent
        String name = intent.getStringExtra("name");
        if(name!=null){
            Log.d("得到的姓名:",name);
        }

---

## 2.向上一个活动返回数据 ##

**SecondActivity**

#### 在先导活动中，将 `startActivity(intent);`改成`startActivityForResult(intent,1);`   表示期望下一个活动返回数据，1表示请求码，可能有多个活动需要返回数据，用请求码来区分

#### 获取返回的数据 ####

 **当下一个活动销毁后，返回的数据需要通过重写 onActivityResult方法获得**

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    int age = data.getIntExtra("age",0);
                    Log.e("返回的年龄:",String.valueOf(age));
                }
                break;
            default:
                break;
        }
    }

---
**ThirdActivity**
#### （1）：通过按钮点击事件返回 ####

        // 向上一个活动返回数据，注意new Intent()内不能传入参数
        Button thirdButton = (Button)findViewById(R.id.thirdButton);
        thirdButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent  = new Intent();
                intent.putExtra("age",23);
                //设置返回数据，和返回状态
                setResult(RESULT_OK, intent);
                finish();   // 结束当前活动
            }
        });

#### (2) ####
# 3.活动的生命周期 #

- 当某activity stop后，系统会根据内存占用情况决定是否将该activity 回收，如果回收，则会导致数据丢失。

- 如果回收，下次启动该activity 会重新创建该activity，调用onCreate方法

    stop->回收->create->start->resume

- 未回收,下次重新启动

    stop->restart->resume

### 回收不等同于destroy，不能用destroy模拟回收，得到回收之前保存的数据，暂时无法模拟回收，仅仅只有回收时，再次进入该活动 ###

**解决办法：在被回收之前保存数据**

###  此方法在Stop之前调用，因为一旦进入Stop状态，就随时可能被回收3 

	数据保存到 Bundle对象中
	 @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String str="这是销毁之前保存的数据";
        outState.putString("save",str);
    }

**在创建活动时，判断传进来的 Bundle对象是否为空，为空则没有需要保存的数据**

	 @Override
    protected void onCreate(Bundle savedInstanceState) {
 		....


        if(savedInstanceState!=null){
            String save = savedInstanceState.getString("save");
            Log.e(TAG,save);
        }


### finish ###

- 当调用finish，生命周期 `pause->stop->destroy`  
- 执行`finish`之后，该`activity`从栈中取出，故当下一个Activity 返回时，不会返回到该`activity`，只能返回此`Activity`的前一个`Activity`

- `finish()` 虽然会导致 `onStop()`,但不会触发`onSaveInstanceState()`方法，也就不能手动模拟 回收=> 获取数据
	

# 4.页面中多个按钮事件，使当前`activity`实现`OnClickListener`#

	

	public class MainActivity extends AppCompatActivity implements View.OnClickListener {  实现View.OnClickListener接口，重写 onClick方法
	
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        .....
        Button normal = (Button) findViewById(R.id.start_normal_activity);
        Button dialog = (Button) findViewById(R.id.start_dialog_activity);
        Button fini = (Button) findViewById(R.id.exit);

        register(normal);
        register(dialog);
        register(fini);
    }
	// 封装注册事件
    public void register(Button b){
        b.setOnClickListener(this);
    }

    //View表示当前点击的控件，重写 onClick()方法，根据点击的id 判断是哪个控件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch(id){
            case R.id.start_normal_activity:
                startNormalActivity((Button)v);
                break;
            case R.id.start_dialog_activity:
                startDialogActivity((Button)v);
                break;
            case R.id.exit:
                exit((Button)v);
                break;
            default:
                break;
        }
    }
	
	// 单独声明每个事件
    public void startNormalActivity(Button b) {
        Intent intent = new Intent(MainActivity.this, NormalActivity.class);
        startActivity(intent);
    }

    public void startDialogActivity(Button b) {
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }

    public void exit(Button b) {
        finish();
    }

# 5.活动的启动模式 #

1. `standard`
		**标准启动模式**，不管当前活动在不在栈顶，只要启动，就会创建新的活动实例，压栈 

2. `singleTop`
	如果活动在栈顶，启动该活动，不会创建新的活动实例，直接到达该活动

3. `singleTask`
	 如果活动在返回栈中，就不会创建新的活动实例，**将位于该活动之上的栈中 活动全都销毁**，到达该活动

4. `singInstance`
	单独创建一个返回栈给该活动，不与其他活动共享栈。
	**注意：** **当应用程序退出时，依次退空所有栈中的活动**

	`getTaskId()` 获取当前活动所在返回栈的 `id`

# 6.工具类 #
##(1)获取当前活动的名称 ##

- 分析，每进入一个活动时，都会调用父类的 `onStart()`方法，故自定义一个类，继承`AppCompatActivity`,并重写`onStart()方法`,输出获得当前类的类名（即活动名）

**BaseActivity**

	public class BaseActivity extends AppCompatActivity {
   		 @Override
   		 protected void onStart() {
       	 super.onStart();
       	 Log.e("BaseActivity",getClass().getSimpleName()); 获取当前类的 类名
   		 }
	}

**让其他活动类继承`BaseActivity`类即可**

## (2)存放返回栈中的活动，可随时随地结束整个程序 ##

**ActivityCollector**

    public class ActivityCollector {
         private static List<Activity> activities = new ArrayList<>();

   	     public static void addActivity(Activity activity){
       	 	activities.add(activity);
   		 }
   		 public static void removeActivity(Activity activity){
       	 	activities.remove(activity);
   		 }
  		  public static void finishAll(){
       		 for(Activity activity:activities){
            	if (!activity.isFinishing()) {
               		 activity.finish();
            	}
        	}
   		 }
	}

**BaseActivity**

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);   // 每次创建活动，加到集合中
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);  // 销毁后，从集合中移走
    }

**ThirdActivity**

1. 当想退出程序时：

	    ActivityCollector.finishAll();
    
2. 杀掉整个进程，确保程序退出：

	    android.os.Process.killProcess(android.os.Process.myPid());
    	android.os.Process.myPid())  // 当前进程id

# 7.启动活动的最佳写法 #

分析：`firstActivity`	-->通过`intent`启动-->`secondActivity` 

由于s`econdActivity` 可能需要若干个参数通过 `intent` 传递过来，但是 `firstActivity` 并不知道其需要哪些参数，造成了 开发上的困难

解决办法：由需求者 `secondActivity` 根据需求，将需要的方法封装到静态函数中，这样提供者  `firstActivity` 就能调用其静态方法，通过传参的方式，将 `secondActivity `需要的参数传递过去

**secondActivity**

 	public static void startAction(Context context, String data1, String data2){
        Intent intent = new Intent(context,FourthActivity.class);
        intent.putExtra("name",data1);
        intent.putExtra("age",data2);
        context.startActivity(intent);

    }

注意：`Context` 保存的是 触发该`activity`启动 的`Activity `对象，`context.startActivity（）`声明由`context `触发启动

**firstActivity**

    startAction(firstActivity.this,"张三","213");