package huce.it.datnbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
@AllArgsConstructor
public class BrandDto {

    private int id;

    private String name;

    private Long numberOfProduct;
}
