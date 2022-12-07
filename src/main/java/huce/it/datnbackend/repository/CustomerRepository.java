package huce.it.datnbackend.repository;

import huce.it.datnbackend.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    @Query("Select a from CustomerEntity a where a.status = 1")
    List<CustomerEntity> findAllActive();
}
