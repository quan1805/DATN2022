package huce.it.datnbackend.dto;

import huce.it.datnbackend.model.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailDto {
    private int id;

    private ProductEntity product;

    private Double price;

    private Double promotionPrice;

    private int quantity;

    private String size;

    private Timestamp createDate;

    private Timestamp updateDate;

    private int status;
}
