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
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @Column(name = "total")
    private Double total;

    @Column(name = "discount")
    private int discount;

    @Column(name = "createdate")
    private Timestamp createDate;

    @Column(name = "updatedate")
    private Timestamp updateDate;

    @Column(name = "status")
    private int status;

    @Column(name = "note")
    private String note;
}
