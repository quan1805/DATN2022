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
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name= "username")
    private String username;

    @Column(name= "password")
    private String password;

    @Column(name = "role")
    private int role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @Column(name = "status")
    private int status;

    @Column(name = "createdate")
    private Timestamp createdDate;

    @Column(name = "updatedate")
    private Timestamp updatedDate;
}
