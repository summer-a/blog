package com.hjb.blog.controller.home;

import com.hjb.blog.entity.normal.Music;
import com.hjb.blog.service.normal.MusicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 歌单
 * @author 胡江斌
 * @version 1.0
 * @title: MusicController
 * @projectName blog
 * @description: TODO
 * @date 2019/8/12 9:56
 */
@RestController
@RequestMapping("/music")
public class MusicController {

    @Resource
    private MusicService musicService;

    @GetMapping("/list")
    public List<Music> list() {
        // 歌曲列表
        Music music = new Music();
        music.setStatus(true);
        return musicService.select(music);
    }

    @GetMapping("/all")
    public List<Music> all() {
        return musicService.selectAll();
    }
}
