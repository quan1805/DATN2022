package huce.it.datnbackend.services.orderdetail;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IOrderDetailService extends IFunctionService<OrderDetailEntity> {
    @Override
    List<OrderDetailEntity> getAll();

    List<OrderDetailEntity> getObjectByOrderId(int id);
    List<OrderDetailDto> getOrderDetailByOrderId(int id);

    @Override
    OrderDetailEntity getObjectById(int id);

    @Override
    int insertObject(OrderDetailEntity orderDetailEntity);

    @Override
    int updateObject(OrderDetailEntity orderDetailEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<OrderDetailEntity> getPage(int pageNumber);
}
