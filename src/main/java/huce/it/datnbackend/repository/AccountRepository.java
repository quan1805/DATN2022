package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.AccountEntity;
import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    @Query("Select a from AccountEntity a where a.status = 1")
    List<AccountEntity> findAllActive();

    @Query("Select a from AccountEntity a order by a.status desc, a.id asc")
    List<AccountEntity> findAllAccount();

    @Query("select p from AccountEntity p where p.username like %?1% order by p.status desc , p.id asc")
    List<AccountEntity> searchAccountsList(String keyword);
}
