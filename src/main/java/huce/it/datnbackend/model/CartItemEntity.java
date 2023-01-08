package huce.it.datnbackend.model;

import huce.it.datnbackend.dto.ProductDetailDto;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Table(name = "cartitem")
public class CartItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "total_price")
    private Double totalPrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cartshop_id")
    private CartShopEntity cartShop;

    @OneToOne
    @JoinColumn(name = "productdetail_id")
    private ProductDetailEntity productDetail;

}
