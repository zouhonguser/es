package org.zh.elasticsearch.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zh.elasticsearch.service.EsService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class EsController {

    @Autowired
    EsService esService;

    @RequestMapping("/create-index")
    public void create() throws IOException {
        esService.createJdIndex();
    }


    @RequestMapping("/save/{keywords}")
    public Boolean saveKeyWords(@PathVariable String keywords) throws IOException {

        return esService.saveInfo(keywords);
    }

    @RequestMapping("/search/{keywords}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> searchPage(@PathVariable String keywords,
                                                  @PathVariable Integer pageNo,
                                                  @PathVariable Integer pageSize) throws IOException {
        return esService.searchPage(keywords,pageNo,pageSize);
    }

}
