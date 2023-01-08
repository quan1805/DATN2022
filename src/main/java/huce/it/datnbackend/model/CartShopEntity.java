package huce.it.datnbackend.model;

import huce.it.datnbackend.controller.ShoppingCartController;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "cartshop")
public class CartShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "shoppingcart_id")
    private ShoppingCartEntity shoppingCart;


    @Column(name = "total_item")
    private int totalItem;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cartShop")
    private Set<CartItemEntity> cartItems;

}
