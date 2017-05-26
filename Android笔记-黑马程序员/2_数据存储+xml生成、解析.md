# 数据的存储


# 1.测试的相关概念 (了解)
	
		SUV  好的软件不是开发出来的是测试出来的
	
		jd 黑客
		当当： -10 

		1.测试是否知道源代码
			黑盒测试  不知道代码
			白盒测试  知道代码
		2.按照测试的粒度
			方法测试
			单元测试 Junit
			集成测试
			系统测试
		3.按照测试的暴力程度
			冒烟测试  硬件
			压力测试 12306

		monkey测试： adb shell下的一个测试指令。 adb shell +　monkey -p packagename count;

# 2.单元测试(了解，会用即可)
	1.创建一个类集成AndroidTestCase，那么该类就具备单元测试的功能。

	2.需要在androidmanifest.xml中的application节点下配置一个uses-library;
		<uses-library android:name="android.test.runner" />
	3.需要在androidmanifest.xml中的manifest节点下配置一个instrumentation;targetPackage：需要测试的工程的包名。
		    <instrumentation
        android:name="android.test.InstrumentationTestRunner"
        android:targetPackage="com.itheima.junit" />

	4.如果不知道怎么配置androidmanifest.xml,可以新建一个android test project工程，会自动配置.
# 3.Logcat日志猫工具的使用 (会用即可)

		包括五种级别，可以添加过滤器过滤日志信息。能够帮助我们观察程序运行的状态。
		e:
		w:
		i:
		d:
		v:

		在公司开发中一般打印日志用Log类，通常会封装一个LogUtils，通过开关来控制日志信息的打印。


		开发时：设置switch =true；
		发布后：设置为true
		public class LogUtils{	
			private boolean switch = true;
			public static void e(String tag,String msg){
				if(switch){
					Log.e(tag,msg);
				}
			}
		}

		
	使用：LogUtils.e(tag,msg)
		
	
# 4.把数据存储到文件(login案例)  android 下的数据存储 

	1.写布局
		LinearLayout + RelativeLayout
	2.写业务逻辑
		a.找到相应控件

		b.设置按钮的点击事件

		c.在onclick方法中，获取用户输入的用户名密码和是否记住密码

		d.判断用户名密码是否为空，不为空请求服务器（省略，默认请求成功）

		e.判断是否记住密码，如果记住，将用户名密码保存本地。???? 

		f.回显用户名密码 ??


		//通过context对象获取私有目录，/data/data/packagename/filse
		context.getFileDir().getPath()


细节问题：


1. 定义私有成员变量` private Context mContext=this;`后面需要传入Context 对象时，直接传入mContext
2. TextUtils.isEmpty(String str);  判断字符串是否为空

# 5.存储到SD卡,获取SD的大小及可用空间  （重点）
	
	使用Sdcard注意事项：

	1.权限问题：	
		<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	2.硬性编码问题：通过 Environment可以获取sdcard的路径
		 Environment.getExternalStorageDirectory().getPath();
	3.使用前需要判断sdcard状态
			if(!Environment.getExternalStorageState().equals( Environment.MEDIA_MOUNTED)){
					//sdcard状态是没有挂载的情况
					Toast.makeText(mContext, "sdcard不存在或未挂载", Toast.LENGTH_SHORT).show();
					return ;
				}
	4.需要判断sdcard剩余空间
					//判断sdcard存储空间是否满足文件的存储
				File sdcard_filedir = Environment.getExternalStorageDirectory();//得到sdcard的目录作为一个文件对象
				long usableSpace = sdcard_filedir.getUsableSpace();//获取文件目录对象剩余空间
				long totalSpace = sdcard_filedir.getTotalSpace();
				//将一个long类型的文件大小格式化成用户可以看懂的M，G字符串
				String usableSpace_str = Formatter.formatFileSize(mContext, usableSpace);
				String totalSpace_str = Formatter.formatFileSize(mContext, totalSpace);
				if(usableSpace < 1024 * 1024 * 200){//判断剩余空间是否小于200M
					Toast.makeText(mContext, "sdcard剩余空间不足,无法满足下载；剩余空间为："+usableSpace_str, Toast.LENGTH_SHORT).show();
					return ;	
				}


		/data/data: context.getFileDir().getPath();
					是一个应用程序的私有目录，只有当前应用程序有权限访问读写，其他应用无权限访问。一些安全性要求比较高的数据存放在该目录，一般用来存放size比较小的数据。
		/sdcard:  Enviroment.getExternalStorageDirectory().getPath();
					是一个外部存储目录，只用应用声明了<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>的一个权限，就可以访问读写sdcard目录；所以一般用来存放一些安全性不高的数据，文件size比较大的数据。
		
# 7.文件的权限概念 (了解)

	//通过context对象获取一个私有目录的文件读取流  /data/data/packagename/files/userinfoi.txt
	FileInputStream fileInputStream = context.openFileInput("userinfo.txt");

	//通过context对象得到私有目录下一个文件写入流； name : 私有目录文件的名称    mode： 文件的操作模式， 私有，追加，全局读，全局写
		FileOutputStream fileOutputStream = context.openFileOutput("userinfo.txt", Context.MODE_PRIVATE);	



	linux下一个文件的权限由10位标示：
	1位：文件的类型，d：文件夹 l:快捷方式  -:文件
	2-4： 该文件所属用户对本文件的权限 ， rwx ：用二进制标示，如果不是-就用1标示，是-用0标示；chmod指令赋权限。
	5-7：该文件所属用户组对本文件的权限
	8-10：其他用户对该文件的权限。
	
# 8.SharedPreferences介绍  (重点) 用来做数据存储

		sharedPreferences是通过xml文件来做数据存储的。
		一般用来存放一些标记性的数据，一些设置信息。


		*********使用sharedPreferences存储数据

				
			1.通过Context对象创建一个SharedPreference对象
				//name:sharedpreference文件的名称    mode:文件的操作模式
				SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
			2.通过sharedPreferences对象获取一个Editor对象
				Editor editor = sharedPreferences.edit();
			3.往Editor中添加数据
				editor.putString("username", username);
				editor.putString("password", password);
			4.提交Editor对象
				editor.commit();

		*********使用sharedPreferences读取数据

			1.通过Context对象创建一个SharedPreference对象
				SharedPreferences sharedPreferences = context.getSharedPreferences("userinfo.txt", Context.MODE_PRIVATE);
				
			2.通过sharedPreference获取存放的数据
				//key:存放数据时的key   defValue: 默认值,根据业务需求来写
				String username = sharedPreferences.getString("username", "");
				String password = sharedPreferences.getString("password", "");
				


		通过PreferenceManager可以获取一个默认的sharepreferences对象		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
# 9 生成xml的2种方式 
	

	1.写布局

	2.业务逻辑
		a.备份
			1.封装短信数据到list中
			2.将list中的数据写到xml文件中。
		b.恢复
			1.解析xml文件中短信数据，封装到list集合中
			2.将解析数据打印。


	XmlSerializer
	


	//使用XmlSerializer来序列化xml文件
	public static boolean backupSms_android(Context context){
		
		try{
			
			//0.获取短信数据
			ArrayList<SmsBean> allSms = SmsDao.getAllSms();
			//1.通过Xml获取一个XmlSerializer对象
			XmlSerializer xs = Xml.newSerializer();
			//2.设置XmlSerializer的一些参数，比如：设置xml写入到哪个文件中
			//os:xml文件写入流   encoding：流的编码
			xs.setOutput(context.openFileOutput("backupsms2.xml", Context.MODE_PRIVATE), "utf-8");
			//3.序列化一个xml的声明头
			//encoding:xml文件的编码  standalone:是否独立
			xs.startDocument("utf-8", true);
			//4.序列化一个根节点的开始节点
			//namespace:命名空间  name： 标签的名称
			xs.startTag(null, "Smss");
			//5.循环遍历list集合序列化一条条短信
			
				for (SmsBean smsBean : allSms) {
					xs.startTag(null, "Sms");
					//name:属性的名称  value：属性值
					xs.attribute(null, "id", smsBean.id+"");
					
					xs.startTag(null, "num");
					//写一个标签的内容
					xs.text(smsBean.num);
					xs.endTag(null, "num");
					
					
					xs.startTag(null, "msg");
					xs.text(smsBean.msg);
					xs.endTag(null, "msg");
					
					
					xs.startTag(null, "date");
					xs.text(smsBean.date);
					xs.endTag(null, "date");
					
					xs.endTag(null, "Sms");
				}

			//6.序列化一个根节点的结束节点
				xs.endTag(null, "Smss");
			//7.将xml写入到文件中，完成xml的序列化
				xs.endDocument();
				return true;

		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	
# 10.使用pull解析xml格式的数据

	dom解析：基于全文加载的解析方式   sax解析：基于事件的逐行解析方式  pull解析：同sax
		

	XmlPullParser

				//解析xml文件读取短信内容
	public static int restoreSms(Context context) {
		ArrayList<SmsBean> arrayList = null;
		SmsBean smsBean = null;
		try{
			//1.通过Xml获取一个XmlPullParser对象
			XmlPullParser xpp = Xml.newPullParser();
			//2.设置XmlPullParser对象的参数，需要解析的是哪个xml文件,设置一个文件读取流
		
			//通过context获取一个资产管理者对象
			AssetManager assets = context.getAssets();
			//通过资产管理者对象能获取一个文件读取流
			InputStream inputStream = assets.open("backupsms.xml");
			xpp.setInput(inputStream,"utf-8");
			//xpp.setInput(context.openFileInput("backupsms2.xml"), "utf-8");
			//3.获取当前xml行的事件类型
			int type = xpp.getEventType();
			//4.判断事件类型是否是文档结束的事件类型
			while(type != XmlPullParser.END_DOCUMENT){
				//5.如果不是，循环遍历解析每一行的数据。解析一行后，获取下一行的事件类型

				String currentTagName = xpp.getName();
				//判断当前行的事件类型是开始标签还是结束标签
				switch (type) {
				case XmlPullParser.START_TAG:
					if(currentTagName.equals("Smss")){
						//如果当前标签是Smss，需要初始化一个集合
						arrayList = new ArrayList<SmsBean>();
					}else if(currentTagName.equals("Sms")){

						smsBean = new SmsBean();
						smsBean.id = Integer.valueOf(xpp.getAttributeValue(null, "id"));

					}else if(currentTagName.equals("num")){
						smsBean.num =  xpp.nextText();
					}else if(currentTagName.equals("msg")){
						smsBean.msg =  xpp.nextText();
					}else if(currentTagName.equals("date")){
						smsBean.date =  xpp.nextText();
					}
					break;
				case XmlPullParser.END_TAG:
					//当前结束标签是Sms的话，一条短信数据封装完成， 可以加入list中
					if(currentTagName.equals("Sms")){
						arrayList.add(smsBean);
					}
					break;
				default:
					break;
				}

				type = xpp.next();//获取下一行的事件类型
			}

			return arrayList.size();

		}catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

