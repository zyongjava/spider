package cn.pomelo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhengyong on 16/11/30.
 */
@Controller
public class Monitor {

    @RequestMapping(value = "/ok")
    @ResponseBody
    public String ok() {
        return "ok";
    }

    @RequestMapping(value = "/home")
    public String home() {
        return "home";
    }

}
