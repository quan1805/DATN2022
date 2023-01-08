package huce.it.datnbackend.services.shoppingcart;

import huce.it.datnbackend.model.*;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.repository.CartItemRepository;
import huce.it.datnbackend.repository.CartShopRepository;
import huce.it.datnbackend.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartShopRepository cartShopRepository;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;


    @Override
    public List<ShoppingCartEntity> getAll() {
        return null;
    }

    @Override
    public ShoppingCartEntity getObjectById(int id) {
        return null;
    }

    @Override
    public ShoppingCartEntity getObjectByCustomer(int id) {
        return shoppingCartRepository.findByCustomer(id);
    }

    @Override
    public int insertObject(ShoppingCartEntity shoppingCartEntity) {
        return 0;
    }

    @Override
    public int updateObject(ShoppingCartEntity shoppingCartEntity) {
        return 0;
    }

    @Override
    public int deleteObject(int id) {
        return 0;
    }

    @Override
    public Paged<ShoppingCartEntity> getPage(int pageNumber) {
        return null;
    }

    @Override
    public ShoppingCartEntity addItemToCart(ProductDetailEntity productDetail, int quantity, BrandEntity brand, CustomerEntity customer) {
        ShoppingCartEntity cart = shoppingCartRepository.findByCustomer(customer.getId());
        if (cart == null) {
            cart = new ShoppingCartEntity();
            cart = shoppingCartRepository.save(cart);
        }
        Set<CartShopEntity> cartShops = cart.getCartShops();
        CartShopEntity cartShop = findCartShop(cartShops, productDetail.getProduct().getBrand().getId());

        if (cartShop == null) {
            cartShop = new CartShopEntity();
            cartShop = cartShopRepository.save(cartShop);

        }
        Set<CartItemEntity> cartItems = cartShop.getCartItems();
        CartItemEntity cartItem = findCartItem(cartItems, productDetail.getId());
        if (cartShops == null){
            cartShops = new HashSet<>();
            cartItems = new HashSet<>();


//            cartShop.setShoppingCart(cart);
//            cartShop.setBrand(brand);
//            cartShop.setTotalItem(1);
//            cartShop.setTotalPrice(quantity * productDetail.getPromotionPrice());
//            cartShopRepository.save(cartShop);

            cartItem = new CartItemEntity();
            cartItem.setProductDetail(productDetail);
            cartItem.setTotalPrice(quantity * productDetail.getPromotionPrice());
            cartItem.setQuantity(quantity);
            cartItem.setCartShop(cartShop);
            cartItems.add(cartItem);
            cartItemRepository.save(cartItem);

            cartShop.setCartItems(cartItems);

            int totalItemsShop = totalItemsShop(cartShop.getCartItems());
            double totalPriceShop = totalPriceShop(cartShop.getCartItems());

            cartShop.setTotalItem(totalItemsShop);
            cartShop.setTotalPrice(totalPriceShop);
            cartShop.setShoppingCart(cart);
            cartShop.setBrand(brand);
            cartShops.add(cartShop);
            cartShopRepository.save(cartShop);
        } else{
            if (cartItems == null){
                cartItems = new HashSet<>();
                if(cartItem == null){
                    cartItem = new CartItemEntity();
                    cartItem.setProductDetail(productDetail);
                    cartItem.setTotalPrice(quantity * productDetail.getPromotionPrice());
                    cartItem.setQuantity(quantity);
                    cartItem.setCartShop(cartShop );
                    cartItems.add(cartItem);
                    cartItemRepository.save(cartItem);
                }
            }else{
                if(cartItem == null){
                    cartItem = new CartItemEntity();
                    cartItem.setProductDetail(productDetail);
                    cartItem.setTotalPrice(quantity * productDetail.getPromotionPrice());
                    cartItem.setQuantity(quantity);
                    cartItem.setCartShop(cartShop );
                    cartItems.add(cartItem);
                    cartItemRepository.save(cartItem);
                }else {
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    cartItem.setTotalPrice(cartItem.getTotalPrice() + quantity * productDetail.getPromotionPrice());
                    cartItemRepository.save(cartItem);
                }

            }
            cartShop.setCartItems(cartItems);

            int totalItemsShop = totalItemsShop(cartShop.getCartItems());
            double totalPriceShop = totalPriceShop(cartShop.getCartItems());

            cartShop.setShoppingCart(cart);
            cartShop.setTotalItem(totalItemsShop);
            cartShop.setTotalPrice(totalPriceShop);
            cartShop.setBrand(brand);
            cartShops.add(cartShop);
            cartShopRepository.save(cartShop);
        }
        cart.setCartShops(cartShops);
        int totalItems = totalItems(cart.getCartShops());
        double totalPrice = totalPrice(cart.getCartShops());

        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        cart.setCustomer(customer);
        customer.setCart(cart);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartEntity updateItemInCart(ProductDetailEntity productDetail, int quantity, BrandEntity brand, CustomerEntity customer) {
        ShoppingCartEntity cart = shoppingCartRepository.findByCustomer(customer.getId());


        Set<CartShopEntity> cartShops = cart.getCartShops();
        CartShopEntity cartShop = findCartShop(cartShops, productDetail.getProduct().getBrand().getId());
        Set<CartItemEntity> cartItems = cartShop.getCartItems();
        CartItemEntity cartItem = findCartItem(cartItems, productDetail.getId());

        cartItem.setQuantity(quantity);
        cartItem.setTotalPrice(quantity * productDetail.getPromotionPrice());

        cartItemRepository.save(cartItem);

        int totalItemsShop = totalItemsShop(cartShop.getCartItems());
        double totalPriceShop = totalPriceShop(cartShop.getCartItems());
        cartShop.setTotalItem(totalItemsShop);
        cartShop.setTotalPrice(totalPriceShop);
        cartShopRepository.save(cartShop);

        int totalItems = totalItems(cart.getCartShops());
        double totalPrice = totalPrice(cart.getCartShops());
        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        customer.setCart(cart);

        return shoppingCartRepository.save(cart);



    }

    @Override
    public ShoppingCartEntity deleteItemFromCart(ProductDetailEntity productDetail, BrandEntity brand, CustomerEntity customer) {
        ShoppingCartEntity cart = shoppingCartRepository.findByCustomer(customer.getId());


        Set<CartShopEntity> cartShops = cart.getCartShops();
        CartShopEntity cartShop = findCartShop(cartShops, productDetail.getProduct().getBrand().getId());
        Set<CartItemEntity> cartItems = cartShop.getCartItems();
        CartItemEntity cartItem = findCartItem(cartItems, productDetail.getId());

        cartItems.remove(cartItem);

        cartItemRepository.delete(cartItem);

        int totalItemsShop = totalItemsShop(cartShop.getCartItems());
        double totalPriceShop = totalPriceShop(cartShop.getCartItems());
        cartShop.setCartItems(cartItems);
        cartShop.setTotalItem(totalItemsShop);
        cartShop.setTotalPrice(totalPriceShop);
        cartShopRepository.save(cartShop);

        int totalItems = totalItems(cart.getCartShops());
        double totalPrice = totalPrice(cart.getCartShops());
        cart.setCartShops(cartShops);
        cart.setTotalItem(totalItems);
        cart.setTotalPrice(totalPrice);
        customer.setCart(cart);

        return shoppingCartRepository.save(cart);
    }

    @Override
    public ShoppingCartEntity removeAllItem(ShoppingCartEntity cart) {
        return null;
    }


    private CartItemEntity findCartItem(Set<CartItemEntity> cartItems, int productDetailId){
        if (cartItems == null){
            return null;
        }
        CartItemEntity cartItem = null;
        for (CartItemEntity item : cartItems){
            if(item.getProductDetail().getId() == productDetailId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private CartShopEntity findCartShop(Set<CartShopEntity> cartShops, int brandId){
        if (cartShops == null){
            return null;
        }
        CartShopEntity cartShop = null;
        for (CartShopEntity item : cartShops){
            if(item.getBrand().getId() == brandId) {
                cartShop = item;
            }
        }
        return cartShop;
    }

    private int totalItemsShop(Set<CartItemEntity> cartItems){
        int totalItems = 0;
        for (CartItemEntity item : cartItems){
            totalItems += item.getQuantity();
        }
        return totalItems;
    }

    private int totalItems(Set<CartShopEntity> cartShops){
        int totalItems = 0;
        for (CartShopEntity item : cartShops){
            totalItems += item.getTotalItem();
        }
        return totalItems;
    }

    private Double totalPriceShop(Set<CartItemEntity> cartItems){
        Double totalPrice = 0.0;
        for (CartItemEntity item : cartItems){
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

    private Double totalPrice(Set<CartShopEntity> cartShops){
        Double totalPrice = 0.0;
        for (CartShopEntity item : cartShops){
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }
}
