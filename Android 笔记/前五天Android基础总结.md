总结：

	第一天： android入门： 
			
			1.环境搭建 
					
				jdk+ eclipse+android sdk

			2，项目结构介绍
				熟悉各个目录的用途.
			3.五大布局

				LinearLayout + RelativeLayout
				控件： EditText TextVeiw Button ImageView ListView ScrollView CheckBox ProgressBar 
			4.adb的练习
				adb devices 
				adb install ; adb uninstall ; adb push ;adb pull;logcat ;adb shell
				adb kill-server ;adb start-server ;adb shell+input text  ;adb shell+monkey

			5.android系统架构
				4层；


			6. 电话拨号器

					Intent intent = new Intent();
					intent.setAction();
					intent.setData();
					startActivity();
			

	第二天：数据的存储

			1.登录案例
				
				File file = context.getFilesDir();
				context.openFileOutput(String filename,String mode);//mode:Context.Mode_Private
				context.openFileInput(String filename);

			2.sdcard的使用
					1.权限
					2.硬性编码
					File file = Enviroment.getExternalStorageDirectory();//获取sdcard的目录文件
					3.判断sdcard状态
						boolean state = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
					4.判断sdcard大小
						File file = Enviroment.getExternalStorageDirectory();//获取sdcard的目录文件
						long usablespace= file.getUsableSpace();
						String usablespace_str= Formatter.foramatterFilesize(long size);

			4.SharedPreference的使用
	
					1.保存数据
	
					a. 获取一个sharedpreference对象
							Sharedpreference sharedpreference = context.getShare..(String name ,String mode);
					ｂ．得到一个Editor对象
						Editor editor = 	sharedpreference.edit();
					c.往Editor中添加数据
							editor.putString(String key ,Stirng value);
					d.提交Editor
						editor.commit();


					2.读取数据
							
					a. 获取一个sharedpreference对象
							Sharedpreference sharedpreference = context.getShare..(String name ,String mode);
					b.使用sharedpreference获取数据
							sharedpreference.getString(String key ,String defValue);

			5.xml的生成与解析
				a.生成  XmlSerializer  模板代码
				b.解析 XmlPullParser 模板代码




第三天： 数据库 listview

		1.数据库的使用  对于一个数据库2个类， 1是帮助类 SqliteOpenHelper 2是Dao类封装增删改查

			1.创建数据库
				创建一个类集成SqliteOpenHelper 创建一个构造方法设置数据库的名称，版本，实现两个方法，oncteate：特别适合做表结构的初始化，数据库第一次创建的时候调用。
				onupgrade:特别适合做表结构的修改，数据库版本发生改变时调用
			2.增删改查
				a.创建一个帮助类的对象，并调用getReadableDatabase或getWriteableDatabase帮助获取一个SqliteDatabase数据库操作对象。

				b.使用SqliteDatabase数据库操作对象做增删改查
					2种方式
					1.调用execSql(String sql ,Object[] obj)可以做增删改，调用rawQuery(String sql ,Object[] obj)做查询，返回一个Cursor对象，遍历Cursor获取查询结果。
					2.调用insert delete update query做增删改查

					第一种方式适合做查询，第二种方式有了返回值，更适合做的增删改
						
				
		2.listview的使用

					1.写布局
					2.找到listview,并设置条目的点击事件
						listview.setOnItemClickListener(OnItemClickListener on);
					3.获取listview条目上要展示的数据到list集合中,传给adapter做控制显示
					4.写一个类集成BaseAdapter,实现四个方法，写一个构造方法接受list集合中的数据

							getCount();返回listview要显示的条目数
							getItem(int postion);返回listveiw上postion位置上的Bean对象
							getId();返回postion
							*****getView();告诉listview条目要显示的内容
								1.模板代码优化listview
								2.创建一个item布局转化成view对象作为getview的返回值
								3.获取item布局中子控件
								4.获取list集合中postion位置的Bean对象
								5.将Bean中的数据设置给子控件做展示
					5.创建一个adapter，并设置给listview
							listveiw.setAdapter(ListAdapter adapter)
					6.实现条目点击事件的操作



第四天：网络编程
			
		1.使用UrlConnection连接网络获取数据

				1.创建一个Url对象
				2.使用URL对象获取一个HttpUrlConnection
				3.设置connection对象的参数
				4.获取状态码
				5.获取流信息。
	

		2.主线程不能做耗时的操作（网络请求），只能在子线程中请求；子线程不能更新UI；解决办法：Handler

		3.使用handler
				1.主线程创建Handler，重写handlermessage方法
				2.子线程中创建Message对象，携带子线程获取的数据
				3.使用主线程的handlerf发送message到主线程
				4.主线程的handlerMessage方法接受数据，处理Ui

		4.流转图片

			Bitmap bitmap = BitmaptFatory.decodeStream(InputStream in);
		5.网络版新闻： 数据库  listview   URLConnection  Json解析  Handler		
				json解析：
					JsonObject  JsonArray
		6.get post请求网络
			post提交数据需要设置内容到写入流：
				connection.setDoOut（true）;
				connection.getOutputStream().write(byte[]);
		7.解决乱码问题
				1.客户端和服务端编码保持一致
				2.提交的数据需要用URLEncode编码

第五天  网络请求2    HttpClient   文件上传  多线程下载
				
				1.HttpClent:
					1.创建一个DefaultHttpclient
					2.创建一个请求方式 HttpGet HttpPost
						
							Arraylist<BaicNameValueparire> list = new Arraylist<BaicNameValueparire>();
							BaicNameValueparire valuse =	BaicNameValueparire（）
							valuse.put(String key ,String valuse);
								list.add(valuse);
							UrlEncodeFormEntity entity = new UrlEncodeFormEntity(List<NameValuepare>);
							HttpPost.setEntity(entity);
					
					3.httpclient执行请求
						httpeclient.execut(HttpGet httpget)
					4.获取响应码
							httpclient.getStateLines().getstateCode();
					5.获取内容
						InputStream in = httpclient.getEntry.getContent();

				2.文件上传 使用开源项目  AsycnHttpClient

				3.多线程断点续传下载		

					1.获取资源大小
					2.本地创建一个与服务端一致的文件			
						RandmodAcecssFile
					3.根据线程数量计算每个线程下载的开始位置，结束位置

					4.开启线程进行真实的下载。	
						header : Range  value :   bytes:0-499  
						UrlConnection.setRequestProperty(String header , String value);
				
					5.将读取 的流分段写入文件
							RandmodAcecssFile file = new RandmodAcecssFile（File file,String mode）;
							file.seek(int startIndex);//设置文件从哪里开始写入
					6.ProgressBar : 主要属性 ： style   样式   max 最大进度   progress 当前进度  