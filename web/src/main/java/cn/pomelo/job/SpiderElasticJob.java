package cn.pomelo.job;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

import java.util.Date;
import java.util.Map;

/**
 * elastic-job
 */
@Component
public class SpiderElasticJob extends AbstractSimpleElasticJob {

    private static final Logger  logger = LoggerFactory.getLogger(SpiderElasticJob.class);

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Override
    public void process(JobExecutionMultipleShardingContext context) {
        try {
            Map map = Maps.newHashMap();
            map.put("name", "定时器");
            map.put("time", new Date());
            map.put("age", 20);
            elasticSearchService.insertRecord(map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
