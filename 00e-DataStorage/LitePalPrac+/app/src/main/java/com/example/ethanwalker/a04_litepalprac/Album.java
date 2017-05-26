package com.example.ethanwalker.a04_litepalprac;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by EthanWalker on 2017/5/20.
 */

public class Album extends DataSupport {
    // 没设置主键， 默认会创建 自动增长的主键 id

    @Column(unique =true,defaultValue = "unknown",nullable = false)
    private String name;

    @Column(ignore = false)
    private float price;

    private byte[] cover;

    private List<Song> songs = new ArrayList<>();
    // 一对多，本应将该对象的主键 id 作为 Song的外键，注意Song里面得声明 Album为其属性，Album 其实没有Song属性
    // 但实际上没有外键的约束，即 Song中的album_id 并不要求一定存在于 Album集合中

    private Date releaseDate;

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }
}
