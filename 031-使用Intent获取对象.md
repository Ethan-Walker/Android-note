比较 Serializable 和 Parcelable ：

Serializable： 实现起来容易，但要把整个对象序列化，效率低
Parcelable：实现逻辑复杂， 效率高，一般用这个

## 1. 通过自定义类实现Serializable

- Person

```java
public class Person implements Serializable {

    private String name;
    private int age;

}
```

- FirstActivity.java

```java
Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
Person person = new Person("张三",20);
intent.putExtra("person",person);
startActivity(intent);

```

- SecondActivity

```java

Intent intent = getIntent();
Person person = (Person)intent.getSerializableExtra("person");

```

## 2. 自定义类实现 Parcelable

- People

```java
public class People implements Parcelable {

    private String name;
    private int age;

    public People(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @Override
    public int describeContents() {
        return 0;         // 返回0 即可
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);      // 写出name
        dest.writeInt(age);				// 写出age
    }

    public static Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>(){
        @Override
        public People createFromParcel(Parcel source) {
            People people = new People(source.readString(),source.readInt());  // 读取name，读取age
            return people;
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];     // 返回 大小为 size 的People数组
        }
    };
```
