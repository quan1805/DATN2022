package huce.it.datnbackend.services.ordershop;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.dto.OrderDto;
import huce.it.datnbackend.dto.OrderShopDto;
import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.OrderDetailRepository;
import huce.it.datnbackend.repository.OrderShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderShopServiceImpl implements IOrderShopService{

    @Autowired
    private OrderShopRepository repository;

    @Override
    public List<OrderShopEntity> getAll() {
        return null;
    }

    @Override
    public Page<OrderShopDto> pageOrdersBrand(int pageNo, int id) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<OrderShopDto> orders = transfer(repository.findAllByBrandId(id));
        Page<OrderShopDto> orderPages =  toPage(orders, pageable);
        return orderPages;
    }

    @Override
    public OrderShopDto getObjectDtoById(int id) {
        OrderShopEntity orderShop = repository.getById(id);
        OrderShopDto orderShopDto = new OrderShopDto();
        orderShopDto.setId(orderShop.getId());
        orderShopDto.setBrand(orderShop.getBrand());
        orderShopDto.setOrder(orderShop.getOrder());
        orderShopDto.setMessage(orderShop.getMessage());
        orderShopDto.setDiscount(orderShop.getDiscount());
        orderShopDto.setNote(orderShop.getNote());
        orderShopDto.setStatus(orderShop.getStatus());
        orderShopDto.setUpdateDate(orderShop.getUpdateDate());
        orderShopDto.setCreateDate(orderShop.getCreateDate());
        orderShopDto.setDeliveryDate(orderShop.getDeliveryDate());
        orderShopDto.setTotal(orderShop.getTotal());
        return orderShopDto;

    }

    @Override
    public Page<OrderShopDto> searchOrders(int pageNo, int keyword, int id) {
        Pageable pageable = PageRequest.of(pageNo, 6);
        List<OrderShopDto> orderDtoList = transfer(repository.searchOrdersBrandList(keyword, id));
        Page<OrderShopDto> orders = toPage(orderDtoList, pageable);
        return orders;
    }

    @Override
    public List<OrderShopDto> getObjectByOrderId(int id) {
        List<OrderShopEntity> orderShops = repository.findAllByOrderId(id);
        List<OrderShopDto> orderShopDtoList = transfer(orderShops);
        return  orderShopDtoList;
    }

    private List<OrderShopDto> transfer(List<OrderShopEntity> orderShops){
        List<OrderShopDto> orderShopDtoList = new ArrayList<>();
        for (OrderShopEntity orderShop :  orderShops){
            OrderShopDto orderShopDto = new OrderShopDto();
            orderShopDto.setId(orderShop.getId());
            orderShopDto.setBrand(orderShop.getBrand());
            orderShopDto.setOrder(orderShop.getOrder());
            orderShopDto.setMessage(orderShop.getMessage());
            orderShopDto.setDiscount(orderShop.getDiscount());
            orderShopDto.setNote(orderShop.getNote());
            orderShopDto.setStatus(orderShop.getStatus());
            orderShopDto.setUpdateDate(orderShop.getUpdateDate());
            orderShopDto.setCreateDate(orderShop.getCreateDate());
            orderShopDto.setDeliveryDate(orderShop.getDeliveryDate());
            orderShopDto.setTotal(orderShop.getTotal());
            orderShopDtoList.add(orderShopDto);
        }
        return orderShopDtoList;
    }

    private Page toPage(List<OrderShopDto> list, Pageable pageable){
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

    @Override
    public OrderShopEntity getObjectById(int id) {
        return repository.getById(id);
    }


    @Override
    public int insertObject(OrderShopEntity orderShopEntity) {
        return 0;
    }

    @Override
    public int updateObject(OrderShopEntity orderShopEntity) {
        repository.save(orderShopEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        return 0;
    }

    @Override
    public Paged<OrderShopEntity> getPage(int pageNumber) {
        return null;
    }
}
