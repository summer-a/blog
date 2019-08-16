package com.hjb.blog.service.normal.impl;

import com.hjb.blog.entity.normal.Music;
import com.hjb.blog.mapper.MusicMapper;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.common.RedisService;
import com.hjb.blog.service.normal.MusicService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 音乐服务
 * @author 胡江斌
 * @version 1.0
 * @title: MusicServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/15 3:33
 */
@Service
public class MusicServiceImpl extends BaseServiceImpl<Music> implements MusicService {

    @Resource
    private MusicMapper musicMapper;

    /**
     * 歌单查询
     * @param music 条件
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "music", key = "#music.toString()")
    @Override
    public List<Music> select(Music music) {
        return musicMapper.select(music);
    }

    /**
     * 全部歌单
     * @return
     */
    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "music", key = "#root.methodName")
    @Override
    public List<Music> selectAll() {
        return musicMapper.selectAll();
    }
}
