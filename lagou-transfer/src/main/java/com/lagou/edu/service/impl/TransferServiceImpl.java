package com.lagou.edu.service.impl;


import com.ispring.context.annotation.Autowired;
import com.ispring.context.annotation.Service;
import com.ispring.context.annotation.Transactional;
import com.lagou.edu.dao.AccountDao;
import com.lagou.edu.pojo.Account;
import com.lagou.edu.service.TransferService;

/**
 * @author hetengjiao
 */

@Service("transferService")
@Transactional()
public class TransferServiceImpl implements TransferService {

    // 最佳状态
    @Autowired
    private AccountDao accountDao;

    @Override
    public void transfer(String fromCardNo, String toCardNo, int money) throws Exception {
        System.out.println("start");
        Account from = accountDao.queryAccountByCardNo(fromCardNo);
        Account to = accountDao.queryAccountByCardNo(toCardNo);

        from.setMoney(from.getMoney()-money);
        to.setMoney(to.getMoney()+money);

        accountDao.updateAccountByCardNo(to);
        //int c = 1/0;
        accountDao.updateAccountByCardNo(from);
        System.out.println("end");
    }
}
