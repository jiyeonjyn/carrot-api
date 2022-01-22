package com.umc.courtking.src.user;

import com.umc.courtking.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetUserRes> viewUserList(){
        return this.jdbcTemplate.query("SELECT * FROM User",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("profileImg"),
                        rs.getString("status"),
                        rs.getString("createAt"),
                        rs.getString("updateAt"))

        );
    }

    public List<GetUserRes> viewUser(int userIdx) {
        return this.jdbcTemplate.query("SELECT * FROM User WHERE userIdx = ?",
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("nickName"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("profileImg"),
                        rs.getString("status"),
                        rs.getString("createAt"),
                        rs.getString("updateAt")),
                userIdx);
    }

    public int findUserByPhone(String phone) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT userIdx FROM User WHERE phone = ?",
                    Integer.class,
                    phone);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public String getAddress(int userIdx) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT address FROM User WHERE userIdx = ?",
                    String.class,
                    userIdx);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int addUser(PostUserReq postUserReq){
        String createUserQuery = "INSERT INTO User (nickName, phone, email, address, profileImg) VALUES (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{
                postUserReq.getNickName(),
                postUserReq.getPhone(),
                postUserReq.getEmail(),
                postUserReq.getAddress(),
                postUserReq.getProfileImg()
        };
        this.jdbcTemplate.update(createUserQuery, createUserParams);
        return this.jdbcTemplate.queryForObject("SELECT last_insert_id()", Integer.class);
    }
}