package huce.it.datnbackend.services.account;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IAccountService extends IFunctionService<AccountEntity> {
    @Override
    List<AccountEntity> getAll();

    @Override
    AccountEntity getObjectById(int id);

    @Override
    int insertObject(AccountEntity accountEntity);

    AccountEntity getAccountByUsername(String username);

    @Override
    int updateObject(AccountEntity accountEntity);

    @Override
    int deleteObject(int id);
    @Override
    Paged<AccountEntity> getPage(int pageNumber);
}
