package huce.it.datnbackend.dto;

import huce.it.datnbackend.model.BrandEntity;
import huce.it.datnbackend.model.ProductCategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private int id;
    private ProductCategoryEntity category;
    private BrandEntity brand;
    private String name;
    private String image;
    private int quantity;
    private int hot;
    private String description;
    private Timestamp createDate;
    private Timestamp updateDate;
    private int status;
    private Double price;
}
