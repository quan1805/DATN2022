package huce.it.datnbackend.services.order;

import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class OrderServiceImpl implements IOrderService{

    @Autowired
    private OrderRepository repository;

    @Override
    public List<OrderEntity> getAll() {
        return null;
    }

    @Override
    public OrderEntity getObjectById(int id) {
        return null;
    }

    @Override
    public int insertObject(OrderEntity orderEntity) {
        return 0;
    }

    @Override
    public int updateObject(OrderEntity orderEntity) {
        return 0;
    }

    @Override
    public int deleteObject(int id) {
        return 0;
    }
}
