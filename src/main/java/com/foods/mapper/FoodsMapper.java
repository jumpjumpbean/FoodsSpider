package com.foods.mapper;

import com.foods.domain.Foods;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * Created by jacob on 2017/4/17.
 */
@Mapper
@Component
public interface FoodsMapper {
    @Insert("insert into foods (`name`,`category`,`image`,`nutrient_content`,`effect`) values (#{name},#{category},#{image},#{nutrition},#{value})")
    public int add(Foods foods);
}
