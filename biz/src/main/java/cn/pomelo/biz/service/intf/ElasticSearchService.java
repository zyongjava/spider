package cn.pomelo.biz.service.intf;

import java.util.Map;

/**
 * elasticSearch接口
 */

public interface ElasticSearchService {

    /**
     * @param id elasticsearch _id
     * @param map
     */
    void insertRecord(String id, Map map);
}
