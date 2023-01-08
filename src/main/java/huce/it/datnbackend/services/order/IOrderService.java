package huce.it.datnbackend.services.order;

import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.model.ShoppingCartEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;
import org.hibernate.criterion.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IOrderService extends IFunctionService<OrderEntity> {
    List<OrderDto> findAll();
    OrderDto getById(int id);
    void cancelOrder(int id);

    List<OrderEntity> getOrderByCustomer(int id);

    OrderEntity saveOrder(ShoppingCartEntity shoppingCart);
    Page<OrderDto> pageOrders(int pageNo);
    Page<OrderDto> searchOrders(int pageNo, int keyword);
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

    Paged<OrderEntity> getPage(int status, int pageNumber);
    Paged<OrderEntity> getPage(int pageNumber, BrandEntity brand);
    Paged<OrderEntity> getPage(int status, int pageNumber, BrandEntity brand);
}
