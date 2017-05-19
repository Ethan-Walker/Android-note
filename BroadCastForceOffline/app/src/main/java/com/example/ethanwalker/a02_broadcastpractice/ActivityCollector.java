package com.example.ethanwalker.a02_broadcastpractice;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EthanWalker on 2017/5/19.
 */

public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<>();

    public static void add(Activity activity) {
        activities.add(activity);
    }

    public static void remove(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
