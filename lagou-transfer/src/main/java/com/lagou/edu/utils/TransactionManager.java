package com.lagou.edu.utils;



import com.ispring.context.annotation.Autowired;
import com.ispring.context.annotation.Component;
import org.aspectj.lang.annotation.*;

import java.sql.SQLException;

/**
 * @author hetengjiao
 *
 * 事务管理器类：负责手动事务的开启、提交、回滚
 */

@Component("transactionManager")
@Aspect
public class TransactionManager {

    @Autowired
    private ConnectionUtils connectionUtils;

    // 开启手动事务控制
    @Pointcut("execution(* com.lagou.edu.service.impl.TransferServiceImpl.*(..))")
    public void pt1() {

    }


    @Before("pt1()")
    public void beginTransaction() throws SQLException {
        connectionUtils.getCurrentThreadConn().setAutoCommit(false);
    }


    // 提交事务
    @AfterReturning("pt1()")
    public void commit() throws SQLException {
        connectionUtils.getCurrentThreadConn().commit();
    }


    // 回滚事务
    @AfterThrowing("pt1()")
    public void rollback() throws SQLException {
        connectionUtils.getCurrentThreadConn().rollback();
    }
}
