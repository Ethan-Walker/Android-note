### 1. 使用JSONObject

1.
+ json 形式为 { name:'',age:''} ， 即单个JSON对象
`JSONObject jsonObject = new JSONObject(data);`

+ json 形式为数组 [{name:"",age:""},{name:"",age:""}]

```java
 private void parseWithJSONObject(String responseData) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(responseData);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject =  jsonArray.getJSONObject(i);
                String id = jsonObject.getString("id");
                String name = jsonObject.getString("name");
                String version = jsonObject.getString("version");
//                Toast.makeText(JsonParseTest.this, , Toast.LENGTH_SHORT).show();
                // 子线程中不能操作UI
                Log.e("id/name/version",id + "/" + name + "/" + version);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
```

嵌套：

data:

```json
{
    "apps": [
        {
        	name:""
        },
        {
			name:""
   		 }
     ]

}
```

```java


JSONObject obj = new JSONObject(data);
JSONArray array = obj.getJSONArray("apps");
for(int i=0;i<array.length();i++){
	// JsonObject tempObj =  array.getJSONObject(i);

	String resData = array.getJSONObject(i).toString();
	App app = new Gson().fromJson(resData,App.class);
}

```

### 2. 使用GSON 开源组件
1. 添加依赖 `compile 'com.google.code.gson:gson:2.7'`
2. 创建和JSON文件中属性一一对应的 类，如App,并生成set、get方法
3.
+ json 形式为 { name:'',age:''} ， 即单个JSON对象

```java
App app = new Gson().fromJson(responseData,App.class);
```

+ json 形式为数组 [{name:"",age:""},{name:"",age:""}]

```java
// 根据json内容和创建的类之间互相匹配，返回包含该类对象的集合
 List<App> apps = new Gson().fromJson(responseData,new TypeToken<List<App>>(){}.getType());
```
