package com.little;

import com.little.model.domain.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author pengx
 * @date 2016/11/16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring/appContext-core.xml" })
public class HibernateTest {
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Test
    public void getUser(){
        User user = hibernateTemplate.get(User.class,"1");
        System.out.println(user);
    }

}
