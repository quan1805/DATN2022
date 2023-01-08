package huce.it.datnbackend.dto;

import huce.it.datnbackend.model.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private int id;
    private CustomerEntity customer;
//    private Timestamp deliveryDate;
    private int discount;
    private Timestamp createDate;
    private Timestamp updateDate;
    private int status;
    private String note;
    private Double total;
}
