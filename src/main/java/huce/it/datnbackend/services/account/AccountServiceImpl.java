package huce.it.datnbackend.services.account;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class AccountServiceImpl implements IAccountService {

    @Autowired
    private AccountRepository repository;

    @Override
    public List<AccountEntity> getAll() {
        return repository.findAllAccount();
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
        AccountEntity account = repository.findById(accountEntity.getId()).get();
        accountEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        accountEntity.setStatus(account.getStatus());
        accountEntity.setCreatedDate(account.getCreatedDate());
        repository.save(accountEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        AccountEntity accountEntity = repository.findById(id).get();
        accountEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        accountEntity.setStatus(0);
        repository.save(accountEntity);
        return 200;
    }

    @Override
    public int enableObject(int id) {
        AccountEntity accountEntity = repository.findById(id).get();
        accountEntity.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        accountEntity.setStatus(1);
        repository.save(accountEntity);
        return 200;
    }

    @Override
    public Paged<AccountEntity> getPage(int pageNumber) {
        return null;
    }

    @Override
    public Page<AccountEntity> pageAccounts(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<AccountEntity> accounts = repository.findAllAccount();
        Page<AccountEntity> accountPage =  toPage(accounts, pageable);
        return accountPage;
    }

    @Override
    public Page<AccountEntity> searchAccounts(int pageNo, String keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<AccountEntity> accountsList = repository.searchAccountsList(keyword);
        Page<AccountEntity> accounts = toPage(accountsList, pageable);
        return accounts;
    }

    private Page toPage(List<AccountEntity> list, Pageable pageable){
        if (pageable.getOffset() >= list.size()){
            return Page.empty();
        }
        int startIndex = (int) pageable.getOffset();
        int endIndex = ((pageable.getOffset() + pageable.getPageSize()) > list.size())
                ? list.size()
                : (int) (pageable.getOffset() + pageable.getPageSize());
        List subList = list.subList(startIndex, endIndex);
        return new PageImpl(subList, pageable, list.size());
    }
}
