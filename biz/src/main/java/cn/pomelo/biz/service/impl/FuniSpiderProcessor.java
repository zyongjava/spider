package cn.pomelo.biz.service.impl;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 成都透明房产网
 * Created by zhengyong on 16/11/30.
 */
@Service("funiSpiderProcessor")
public class FuniSpiderProcessor implements PageProcessor {

    public static Logger         logger = LoggerFactory.getLogger(FuniSpiderProcessor.class);

    private Site                 site   = Site.me().setSleepTime(10).setCycleRetryTimes(3).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public void process(Page page) {

        List pagination = page.getHtml().links().regex(".*loupan/region.*").all();
        page.addTargetRequests(pagination);

        String region = page.getHtml().xpath("//div[@class=house-search]/div[@class=s-con]/dl/dd/a[@class=on]/text()").toString();

        List<String> buildings = page.getHtml().xpath("//div[@class=maplist]/dl").all();

        if (CollectionUtils.isEmpty(buildings)) {
            logger.info("page={}", page.getUrl().toString());
            return;
        }

        for (String building : buildings) {
            Html html = new Html(building);
            String name = html.xpath("//dt[@class=clearfix]/h2/a/text()").toString();
            String location = html.xpath("//dt[@class=clearfix]/p[@class=p_map]/i/text()").toString();
            String price = html.xpath("//dt[@class=clearfix]/strong[@class=h-price]/i[@class=f28]/text()").toString();

            Map map = Maps.newHashMap();
            map.put("@timestamp", new Timestamp(new Date().getTime()));
            map.put("url", page.getUrl().toString());
            map.put("region", region);
            map.put("name", name);
            map.put("location", location);
            map.put("price", price);

            elasticSearchService.insertRecord(map);
            logger.info(JSON.toJSONString(map));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }
}
