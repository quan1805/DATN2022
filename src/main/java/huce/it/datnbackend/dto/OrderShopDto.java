package huce.it.datnbackend.dto;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderShopDto {
    private int id;

    private BrandEntity brand;

    private OrderEntity order;

    private String message;

    private int discount;

    private String note;

    private int status;

    private Timestamp createDate;

    private Timestamp updateDate;

    private Timestamp deliveryDate;

    private Double total;

}
