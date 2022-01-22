package com.umc.courtking.src.product;

import com.umc.courtking.config.BaseException;
import com.umc.courtking.src.product.model.PostProductReq;
import com.umc.courtking.src.product.model.PostProductRes;
import com.umc.courtking.src.product.model.PutProductReq;
import com.umc.courtking.src.product.model.PutProductRes;
import com.umc.courtking.src.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.umc.courtking.config.BaseResponseStatus.INVALID_USER_JWT;

// Service Create, Update, Delete 로직 처리
@Service
public class ProductService {

    private final ProductDao productDao;
    private final UserDao userDao;

    @Autowired
    public ProductService(ProductDao productDao, UserDao userDao) {
        this.productDao = productDao;
        this.userDao = userDao;
    }

    public PostProductRes postProduct(PostProductReq postProductReq) {
        int productIdx = productDao.addProduct(postProductReq);
        PostProductRes postProductRes = new PostProductRes(productIdx);
        return postProductRes;
    }

    public PutProductRes putProduct(PutProductReq putProductReq) {
        int productIdx = productDao.updateProduct(putProductReq);
        PutProductRes putProductRes = new PutProductRes(productIdx);
        return putProductRes;
    }

    public int deleteProduct(int productIdx, int userIdx) throws BaseException {
        if (productDao.getWriter(productIdx)!=userIdx) {
            throw new BaseException(INVALID_USER_JWT);
        }
        int result = productDao.deleteProduct(productIdx);
        return  result;
    }
}
