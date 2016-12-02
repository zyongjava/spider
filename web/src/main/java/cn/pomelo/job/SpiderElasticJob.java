package cn.pomelo.job;

import cn.pomelo.biz.service.intf.SpiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;

/**
 * 定时器 elastic-job
 */
@Component
public class SpiderElasticJob extends AbstractSimpleElasticJob {

    private static final Logger logger = LoggerFactory.getLogger(SpiderElasticJob.class);

    @Autowired
    private SpiderService       spiderService;

    @Override
    public void process(JobExecutionMultipleShardingContext context) {
        try {

            // TODO 定时器占时关闭, 后续开发
            spiderService.crawl("http://www.funi.com/loupan/region_56_0_0_0_1");

            logger.info("spider elastic job execute.");

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
