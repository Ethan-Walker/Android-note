package com.example.ethanwalker.myapp;

import java.util.ArrayList;

/**
 * Created by EthanWalker on 2017/4/14.
 */

public class UserList {
    static ArrayList<User> users  = new ArrayList<>();

   static class User{
       private long imageId;
       private String name;

       public User(int imageId, String name) {
           this.imageId = imageId;
           this.name = name;
       }

       public long getImageId() {
           return imageId;
       }

       public void setImageId(long imageId) {
           this.imageId = imageId;
       }

       public String getName() {
           return name;
       }

       public void setName(String name) {
           this.name = name;
       }
   }
   static{
       users.add(new User(R.drawable.i,"AAAA"));
       users.add(new User(R.drawable.i,"BBBB"));
       users.add(new User(R.drawable.i,"CCCC"));
       users.add(new User(R.drawable.i,"DDDD"));
       users.add(new User(R.drawable.i,"EEEE"));
       users.add(new User(R.drawable.i,"FFFF"));
       users.add(new User(R.drawable.i,"GGGG"));
       users.add(new User(R.drawable.i,"HHHH"));
       users.add(new User(R.drawable.i,"IIII"));
       users.add(new User(R.drawable.i,"JJJJ"));
       users.add(new User(R.drawable.i,"KKKK"));
       users.add(new User(R.drawable.i,"LLLL"));
       users.add(new User(R.drawable.i,"MMMM"));
       users.add(new User(R.drawable.i,"NNNN"));
       users.add(new User(R.drawable.i,"OOOO"));
       users.add(new User(R.drawable.i,"PPPP"));

   }

   public void add(User user){
       users.add(user);
   }
}
