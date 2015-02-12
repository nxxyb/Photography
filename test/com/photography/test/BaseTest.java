package com.photography.test;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 相关测试的基类
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring*.xml"})
@TransactionConfiguration(transactionManager="transactionManager",defaultRollback=false)
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class BaseTest {
	
}
