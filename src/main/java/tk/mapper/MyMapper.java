package tk.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author 胡江斌
 * @version 1.0
 * @title: UserContoller
 * @projectName blog
 * @description: TODO 基础mapper，不能被扫描到，BlogApplication中只扫描com.hjb.blog.mapper
 * @date 2019/6/9 10:14
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}