package huce.it.datnbackend.services.customer;

import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements ICustomerService{

    @Autowired
    private CustomerRepository repository;

    @Override
    public List<CustomerEntity> getAll() {
//        return repository.findAll();
        return repository.findAllActive();
    }

    @Override
    public CustomerEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(CustomerEntity customerEntity) {
        repository.save(customerEntity);
        return 0;
    }


    @Override
    public int updateObject(CustomerEntity customerEntity) {
        repository.save(customerEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        CustomerEntity customerEntity = repository.findById(id).get();
        customerEntity.setStatus(0);
        repository.save(customerEntity);
        return 200;
    }

    @Override
    public Paged<CustomerEntity> getPage(int pageNumber) {
        return null;
    }
}
