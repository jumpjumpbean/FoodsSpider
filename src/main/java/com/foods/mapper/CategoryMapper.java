package com.foods.mapper;

import com.foods.domain.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * Created by jacob on 2017/4/17.
 */
@Mapper
@Component
public interface CategoryMapper {
    @Insert("insert into category (`name`) values (#{name})")
    public int add(Category category);
}
