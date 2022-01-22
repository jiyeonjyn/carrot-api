package com.umc.courtking.src.product;


import com.umc.courtking.config.BaseException;
import com.umc.courtking.config.BaseResponse;
import com.umc.courtking.src.product.model.*;
import com.umc.courtking.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.umc.courtking.config.BaseResponseStatus.INVALID_USER_JWT;

@RestController
public class ProductController {

    @Autowired
    private final ProductProvider productProvider;
    @Autowired
    private final ProductService productService;
    @Autowired
    private final JwtService jwtService;

    public ProductController(ProductProvider productProvider, ProductService productService, JwtService jwtService){
        this.productProvider = productProvider;
        this.productService = productService;
        this.jwtService = jwtService;
    }

    @GetMapping("/products")
    public BaseResponse<List<GetProductListRes>> getProductList(
            @RequestParam(value = "writer", required = false) String writer,
            @RequestParam(value = "status", required = false) String status) {
        if (writer != null) {
            List<GetProductListRes> productList = productProvider.getProductListByUser(Integer.parseInt(writer), status);
            return new BaseResponse<>(productList); //현재 유저의 address와 상관없이 해당 유저의 상품리스트 모두 조회
        }
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        List<GetProductListRes> productList = productProvider.getProductList(userIdx);
        return new BaseResponse<>(productList); //현재 유저의 지역에 해당하는 상품리스트 조회
    }

    @GetMapping("/products/{productIdx}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable int productIdx) {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        GetProductRes getProductRes = productProvider.getProduct(productIdx, userIdx);
        return new BaseResponse<>(getProductRes);
    }

    @GetMapping("/categories/{categoryIdx}/products")
    public BaseResponse<List<GetProductListRes>> getProductListByCategory(@PathVariable int categoryIdx) {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        List<GetProductListRes> productList = productProvider.getProductListByCategory(categoryIdx, userIdx);
        return new BaseResponse<>(productList);
    }

    @PostMapping("/products")
    public BaseResponse<PostProductRes> postProduct(@RequestBody PostProductReq postProductReq) {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        postProductReq.setUserIdx(userIdx);
        PostProductRes postProductRes = productService.postProduct(postProductReq);
        return new BaseResponse<>(postProductRes);
    }

    @PutMapping("/products")
    public BaseResponse<PutProductRes> putProduct(@RequestBody PutProductReq putProductReq) {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
            if (putProductReq.getUserIdx()!= userIdx)
                return new BaseResponse<>(INVALID_USER_JWT);
            PutProductRes putProductRes = productService.putProduct(putProductReq);
            return new BaseResponse<>(putProductRes);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }

    @DeleteMapping("/products/{productIdx}")
    public BaseResponse<Integer> deleteProduct(@PathVariable int productIdx) {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
            int result = productService.deleteProduct(productIdx, userIdx);
            return new BaseResponse<>(result);
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
    }
}
