package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: Music
 * @projectName blog
 * @description: TODO
 * @date 2019/6/15 3:28
 */
public class Music extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "mp3")
    private String mp3;

    @Column(name = "poster")
    private String poster;

    @Column(name = "status")
    private Boolean status;

    public Music() {
    }

    public Music(String title, String artist, String mp3, String poster, Boolean status) {
        this.title = title;
        this.artist = artist;
        this.mp3 = mp3;
        this.poster = poster;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Music{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", mp3='" + mp3 + '\'' +
                ", poster='" + poster + '\'' +
                ", status=" + status +
                '}';
    }
}
