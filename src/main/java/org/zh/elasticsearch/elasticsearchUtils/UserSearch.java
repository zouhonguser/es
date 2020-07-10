package org.zh.elasticsearch.elasticsearchUtils;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.zh.elasticsearch.entity.User;


public interface UserSearch extends ElasticsearchRepository<User, String> {

}