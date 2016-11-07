package com.little.service;

import com.core.support.BaseHibernateDao;
import com.little.model.domain.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author pengx
 * @date 2016/11/3
 */

@Service
public class TestService {

    @Autowired
    private BaseHibernateDao hibernateDao;

    /**
     * 获取一条test表记录
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public Test getRecord(int id){

        return hibernateDao.get(Test.class,id);
    }
}
