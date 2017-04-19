package com.foods.spider;

import com.foods.domain.Foods;
import org.json.JSONArray;
import org.json.JSONObject;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jacob on 2017/2/22.
 */
public class FoodsSpider implements PageProcessor {
    private Site site = Site.me().setTimeOut(20000).setRetryTimes(3)
            .setSleepTime(2000).setCharset("gb2312");
    private static boolean isCategoryExisted = false;

    @Override
    public Site getSite() {

        Set<Integer> acceptStatCode = new HashSet<Integer>();
        acceptStatCode.add(200);

        site = site.setAcceptStatCode(acceptStatCode).addHeader("Accept-Encoding", "/")
                .setUserAgent(UserAgentUtils.radomUserAgent());

        return site;
    }

    @Override
    public void process(Page page) {
        StringBuffer description = new StringBuffer();
        StringBuffer nutrition = new StringBuffer();
        StringBuffer value = new StringBuffer();
        if(!isCategoryExisted) {
            // 获取分类及各分类url
            List<String> categories = page.getHtml().xpath("//div[@id='cimenu']/ul/li/a/text()").all();
            page.putField("categories", categories);
            List<String> urls = page.getHtml().xpath("//div[@id='cimenu']/ul/li").links().all();
            page.addTargetRequests(urls);
            isCategoryExisted= true;
        }
        if(this.isListPage(page)) {
            // 获取当前列表页所有食材的url
            List<String> urls = page.getHtml().xpath("//div[@class='baobaoshicai']/ul/li").links().all();
            page.addTargetRequests(urls);

            // 如有分页，添加下一页url
            List<String> items = page.getHtml().xpath("//div[@id='listpages']/ul/li").all();
            for(String item:items) {
                if(item.contains("下一页")) {
                    Html html = Html.create(item);
                    urls = html.xpath("//li").links().all();
                    page.addTargetRequests(urls);
                    break;
                }
            }
        } else {
            // 解析食材页，获取目标数据
            int type = 0;
            Foods foods = new Foods();
            String title = page.getHtml().xpath("//title/text()").get();
            String name = title.substring(0, title.indexOf("的介绍"));
            foods.setName(name);
            String image = "http://www.meishios.com" + page.getHtml().xpath("//img[@id='shicai_f']/@src").get();
            foods.setImage(image);
            foods.setCategory(page.getHtml().xpath("//li[@id='cihover']/a/text()").toString());
            List<String> items = page.getHtml().xpath("//div[@id='text_body']/div").all();
            for(String item:items) {
                Html temp = Html.create(item);
                if(!item.contains("strong")) {
                    if (type == 1) {
                        description.append(temp.xpath("//div/text()").toString());
                    } else if (type == 2) {
                        String table = tableHandler(temp);
                        nutrition.append(table);
                    } else if (type == 3) {
                        value.append(temp.xpath("//div/text()").toString());
                    }
                } else {
                    if(item.contains("基本介绍")) {
                        type = 1;
                    } else if(item.contains("营养成分")) {
                        type = 2;
                    } else if(item.contains("功效")) {
                        type = 3;
                    } else {
                        type = 0;
                    }
                }
            }
            foods.setDescription(description.toString());
            foods.setValue(value.toString());
            foods.setNutrition(nutrition.toString());
            page.putField("foods", foods);
        }
    }

    // 解析食材页的table并转为json
    private String tableHandler(Html html) {
        List<String> contents = html.xpath("//table/tbody/tr/td/allText()").all();
        JSONArray json = new JSONArray();
        for(int i = 0; i < contents.size();++i){
            if(i%2 == 0) {
                JSONObject jo = new JSONObject();
                jo.put("name", contents.get(i));
                json.put(jo);
            } else {
                JSONObject jo = json.getJSONObject(i/2);
                jo.put("value", contents.get(i));
            }
        }
        return json.toString();
    }

    // 判断是否为列表页
    private boolean isListPage(Page page) {
        String url = page.getUrl().toString();
        return url.contains("-");
    }
}
