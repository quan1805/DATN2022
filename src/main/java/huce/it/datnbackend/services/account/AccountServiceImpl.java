package huce.it.datnbackend.services.account;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public List<AccountEntity> getAll() {
        return repository.findAllActive();
    }

    @Override
    public AccountEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(AccountEntity accountEntity) {
        repository.save(accountEntity);
        return 0;
    }

    @Override
    public AccountEntity getAccountByUsername(String username) {
        List<AccountEntity> accounts = repository.findAll();
        for(AccountEntity account : accounts){
            if(account.getUsername().equals(username)){
                return account;
            }
        }
        return null;
    }

    @Override
    public int updateObject(AccountEntity accountEntity) {
        repository.save(accountEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        AccountEntity accountEntity = repository.findById(id).get();
        accountEntity.setStatus(0);
        repository.save(accountEntity);
        return 200;
    }

    @Override
    public Paged<AccountEntity> getPage(int pageNumber) {
        return null;
    }
}
