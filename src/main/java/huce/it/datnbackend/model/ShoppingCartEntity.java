package huce.it.datnbackend.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "shoppingcart")
public class ShoppingCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "total_item")
    private int totalItem;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingCart")
    private Set<CartShopEntity> cartShops;
}
