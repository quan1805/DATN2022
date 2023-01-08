package huce.it.datnbackend.dto;

import huce.it.datnbackend.model.OrderEntity;
import huce.it.datnbackend.model.OrderShopEntity;
import huce.it.datnbackend.model.ProductDetailEntity;
import huce.it.datnbackend.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDto {
    private int id;

    private OrderShopEntity orderShop;

    private ProductEntity product;

    private ProductDetailEntity productDetail;

    private Double price;

    private int quantity;

    private Timestamp createDate;

    private Timestamp updateDate;

    private int status;

    private double total;
}
