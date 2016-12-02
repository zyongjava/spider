package cn.pomelo.biz.service.impl;

import java.net.InetAddress;
import java.util.Map;

import cn.pomelo.biz.service.intf.ElasticSearchService;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * elasticsearch 操作类<br/>
 * Created by zhengyong on 16/11/22.
 */
@Service("elasticSearchService")
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchServiceImpl implements ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchServiceImpl.class);

    private TransportClient     client;
    /**
     * es index
     */
    private String              index;
    /**
     * es type
     */
    private String              type;

    private String              host;
    private Integer             port;
    private String              clusterName;

    @PostConstruct
    public void init() throws Exception {

        Preconditions.checkNotNull(index, "elasticsearch index must not be null");

        Preconditions.checkNotNull(type, "elasticsearch type must not be null");

        Preconditions.checkNotNull(host, "elasticsearch host must not be null");

        Preconditions.checkNotNull(port, "elasticsearch port must not be null.");

        Preconditions.checkNotNull(clusterName, "elasticsearch clusterName must not be null");

        if (client == null) {
            synchronized (this) {
                Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
                client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host),
                                                                                                                                 port));
            }
        }
        Preconditions.checkNotNull(client, "create elasticsearch client failed");

    }

    @PreDestroy
    private void shutdown() {
        if (client != null) {
            client.close();
        }
    }

    private boolean insert(String id, Map source) {

        Preconditions.checkNotNull(source, "source must not be null.");

        logger.info("insert data : {}", JSON.toJSONString(source));

        IndexRequestBuilder builder = client.prepareIndex(index, type).setSource(source);
        if (StringUtils.isNotBlank(id)) {
            builder.setId(id);
        }
        IndexResponse response = builder.get();

        logger.info("insert response : {}", response.toString());

        return response.isCreated();
    }

    @Override
    public boolean insertRecord(String id, Map record) {
        return insert(id, record);
    }

    @Override
    public boolean insertRecord(Map record) {
        return insert(null, record);
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

}
