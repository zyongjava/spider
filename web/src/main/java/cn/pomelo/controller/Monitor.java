package cn.pomelo.controller;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhengyong on 16/11/30.
 */
@Controller
public class Monitor {

    @Autowired
    private ElasticSearchService elasticSearchService;

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
        map.put("name", name);
        map.put("time", new Date());
        map.put("age", 10);
        elasticSearchService.insertRecord(map);
        return "success";
    }
}
