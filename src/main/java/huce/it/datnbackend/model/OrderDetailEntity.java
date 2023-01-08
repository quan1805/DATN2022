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
@Table(name = "orderdetail")
public class OrderDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "ordershop_id")
    private OrderShopEntity orderShop;

    @OneToOne
    @JoinColumn(name = "productdetail_id")
    private ProductDetailEntity productDetail;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "createdate")
    private Timestamp createDate;

    @Column(name = "updatedate")
    private Timestamp updateDate;

    @Column(name = "status")
    private int status;

}
