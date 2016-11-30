package cn.pomelo.biz.service.impl;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhengyong on 16/11/30.
 */
@Service("elasticSearchService")
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    @Override
    public void insertRecord(JSONObject jsonObject) {
        // TODO
        logger.info(jsonObject.toString());
    }
}
