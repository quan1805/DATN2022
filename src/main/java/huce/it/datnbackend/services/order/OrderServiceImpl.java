package huce.it.datnbackend.services.order;

import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.*;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.paging.Paging;
import huce.it.datnbackend.repository.OrderDetailRepository;
import huce.it.datnbackend.repository.OrderRepository;
import huce.it.datnbackend.repository.OrderShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Service
public class OrderServiceImpl implements IOrderService{

    @Autowired
    private OrderRepository repository;
    @Autowired
    private OrderDetailRepository detailRepository;

    @Autowired
    private OrderShopRepository orderShopRepository;

    @Override
    public List<OrderDto> findAll() {
        List<OrderEntity> orders = repository.findAllOrder();
        List<OrderDto> orderDtoList = transfer(orders);
        return orderDtoList;
    }

    @Override
    public OrderDto getById(int id) {
        OrderEntity order = repository.getById(id);
        OrderDto orderDto = new OrderDto();
        orderDto.setTotal(order.getTotal());
        orderDto.setId(order.getId());
        orderDto.setCustomer(order.getCustomer());
//        orderDto.setBrand(order.getBrand());
//        orderDto.setDeliveryDate(order.getDeliveryDate());
        orderDto.setDiscount(order.getDiscount());
        orderDto.setNote(order.getNote());
        orderDto.setCreateDate(order.getCreateDate());
        orderDto.setUpdateDate(order.getUpdateDate());
        orderDto.setStatus(order.getStatus());
        return orderDto;
    }

    @Override
    public void cancelOrder(int id) {
        OrderEntity order = repository.getById(id);
        order.setUpdateDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus(5);
        repository.save(order);
    }

    @Override
    public List<OrderEntity> getOrderByCustomer(int id) {
        return repository.findByCustomer(id);
    }


    @Override
    public OrderEntity saveOrder(ShoppingCartEntity shoppingCart) {
        OrderEntity order = new OrderEntity();
        order.setCustomer(shoppingCart.getCustomer());
        order.setDiscount(0);
        order.setCreateDate(new Timestamp(System.currentTimeMillis()));
        order.setStatus(0);
        order.setTotal(shoppingCart.getTotalPrice());
        order = repository.save(order);
        for (CartShopEntity cartShop : shoppingCart.getCartShops()){
            OrderShopEntity orderShop = new OrderShopEntity();
            orderShop.setBrand(cartShop.getBrand());
            orderShop.setOrder(order);
            orderShop.setDiscount(0);
            orderShop.setStatus(1);
            orderShop.setCreateDate(new Timestamp(System.currentTimeMillis()));
            orderShop.setTotal(cartShop.getTotalPrice());
            orderShop =  orderShopRepository.save(orderShop);
            for (CartItemEntity cartItem : cartShop.getCartItems()){
                OrderDetailEntity orderDetail = new OrderDetailEntity();
                orderDetail.setProductDetail(cartItem.getProductDetail());
                orderDetail.setPrice(cartItem.getTotalPrice());
                orderDetail.setQuantity(cartItem.getQuantity());
                orderDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));
                orderDetail.setOrderShop(orderShop);
                orderDetail = detailRepository.save(orderDetail);
            }
        }
        return order;
    }

    @Override
    public Page<OrderDto> pageOrders(int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<OrderDto> orders = transfer(repository.findAllOrder());
        Page<OrderDto> orderPages =  toPage(orders, pageable);
        return orderPages;
    }

    @Override
    public Page<OrderDto> searchOrders(int pageNo, int keyword) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<OrderDto> orderDtoList = transfer(repository.searchOrdersList(keyword));
        Page<OrderDto> orders = toPage(orderDtoList, pageable);
        return orders;
    }


    private List<OrderDto> transfer(List<OrderEntity>orders){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for (OrderEntity order :  orders){
            OrderDto orderDto = new OrderDto();
            orderDto.setTotal(order.getTotal());
            orderDto.setId(order.getId());
            orderDto.setCustomer(order.getCustomer());
//            orderDto.setBrand(order.getBrand());
//            orderDto.setDeliveryDate(order.getDeliveryDate());
            orderDto.setDiscount(order.getDiscount());
            orderDto.setNote(order.getNote());
            orderDto.setCreateDate(order.getCreateDate());
            orderDto.setUpdateDate(order.getUpdateDate());
            orderDto.setStatus(order.getStatus());
            orderDtoList.add(orderDto);
        }
        return orderDtoList;
    }

    private Page toPage(List<OrderDto> list, Pageable pageable){
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


    //---------------------------------------------------------------------
    @Override
    public List<OrderEntity> getAll() {
        return repository.findAll();
    }

    @Override
    public OrderEntity getObjectById(int id) {
        return repository.findById(id).get();

    }

    @Override
    public int insertObject(OrderEntity orderEntity) {
        repository.save(orderEntity);
        return 0;
    }

    @Override
    public int updateObject(OrderEntity orderEntity) {
        repository.save(orderEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        OrderEntity orderEntity = repository.findById(id).get();
        orderEntity.setStatus(0);
        repository.save(orderEntity);
        return 200;
    }

    @Override
    public Paged<OrderEntity> getPage(int pageNumber) {
        Page<OrderEntity> postPage = repository.findAllActive(PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE));
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
    }

    @Override
    public Paged<OrderEntity> getPage(int status, int pageNumber) {
        Page<OrderEntity> postPage = repository.findAllOrderByStatus(status, PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE));
        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
    }

    @Override
    public Paged<OrderEntity> getPage( int pageNumber, BrandEntity brand) {
//        Page<OrderEntity> postPage = repository.findAllOrderByBrand(PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE), brand );
//        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
        return null;
    }

    @Override
    public Paged<OrderEntity> getPage(int status, int pageNumber, BrandEntity brand) {
//        Page<OrderEntity> postPage = repository.findAllOrderByStatusBrand(status, PageRequest.of(pageNumber - 1, Paging.PAGE_SIZE), brand );
//        return new Paged<>(postPage, Paging.of(postPage.getTotalPages(), pageNumber, Paging.PAGE_SIZE));
        return null;
    }

}
