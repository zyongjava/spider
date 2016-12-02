package cn.pomelo.controller;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import cn.pomelo.biz.service.intf.SpiderService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhengyong on 16/11/30.
 */
@Controller
public class Monitor {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Autowired
    private SpiderService        spiderService;

    @RequestMapping(value = "/ok")
    @ResponseBody
    public String ok() {
        return "ok";
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }

    @RequestMapping(value = "/es")
    @ResponseBody
    public String es(String name) {
        Map map = Maps.newHashMap();
        map.put("name", "网页" + name);
        map.put("@time", new Date());
        map.put("@timestamp", new Timestamp(new Date().getTime()));
        map.put("age", Math.floor(Math.random() * 10));
        elasticSearchService.insertRecord(null, map);
        return "success";
    }

    // http://www.funi.com/loupan/region_56_0_0_0_1
    @RequestMapping(value = "/spider")
    @ResponseBody
    public String spider(String url) {
        spiderService.crawl(url);
        return "spider start";
    }
}
