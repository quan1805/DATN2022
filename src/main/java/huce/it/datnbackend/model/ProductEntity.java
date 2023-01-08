package huce.it.datnbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    private ProductCategoryEntity category;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @Column(name = "name")
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "hot")
    private int hot;

    @Column(name = "description")
    private String description;

    @Column(name = "createdate")
    private Timestamp createDate;

    @Column(name = "updatedate")
    private Timestamp updateDate;

    @Column(name = "status")
    private int status;

    @Column(name = "price")
    private Double price;

}
