package huce.it.datnbackend.services.orderdetail;

import huce.it.datnbackend.dto.OrderDetailDto;
import huce.it.datnbackend.dto.ProductDto;
import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.OrderDetailRepository;
import huce.it.datnbackend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class OrderDetailServiceImpl implements IOrderDetailService{

    @Autowired
    private OrderDetailRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<OrderDetailEntity> getAll() {
        return repository.findAllActive();
    }

    @Override
    public List<OrderDetailEntity> getObjectByOrderId(int orderId) {
        return  repository.findAllByOrderId(orderId);
    }

    @Override
    public List<OrderDetailDto> getOrderDetailByOrderId(int id) {
        List<OrderDetailEntity> orderDetails = repository.findAllByOrderId(id);
        List<OrderDetailDto> orderDetailDtoList = transfer(orderDetails);
        return orderDetailDtoList;
    }
    private List<OrderDetailDto> transfer(List<OrderDetailEntity> orderDetails){
        List<OrderDetailDto> orderDetailDtoList = new ArrayList<>();
        for (OrderDetailEntity orderDetail :  orderDetails){
            OrderDetailDto orderDetailDto = new OrderDetailDto();
            orderDetailDto.setId(orderDetail.getId());
            orderDetailDto.setProductDetail(orderDetail.getProductDetail());
            orderDetailDto.setPrice(orderDetail.getPrice());
            orderDetailDto.setQuantity(orderDetail.getQuantity());
            orderDetailDto.setOrderShop(orderDetail.getOrderShop());
            orderDetailDto.setStatus(orderDetail.getStatus());
            orderDetailDto.setCreateDate(orderDetail.getCreateDate());
            orderDetailDto.setUpdateDate(orderDetail.getUpdateDate());
            orderDetailDto.setTotal(orderDetail.getPrice() * orderDetail.getQuantity());
            orderDetailDto.setProduct(orderDetail.getProductDetail().getProduct());
            orderDetailDtoList.add(orderDetailDto);
        }
        return orderDetailDtoList;
    }


    @Override
    public OrderDetailEntity getObjectById(int id) {
        return repository.findById(id).get();
    }

    @Override
    public int insertObject(OrderDetailEntity orderDetailEntity) {
        repository.save(orderDetailEntity);
        return 0;
    }

    @Override
    public int updateObject(OrderDetailEntity orderDetailEntity) {
        repository.save(orderDetailEntity);
        return 200;
    }

    @Override
    public int deleteObject(int id) {
        OrderDetailEntity orderDetailEntity = repository.findById(id).get();
        orderDetailEntity.setStatus(0);
        repository.save(orderDetailEntity);
        return 200;
    }

    @Override
    public Paged<OrderDetailEntity> getPage(int pageNumber) {
        return null;
    }
}
