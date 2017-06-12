�Ƚ� Serializable �� Parcelable ��

Serializable�� ʵ���������ף���Ҫ�������������л���Ч�ʵ�
Parcelable��ʵ���߼����ӣ� Ч�ʸߣ�һ�������

## 1. ͨ���Զ�����ʵ��Serializable

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
Person person = new Person("����",20);
intent.putExtra("person",person);
startActivity(intent);

```

- SecondActivity

```java

Intent intent = getIntent();
Person person = (Person)intent.getSerializableExtra("person");

```

## 2. �Զ�����ʵ�� Parcelable

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
        return 0;         // ����0 ����
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);      // д��name
        dest.writeInt(age);				// д��age
    }

    public static Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>(){
        @Override
        public People createFromParcel(Parcel source) {
            People people = new People(source.readString(),source.readInt());  // ��ȡname����ȡage
            return people;
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];     // ���� ��СΪ size ��People����
        }
    };
```
