## 1. 用Toolbar替换Actionbar组件

1. 取消系统自带的ActionBar: 将values/styles 内的 parent设置为 "Theme.AppCompat.Light.NoActionBar"

styles.xml

```xml
<resources>

    <!-- Base application theme. -->
    <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

</resources>

```
2. 在主界面的布局文件中添加Toolbar组件

```xml
    <android.support.v7.widget.Toolbar
        android:id="@+id/toolbar"
        android:layout_width="match_parent"
        android:layout_height="?attr/actionBarSize"
        android:background="?attr/colorPrimary"
        app:popupTheme="@style/ThemeOverlay.AppCompat.Light"
        app:theme="@style/ThemeOverlay.AppCompat.Dark.ActionBar" />

```
app和android 前缀的区别： app为了兼容旧版本

app:theme   由于AppTheme的颜色为浅色，所以Toolbar是浅色主题, 其内部的文字为了和Toolbar进行区分，默认为深色（黑色），要想将文字变成白色，可将Toolbar设置为黑色主题

app:popupTheme 弹出的对话框默认和AppTheme相同，此时变成黑色，可以自定义主题设置成浅色

3. 在活动中加载Toolbar组件

```java
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
```

## 2. 修改标题栏上的名称
AndroidManifest.xml 中的application标签的属性 label 值代表 标题栏名称

activity标签中的label属性表示当前活动的 标题栏名称



## 3. 在标题栏中添加菜单项
1. 在res目录下新建menu文件夹，创建toolbar.xml 文件

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">
    <item
        android:id="@+id/backup"
        android:icon="@drawable/ic_backup"
        android:title="Backup"
        app:showAsAction="always" />

    <item
        android:id="@+id/delete"
        android:icon="@drawable/ic_delete"
        android:title="Delete"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/settings"
        android:icon="@drawable/ic_settings"
        android:title="Settings"
        app:showAsAction="never" />

</menu>
```
2. 在主活动中重写onCreateOptionMenu

```java
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
```

3. 为菜单的每个子项创建点击事件

```java
 public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup:
                Toast.makeText(this, "备份", Toast.LENGTH_SHORT).show();

        }
        return true;
    }
```
