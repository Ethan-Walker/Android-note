# Android 解析xml 资源

1. Android 默认（推荐）采用Pull解析

获取解析器对象，本质上使用`XmlPullParser`解析, `XmlResourceParser` 是 `XMLPullParser` 对象的子类 
 ` XmlResourceParser parser = getResources().getXml(R.xml.book);`

2. 如果通过其他方式解析
`InputStream openRawResource(); `先获取文档对应的输入流，再 选择其他解析方式进行解析

 

```java
   XmlResourceParser parser = getResources().getXml(R.xml.book);
    StringBuilder sb= new StringBuilder("");
    try {
        while(parser.getEventType()!=XmlResourceParser.END_DOCUMENT){
            if(parser.getEventType()==XmlResourceParser.START_TAG){
                String name = parser.getName();
                if(name.equals("book")){
                    String price = parser.getAttributeValue(null,"price");
                    String date = parser.getAttributeValue(1);
                    sb.append("price: ").append(price).append(" + date: ").append(date).append("\n");
                }
            }
            parser.next();
        }
    } catch (XmlPullParserException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
```

