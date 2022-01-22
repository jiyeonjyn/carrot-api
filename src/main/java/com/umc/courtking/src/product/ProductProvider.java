package com.umc.courtking.src.product;

import com.umc.courtking.config.Processing;
import com.umc.courtking.src.product.model.GetProductListRes;
import com.umc.courtking.src.product.model.GetProductRes;
import com.umc.courtking.src.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Service Select 로직 처리
@Service
public class ProductProvider {

    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public ProductProvider(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    public List<GetProductListRes> getProductList(int userIdx){
        String address = userDao.getAddress(userIdx);
        address = Processing.processAddress(address);
        List<GetProductListRes> productList = productDao.viewProductList(address);
        for (GetProductListRes product : productList) {
            product.setUpdateAt(Processing.processTime(product.getUpdateAt()));
            product.setAddress(Processing.processAddress(product.getAddress()));
        }
        return productList;
    }

    public GetProductRes getProduct(int productIdx, int userIdx) {
        if (productDao.getWriter(productIdx) != userIdx)
            productDao.increaseHits(productIdx, userIdx); //조회수 1 증가
        List<GetProductRes> productList = productDao.viewProduct(productIdx);
        for (GetProductRes product : productList) {
            product.setUpdateAt(Processing.processTime(product.getUpdateAt()));
            product.setAddress(Processing.processAddress(product.getAddress()));
        }
        return productList.get(0);
    }

    public List<GetProductListRes> getProductListByCategory(int categoryIdx, int userIdx) {
        String address = userDao.getAddress(userIdx);
        address = Processing.processAddress(address);
        List<GetProductListRes> productList = productDao.viewProductListByCategory(categoryIdx, address);
        return productList;
    }

    public List<GetProductListRes> getProductListByUser(int userIdx, String status) {
        List<GetProductListRes> productList = productDao.viewProductListByUser(userIdx, status);
        for (GetProductListRes product : productList) {
            product.setUpdateAt(Processing.processTime(product.getUpdateAt()));
            product.setAddress(Processing.processAddress(product.getAddress()));
        }
        return productList;
    }
}
