# 1.TextView #

	<TextView
		android:id="@+id/text_view"
		android:layout_width="match_parent"
    	android:layout_height="wrap_content"
   		android:text="这是文本框" />

其他属性：
### (1) `android:gravity="top|center"`###
	

	指定文字的对齐方式：top,bottom,center,left,right 可以用 | 同时指定多个值
	center 等价于 center_vertical|center_horizontal

### (2) TextView ###

# 布局
##LinearLayout ：线性布局
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">     

    <Button
        android:id="@+id/open_alert_dialog"
        android:layout_width="match_parent"  
        android:layout_height="wrap_content" 
        android:text="弹出AlertDialog对话框" />

属性：  orientation  排列方式 vertical/horizontal,默认是horizontal排列
        gravity: 子控件相对于当前控件的对齐方式
            center(等价于center_vertical|center_horizontal)
            left
            top
            right
            bottom
            两种组合 left|bottom
        layout_gravity: 当前控件相对于父控件的对齐方式

        当排列方式是：horizontal时，layout_gravity 只能在垂直方式的对齐方式才有效
                      vertical时，                        水平

        layout_weight:1   （同时要设定layout_width='0dp'）给控件的宽度分配比例

##RelativeLayout 相对布局##
属性：
        相对布局没有 orientation,layout_gravity

        相对父对齐:      layout_alignParentLeft/Right/Top/Bottom ="true" 特殊的：layout_centerInParent="true"

        相对彼此之间：   layout_alignLeft/Right/Top/Bottom = "true"
                        layout_toLeftOf="@id/button1" 在button1的左边/右边 toRightOf()
                        layout_below="@id/button1" 在button1的下边
                        layout_above="@id/button1" 在button1的上边

## FrameLayout 帧布局
属性： 
       有 layout_gravity

## 百分比布局
### PercentRelativeLayout

首先在app/build.gradle 文件中的 depedencies 中加 compile 'com.android.support:percent:24.2.1'

<android.support.percent.PercentRelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
                                             >

    <Button
        android:id="@+id/button_1"
        android:layout_alignParentLeft="true"
        android:layout_alignParentTop="true"
        android:text="Button_1"
        app:layout_heightPercent="50%"
        app:layout_widthPercent="50%" />

属性： 
    继承RelativeLayout
    layout_widthPercent  layout_heightPercent="50%"


#自定义控件

## 引入布局 
        隐藏默认的标题栏
                    ActionBar actionBar = getSupportActionBar();
                        if(actionBar!=null){
                             actionBar.hide();
                        }

        在layout 下创建一个自定义的title.xml,写好布局
        在其他xml文件中包含title.xml
                <include layout="@layout/title" />


## 引入自定义控件
若title.xml布局中绑定了多个事件，则引入title.xml的活动，都需要 注册，绑定事件，造成代码的冗余

解决办法：自定义控件绑定title.xml中
1. 新建TitleLayout.java 文件，继承LinearLayout
2. 动态加载布局文件
3. 在TitleLayout.java中注册，绑定事件
4. 引入title.xml布局中：格式
            <com.example.ethanwalker.uicustomviews.TitleLayout  （必须写完整的包）
                 android:layout_height="wrap_content"
                 android:layout_width="match_parent"/>

TitleLayout.java 

        public class TitleLayout extends LinearLayout {
            public TitleLayout(Context context,AttributeSet attrs) {
                super(context, attrs);
                LayoutInflater.from(context).inflate(R.layout.title,this);  //动态加载布局文件title

                Button back = (Button)findViewById(R.id.back);
                Button edit = (Button)findViewById(R.id.edit);
                back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((Activity)getContext()).finish();
                    }
                });
                edit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"you clicked edit",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }


        getContext()  获取当前运行的Activity，返回的是Context对象