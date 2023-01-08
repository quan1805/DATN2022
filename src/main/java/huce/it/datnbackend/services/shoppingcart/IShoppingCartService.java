package huce.it.datnbackend.services.shoppingcart;

import huce.it.datnbackend.model.*;
import huce.it.datnbackend.paging.Paged;
import huce.it.datnbackend.services.IFunctionService;

import java.util.List;

public interface IShoppingCartService extends IFunctionService<ShoppingCartEntity> {

    @Override
    List<ShoppingCartEntity> getAll();

    @Override
    ShoppingCartEntity getObjectById(int id);

    ShoppingCartEntity getObjectByCustomer(int id);

    @Override
    int insertObject(ShoppingCartEntity shoppingCartEntity);

    @Override
    int updateObject(ShoppingCartEntity shoppingCartEntity);

    @Override
    int deleteObject(int id);

    @Override
    Paged<ShoppingCartEntity> getPage(int pageNumber);

    ShoppingCartEntity addItemToCart(ProductDetailEntity productDetail, int quantity, BrandEntity brand, CustomerEntity customer);
    ShoppingCartEntity updateItemInCart (ProductDetailEntity productDetail, int quantity, BrandEntity brand, CustomerEntity customer);
    ShoppingCartEntity deleteItemFromCart (ProductDetailEntity productDetail, BrandEntity brand, CustomerEntity customer);

    ShoppingCartEntity removeAllItem(ShoppingCartEntity cart);



}
