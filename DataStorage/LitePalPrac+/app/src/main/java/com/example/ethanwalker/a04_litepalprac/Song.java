package com.example.ethanwalker.a04_litepalprac;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by EthanWalker on 2017/5/20.
 */

public class Song extends DataSupport {
    @Column(nullable = false)
    private String name;

    private int duration;

    @Column(ignore = true)
    private String uselessField;

    // 本应将Album的 主键作为该对象Song的外键
    // 实际上只是添加一个属性  album_id integer ，并没有Album这个属性，没有外键约束
    private Album album;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getUselessField() {
        return uselessField;
    }

    public void setUselessField(String uselessField) {
        this.uselessField = uselessField;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
