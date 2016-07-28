package com.task.tangyifeng.newnotebookproject.model.model_class.SQLDataBase;

import android.content.Context;
import android.util.Log;

import com.task.tangyifeng.newnotebookproject.model.greenDao_class.AccountBook;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.AccountBookDao;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoMaster;
import com.task.tangyifeng.newnotebookproject.model.greenDao_class.DaoSession;
import com.task.tangyifeng.newnotebookproject.model.model_class.Account;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangyifeng on 16/7/21.
 */
public class AccountDataBase extends DataBase{

    Context context;
    DaoMaster accountDaoMaster;
    DaoMaster.DevOpenHelper accountHelper;
    DaoSession accountSession;
    AccountBookDao accountBookDao;
    QueryBuilder<AccountBook> accountBuilder;

    public AccountDataBase(Context context){
        setContext(context);
        openADataBase();
        accountSession = accountDaoMaster.newSession();
        accountBookDao = accountSession.getAccountBookDao();
    }

    @Override
    public void setContext(Context context){
        this.context = context;
    }

    @Override
    public void openADataBase(){
        Log.d("context",context.toString());
        accountHelper = new DaoMaster.DevOpenHelper(context, "note_book_project_db");
        accountDaoMaster = new DaoMaster(accountHelper.getWritableDatabase());
    }

    @Override
    public List<AccountBook> getAllData(){
        return accountBookDao.loadAll();
    }

    @Override
    public List<Account> getAllEntities(){
        List<AccountBook> accountBooks = getAllData();
        ArrayList<Account> accounts = new ArrayList<>();
        for(AccountBook accountBook : accountBooks){
            if(!accountBook.getIsDel()) {
                Account account = new Account(accountBook.getAccount(), accountBook.getPassword());
                accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public List<AccountBook> queryData(String[] account){
        accountBuilder = accountSession.queryBuilder(AccountBook.class);
        accountBuilder.where(AccountBookDao.Properties.Account.eq(account[0]));
        return accountBuilder.list();
    }

    @Override
    public List<Account> queryEntities(String[] name){
        List<AccountBook> accountBooks = queryData(name);
        ArrayList<Account> accounts = new ArrayList<>();
        for(AccountBook accountBook : accountBooks){
            if(!accountBook.getIsDel()) {
                Account account = new Account(accountBook.getAccount(), accountBook.getPassword());
                accounts.add(account);
            }
        }
        return accounts;
    }

    @Override
    public void updateData(Object object){
        Account account = (Account)object;
        List<AccountBook> accounts = queryData(new String[]{account.getAccount()});
        if(accounts.isEmpty()){
            AccountBook accountBook = new AccountBook(null, account.getAccount(), account.getPassword(), false);
            accountBookDao.insert(accountBook);
        }else{
            AccountBook accountBook = new AccountBook(accounts.get(0).getId(),
                    account.getAccount(), account.getPassword(), false);
            accountBookDao.update(accountBook);
        }
    }

    @Override
    public boolean deleteData(Object object){
        Account account = (Account)object;
        List<AccountBook> accounts = queryData(new String[]{account.getAccount()});
        if(accounts.isEmpty()){
            return false;
        }else{
            AccountBook accountBook = accounts.get(0);
            accountBookDao.delete(accountBook);
            return true;
        }
    }

    @Override
    public void setIsDel(String[] account){
        List<AccountBook> list = queryData(account);
        AccountBook accountBook = new AccountBook(list.get(0).getId(), list.get(0).getAccount(), list.get(0).getPassword(), true);
        accountBookDao.update(accountBook);
    }

    @Override
    public List<AccountBook> getAllDels(){
        accountBuilder = accountSession.queryBuilder(AccountBook.class);
        accountBuilder.where(AccountBookDao.Properties.IsDel.eq(true));
        return accountBuilder.list();
    }

    @Override
    public int deleteAllDel(){
        List<AccountBook> dels;
        int count = 0;
        dels = getAllDels();
        for(AccountBook accountBook : dels){
            Account acc = new Account(accountBook.getAccount(), accountBook.getPassword());
            deleteData(acc);
            count ++;
        }
        return count;
    }
}
