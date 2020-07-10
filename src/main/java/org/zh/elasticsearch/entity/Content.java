package org.zh.elasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content implements Serializable {

    private static final long serialVersionUID = -2113287554505847851L;
    private String title;
    private String img;
    private String price;

}
