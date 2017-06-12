Lambda ��������һ������������û�з�������û�з������η���û�з���ֵ����

1. ʹ��ǰ���� app/build.gradle

```js
	android {
		defaultConfig{
			...
			jackOptions.enabled = true
		}
		compileOptions {
        	sourceCompatibility JavaVersion.VERSION_1_8
        	targetCompatibility JavaVersion.VERSION_1_8
    	}
	}


```

2. ʹ�÷���

��ĳ���ӿڽ���һ����ʵ�ַ���������ʹ�� lambda ���洴����ͳ�������ڲ��෽��

��1 ��

```java

new Thread(new Runnable(){
	public void run(){

	}
});
```

��Lambda���ʽ�Ϳ���д��

```java
new Thread(()->{
	// run�����ڲ���ʵ��
});

```
���Կ���  ()->{...}  ʵ���˴��������ڲ��࣬�Լ���������д


��2��

```java
Runnable runable = new Runnable(){
	@Override
	public void run(){

	}
}


��Lambda ���棺

Runnable runnable = ()->{

}
```

��3�� ��������Lambda���ʽ


```java

button.setOnClickListener(new View.OnClickListener(){
	@Override
	public void onClick(View v){
		...
	}
});


��Lambda ���ʽ
button.setOnClickListener((String v)->{

});

����ʡ������������
button.setOnClickListener((v)->{

});

��ֻ��һ������ʱ������ʡ�����ţ�
button.setOnClickListener( v ->{

});

```
