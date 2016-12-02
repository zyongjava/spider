package cn.pomelo.biz.service.pipeline;

import static cn.pomelo.biz.constant.Constant.RESULT_LIST_MAP;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import cn.pomelo.biz.utils.MD5Util;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;
import java.util.Map;

/**
 * 结果列表写入ElasticSearch<br/>
 * Created by zhengyong on 16/12/2.
 */
@Service("elasticSearchPipeline")
public class ElasticSearchPipeline implements Pipeline {

    public static Logger logger = LoggerFactory.getLogger(ElasticSearchPipeline.class);

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        Map map = resultItems.getAll();
        Object object = map.get(RESULT_LIST_MAP);
        if (object == null) {
            return;
        }
        List<Map<String, String>> listMap = (List) object;
        if (CollectionUtils.isEmpty(listMap)) {
            return;
        }

        for (Map mapResult : listMap) {
            String id = ganerateId(mapResult);
            elasticSearchService.insertRecord(id, mapResult);
            logger.info(JSON.toJSONString(mapResult));
        }
    }

    /**
     * 生成es _id
     *
     * @param map
     * @return _id
     */
    private String ganerateId(Map map) {
        Map idMap = Maps.newHashMap();
        idMap.putAll(map);
        idMap.remove("@timestamp");
        idMap.remove("url");
        String id = MD5Util.md5(JSON.toJSONString(idMap));
        return id;
    }
}
