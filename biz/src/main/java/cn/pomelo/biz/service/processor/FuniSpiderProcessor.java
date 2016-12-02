package cn.pomelo.biz.service.processor;

import static cn.pomelo.biz.constant.Constant.RESULT_LIST_MAP;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 页面解析 - 成都透明房产网 <br/>
 * Created by zhengyong on 16/11/30.
 */
@Service("funiSpiderProcessor")
public class FuniSpiderProcessor implements PageProcessor {

    public static Logger logger = LoggerFactory.getLogger(FuniSpiderProcessor.class);

    private Site site = Site.me().setSleepTime(10).setCycleRetryTimes(3).setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");

    @Override
    public void process(Page page) {

        List pagination = page.getHtml().links().regex("/xf/.*").all();

        filterUrl(pagination);
        if (CollectionUtils.isEmpty(pagination)) {
            page.setSkip(true);
            return;
        }

        page.addTargetRequests(pagination);

        // 户型页面判断 http://www.funi.com/xf/xinyuanxindouhui/huxing_63790079-6848-4980-b565-ca9fec440a54
        if (!page.getUrl().regex(".*/huxing_.*").match()) {
            page.setSkip(true);
            return;
        }

        String region = page.getHtml().xpath("//div[@class=pro_crum]/a[3]/text()").toString();
        String name = page.getHtml().xpath("//div[@class=pro_crum]/a[4]/text()").toString();

        List<String> buildings = page.getHtml().xpath("//div[@id=houseList]/dl/dd/div[@class=lptabl]/table/tbody/tr").all();
        if (CollectionUtils.isEmpty(buildings)) {
            page.setSkip(true);
            return;
        }

        List<Map<String, String>> pageList = Lists.newArrayList();

        for (String build : buildings) {
            StringBuilder br = new StringBuilder();
            br.append("<table>").append(build).append("</table>");

            Html html = new Html(br.toString());
            String building = html.xpath("//tr/td[1]/text()").toString();
            String unit = html.xpath("//tr/td[2]/text()").toString();
            String floorNumber = html.xpath("//tr/td[3]/text()").toString();
            String doorNumber = html.xpath("//tr/td[4]/text()").toString();
            String area = html.xpath("//tr/td[5]/text()").toString();
            String houseType = html.xpath("//tr/td[6]/text()").toString();
            String price = html.xpath("//tr/td[7]/text()").toString();

            Map map = Maps.newHashMap();
            map.put("@timestamp", new Timestamp(new Date().getTime()));
            map.put("url", page.getUrl().toString());
            map.put("region", region);
            map.put("name", name);

            map.put("building", building);
            map.put("unit", unit);
            map.put("floorNumber", floorNumber);
            map.put("doorNumber", doorNumber);
            map.put("area", area);
            map.put("houseType", houseType);
            map.put("price", price);

            pageList.add(map);

        }

        if (CollectionUtils.isNotEmpty(pageList)) {
            page.putField(RESULT_LIST_MAP, pageList);
        }
    }

    /**
     * 过滤不需要爬取url
     *
     * @param pagination url列表
     */
    private void filterUrl(List<String> pagination) {

        if (CollectionUtils.isEmpty(pagination)) {
            return;
        }
        for (Iterator iter = pagination.iterator(); iter.hasNext(); ) {
            String url = (String) iter.next();
            if (url.indexOf("detail.htm") != -1 || url.indexOf("house.htm") != -1 || url.indexOf("consult.htm") != -1
                    || url.indexOf("map.htm") != -1 || url.indexOf("photo") != -1 || url.indexOf("news.htm") != -1
                    || url.indexOf("guide.htm") != -1 || url.indexOf("news_official.htm") != -1) {
                iter.remove();
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
