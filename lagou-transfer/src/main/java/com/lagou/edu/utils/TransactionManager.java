package com.lagou.edu.utils;


import com.ispring.context.annotation.*;

import java.sql.SQLException;

/**
 * @author hetengjiao
 *
 * 事务管理器类：负责手动事务的开启、提交、回滚
 */

@Component("transactionManager")
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    // 开启手动事务控制
    @Before("beginTransaction")
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    @After("commit")
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    @AfterThrowing("rollback")
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
