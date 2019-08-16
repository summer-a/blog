package com.hjb.blog.service.normal.impl;

import com.hjb.blog.entity.normal.Notice;
import com.hjb.blog.service.base.impl.BaseServiceImpl;
import com.hjb.blog.service.normal.NoticeService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: NoticeServiceImpl
 * @projectName blog
 * @description: TODO
 * @date 2019/6/12 22:11
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<Notice> implements NoticeService {

    @Transactional(readOnly = true, rollbackFor = RuntimeException.class)
    @Cacheable(value = "notice", key = "#notice.toString()")
    @Override
    public List<Notice> select(Notice notice) {
        return super.select(notice);
    }
}
