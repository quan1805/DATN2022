package huce.it.datnbackend.services.orderdetail;

import huce.it.datnbackend.model.OrderDetailEntity;
import huce.it.datnbackend.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderDetailServiceImpl implements IOrderDetailService{

    @Autowired
    private OrderDetailRepository repository;

    @Override
    public List<OrderDetailEntity> getAll() {
        return null;
    }

    @Override
    public OrderDetailEntity getObjectById(int id) {
        return null;
    }

    @Override
    public int insertObject(OrderDetailEntity orderDetailEntity) {
        return 0;
    }

    @Override
    public int updateObject(OrderDetailEntity orderDetailEntity) {
        return 0;
    }

    @Override
    public int deleteObject(int id) {
        return 0;
    }
}
