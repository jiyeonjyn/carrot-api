package com.umc.courtking.src.product;

import com.umc.courtking.src.product.model.GetProductListRes;
import com.umc.courtking.src.product.model.GetProductRes;
import com.umc.courtking.src.product.model.PostProductReq;
import com.umc.courtking.src.product.model.PutProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ProductDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetProductListRes> viewProductList(String address){
        return this.jdbcTemplate.query("SELECT productIdx, P.userIdx AS userIdx, categoryIdx, productName, price, " +
                        "productMainImg, P.status AS status, P.updateAt AS updateAt, nickName, address FROM Product P " +
                        "INNER JOIN User U ON P.userIdx = U.userIdx " +
                        "WHERE address LIKE ?",
                (rs, rowNum) -> new GetProductListRes(
                        rs.getInt("productIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("productMainImg"),
                        rs.getString("status"),
                        rs.getString("updateAt"),
                        rs.getString("nickName"),
                        rs.getString("address")),
                "%"+address+"%");
    }

    public List<GetProductRes> viewProduct(int productIdx) {
        return this.jdbcTemplate.query("SELECT P.productIdx AS productIdx, P.userIdx AS userIdx, P.categoryIdx AS categoryIdx, " +
                        "productName, price, productMainImg, productDetail, P.status AS status, P.updateAt AS updateAt, " +
                        "nickName, address, category, COUNT(viewIdx) AS hits " +
                        "FROM Product P " +
                        "INNER JOIN User U ON U.userIdx = P.userIdx " +
                        "INNER JOIN Category C on P.categoryIdx = C.categoryIdx " +
                        "LEFT JOIN ViewCount VC on P.productIdx = VC.productIdx " +
                        "WHERE P.productIdx = ? " +
                        "GROUP BY P.productIdx",
                (rs, rowNum) -> new GetProductRes(
                        rs.getInt("productIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("productMainImg"),
                        rs.getString("productDetail"),
                        rs.getString("status"),
                        rs.getString("updateAt"),
                        rs.getString("nickName"),
                        rs.getString("address"),
                        rs.getString("category"),
                        rs.getInt("hits")),
                productIdx);
    }

    public List<GetProductListRes> viewProductListByCategory(int categoryIdx, String address){
        return this.jdbcTemplate.query("SELECT productIdx, P.userIdx AS userIdx, categoryIdx, productName, price, " +
                        "productMainImg, P.status AS status, P.updateAt AS updateAt, nickName, address FROM Product P " +
                        "INNER JOIN User U ON P.userIdx = U.userIdx " +
                        "WHERE categoryIdx = ? AND address LIKE ?",
                (rs, rowNum) -> new GetProductListRes(
                        rs.getInt("productIdx"),
                        rs.getInt("userIdx"),
                        rs.getInt("categoryIdx"),
                        rs.getString("productName"),
                        rs.getInt("price"),
                        rs.getString("productMainImg"),
                        rs.getString("status"),
                        rs.getString("updateAt"),
                        rs.getString("nickName"),
                        rs.getString("address")),
                new Object[] {categoryIdx, "%"+address+"%"});
    }

    public List<GetProductListRes> viewProductListByUser(int userIdx, String status) {
        if (status == null) {
            return this.jdbcTemplate.query("SELECT productIdx, P.userIdx AS userIdx, categoryIdx, productName, price, " +
                            "productMainImg, P.status AS status, P.updateAt AS updateAt, nickName, address FROM Product P " +
                            "INNER JOIN User U ON P.userIdx = U.userIdx " +
                            "WHERE P.userIdx = ?",
                    (rs, rowNum) -> new GetProductListRes(
                            rs.getInt("productIdx"),
                            rs.getInt("userIdx"),
                            rs.getInt("categoryIdx"),
                            rs.getString("productName"),
                            rs.getInt("price"),
                            rs.getString("productMainImg"),
                            rs.getString("status"),
                            rs.getString("updateAt"),
                            rs.getString("nickName"),
                            rs.getString("address")),
                    userIdx);
        } else {
            return this.jdbcTemplate.query("SELECT productIdx, P.userIdx AS userIdx, categoryIdx, productName, price, " +
                            "productMainImg, P.status AS status, P.updateAt AS updateAt, nickName, address FROM Product P " +
                            "INNER JOIN User U ON P.userIdx = U.userIdx " +
                            "WHERE P.userIdx = ? AND P.status = ?",
                    (rs, rowNum) -> new GetProductListRes(
                            rs.getInt("productIdx"),
                            rs.getInt("userIdx"),
                            rs.getInt("categoryIdx"),
                            rs.getString("productName"),
                            rs.getInt("price"),
                            rs.getString("productMainImg"),
                            rs.getString("status"),
                            rs.getString("updateAt"),
                            rs.getString("nickName"),
                            rs.getString("address")),
                    new Object[] {userIdx, status});
        }


    }

    public int getWriter(int productIdx) {
        return this.jdbcTemplate.queryForObject("SELECT userIdx FROM Product WHERE productIdx = ?",
                Integer.class,
                productIdx);
    }

    public int addProduct(PostProductReq postProductReq) {
        String createProductQuery = "INSERT INTO Product (userIdx, categoryIdx, productName, price, productDetail, productMainImg) " +
                "VALUES (?,?,?,?,?,?)";
        Object[] createProductParams = new Object[]{
                postProductReq.getUserIdx(),
                postProductReq.getCategoryIdx(),
                postProductReq.getProductName(),
                postProductReq.getPrice(),
                postProductReq.getProductDetail(),
                postProductReq.getProductMainImg()
        };
        this.jdbcTemplate.update(createProductQuery, createProductParams);
        return this.jdbcTemplate.queryForObject("SELECT last_insert_id()", Integer.class);
    }

    public void increaseHits(int productIdx, int userIdx) {
        this.jdbcTemplate.update("INSERT INTO ViewCount (userIdx, productIdx) VALUES (?,?)",
                new Object[]{ userIdx, productIdx });
    }

    public int updateProduct(PutProductReq putProductReq) {
        String createProductQuery = "UPDATE Product SET categoryIdx=?, productName=?, price=?,productDetail=?, " +
                "productMainImg=?, updateAt=now() WHERE productIdx=?";
        Object[] createProductParams = new Object[]{
                putProductReq.getCategoryIdx(),
                putProductReq.getProductName(),
                putProductReq.getPrice(),
                putProductReq.getProductDetail(),
                putProductReq.getProductMainImg(),
                putProductReq.getProductIdx()
        };
        this.jdbcTemplate.update(createProductQuery, createProductParams);
        return putProductReq.getProductIdx();
    }

    public int deleteProduct(int productIdx) {
        return this.jdbcTemplate.update("DELETE FROM Product WHERE productIdx = ?", productIdx);
    }
}
