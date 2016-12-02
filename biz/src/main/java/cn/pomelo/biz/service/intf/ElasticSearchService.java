package cn.pomelo.biz.service.intf;

import java.util.Map;

/**
 * elasticSearch接口
 */

public interface ElasticSearchService {

    /**
     * 指定es _id 插入es记录
     * 
     * @param id elasticsearch _id
     * @param record 待插入记录
     */
    boolean insertRecord(String id, Map record);

    /**
     * 插入es记录
     * 
     * @param record 待插入记录
     */
    boolean insertRecord(Map record);
}
