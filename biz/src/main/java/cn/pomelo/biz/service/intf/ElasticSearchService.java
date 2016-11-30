package cn.pomelo.biz.service.intf;

import com.alibaba.fastjson.JSONObject;

/**
 * elasticSearch接口
 */

public interface ElasticSearchService {

    /**
     * @param jsonObject
     */
    void insertRecord(JSONObject jsonObject);
}
