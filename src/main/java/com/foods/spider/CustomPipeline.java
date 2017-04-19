package com.foods.spider;

import com.foods.domain.Category;
import com.foods.mapper.CategoryMapper;
import com.foods.mapper.FoodsMapper;
import com.foods.domain.Foods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by jacob on 2017/2/22.
 */


@Repository
public class CustomPipeline implements Pipeline {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public CustomPipeline() {
    }

    public void process(ResultItems resultItems, Task task) {
        logger.info("get page: " + resultItems.getRequest().getUrl());
        // 多线程通过context获取bean
        FoodsMapper foodsMapper = (FoodsMapper) SpringContextUtil.getBean("foodsMapper");
        CategoryMapper categoryMapper = (CategoryMapper) SpringContextUtil.getBean("categoryMapper");

        Iterator var3 = resultItems.getAll().entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry entry = (Map.Entry)var3.next();

            if(entry.getKey().equals("foods")) {
                foodsMapper.add((Foods)entry.getValue());
            } else if(entry.getKey().equals("categories")) {
                List<String> categories = (List<String>)entry.getValue();
                for(String item:categories) {
                    Category category = new Category();
                    category.setName(item);
                    categoryMapper.add(category);
                }
            }
        }
    }
}
