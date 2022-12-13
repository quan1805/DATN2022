package huce.it.datnbackend.services.order;

import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IOrderService extends IFunctionService<OrderEntity> {
    @Override
    List<OrderEntity> getAll();

    @Override
    OrderEntity getObjectById(int id);

    @Override
    int insertObject(OrderEntity orderEntity);

    @Override
    int updateObject(OrderEntity orderEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<OrderEntity> getPage(int pageNumber);
}
