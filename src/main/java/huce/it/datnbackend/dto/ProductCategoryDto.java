package huce.it.datnbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class ProductCategoryDto {
    private int id;

    private String name;

    private int parentID;

    private Long numberOfProduct;
}
