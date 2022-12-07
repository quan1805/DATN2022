package huce.it.datnbackend.services.customer;

import huce.it.datnbackend.model.CustomerEntity;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface ICustomerService extends IFunctionService<CustomerEntity> {
    @Override
    List<CustomerEntity> getAll();

    @Override
    CustomerEntity getObjectById(int id);

    @Override
    int insertObject(CustomerEntity customerEntity);

    @Override
    int updateObject(CustomerEntity customerEntity);

    @Override
    int deleteObject(int id);
}
