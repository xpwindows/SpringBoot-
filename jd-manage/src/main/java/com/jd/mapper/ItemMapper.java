package com.jd.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jd.pojo.Item;
import com.jd.pojo.ItemDesc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ItemMapper extends BaseMapper<Item>{
    /**
     * 查询数据库时,要求最近的记录显示在最前方. 倒叙排列
     *
     * @param start
     * @param rows
     * @return
     */
    @Select("SELECT * FROM tb_item ORDER BY updated DESC LIMIT #{start},#{rows}")
    List<Item> findItemByPage(@Param("start")Integer start, @Param("rows")Integer rows);


}
