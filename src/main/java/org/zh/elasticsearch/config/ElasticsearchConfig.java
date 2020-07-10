package org.zh.elasticsearch.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    /*@Bean
    public RestClient restClient(){

        RestClientBuilder clientBuilder = RestClient.builder(new HttpHost("127.0.0.1", 9200));
        //配置身份验证
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));//需es开启
//        clientBuilder.setHttpClientConfigCallback(
//                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        //设置连接超时和套接字超时
        clientBuilder.setRequestConfigCallback(builder -> builder.setConnectTimeout(10000).setSocketTimeout(60000));
        //设置节点选择器
        clientBuilder.setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS);
        //配置HTTP异步请求ES的线程数
        clientBuilder.setHttpClientConfigCallback(
                httpAsyncClientBuilder -> httpAsyncClientBuilder.setDefaultIOReactorConfig(
                        IOReactorConfig.custom().setIoThreadCount(1).build()));
        //设置监听器，每次节点失败都可以监听到，可以作额外处理
        clientBuilder.setFailureListener(new RestClient.FailureListener() {
            @Override
            public void onFailure(Node node) {
                super.onFailure(node);
                System.out.println(node.getHost()+"----------失败");
            }
        });
        return clientBuilder.build();
    }*/


    @Bean
    public RestHighLevelClient restHighLevelClient(){
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("127.0.0.1", 9200, "http")));
    }


}
