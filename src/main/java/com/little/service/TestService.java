package com.little.service;

import com.little.model.domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;

/**
 * @author pengx
 * @date 2016/11/3
 */

@Service
public class TestService {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    /**
     * 获取一条test表记录
     * @param id
     * @return
     */
    public Test getRecord(int id){

        return hibernateTemplate.get(Test.class,id);
    }
}
