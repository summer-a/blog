package tk.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * 基础mapper，不能被扫描到，Application中只扫描tk.mapper
 * @param <T>
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}