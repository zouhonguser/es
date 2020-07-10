package org.zh.elasticsearch.entity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;

@Document(indexName = "test",type = "users")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = -9060369374478982491L;

    @Id
    private String Id;
    private String name;
    private Integer age;


}
