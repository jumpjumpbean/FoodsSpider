package com.foods;

import com.foods.spider.CustomPipeline;
import com.foods.spider.FoodsSpider;
import com.foods.spider.SpringContextUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import us.codecraft.webmagic.Spider;

@SpringBootApplication
public class FoodsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(FoodsApplication.class, args);
		SpringContextUtil.setApplicationContext(applicationContext);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("----开始执行食材单次任务");

		Spider spider = Spider.create(new FoodsSpider())
				.addUrl("http://www.meishios.com/shicai/3-1.html")
				.addPipeline(new CustomPipeline())
				.thread(5)
				.setExitWhenComplete(true);

		spider.start();
		spider.stop();
	}
}
