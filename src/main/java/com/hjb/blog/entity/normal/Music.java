package com.hjb.blog.entity.normal;

import com.hjb.blog.entity.base.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: Music
 * @projectName blog
 * @description: TODO
 * @date 2019/6/15 3:28
 */
@Data
@NoArgsConstructor
@Table(name = "music")
public class Music extends BaseEntity {

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

}
