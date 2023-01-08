package huce.it.datnbackend.services.ordershop;

import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.OrderShopDto;
import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
public interface IOrderShopService extends IFunctionService<OrderShopEntity> {
    @Override
    List<OrderShopEntity> getAll();
    Page<OrderShopDto> pageOrdersBrand(int pageNo, int id);
    Page<OrderShopDto> searchOrders(int pageNo, int keyword, int id);


    List<OrderShopDto> getObjectByOrderId(int id);

    @Override
    OrderShopEntity getObjectById(int id);

    OrderShopDto getObjectDtoById(int id);

    @Override
    int insertObject(OrderShopEntity orderShopEntity);

    @Override
    int updateObject(OrderShopEntity orderShopEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<OrderShopEntity> getPage(int pageNumber);
}
