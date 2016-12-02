package cn.pomelo.biz.service.intf;

/**
 * 爬虫服务<br/>
 * Created by zhengyong on 16/11/30.
 */
public interface SpiderService {

    /**
     * 爬取接口
     * 
     * @param url 带爬取url路径
     */
    void crawl(String url);

}
