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
@Table(name = "ordershop")
public class OrderShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @OneToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "message")
    private String message;

    @Column(name = "discount")
    private int discount;

    @Column(name = "note")
    private String note;

    @Column(name = "status")
    private int status;

    @Column(name = "createdate")
    private Timestamp createDate;

    @Column(name = "updatedate")
    private Timestamp updateDate;

    @Column(name = "deliverydate")
    private Timestamp deliveryDate;

    @Column(name = "total")
    private Double total;



}
