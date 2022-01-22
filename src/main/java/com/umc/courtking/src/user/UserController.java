package com.umc.courtking.src.user;


import jdk.jfr.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.umc.courtking.config.BaseException;
import com.umc.courtking.config.BaseResponse;
import com.umc.courtking.src.user.model.*;
import com.umc.courtking.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;


import static com.umc.courtking.config.BaseResponseStatus.*;
import static com.umc.courtking.utils.ValidationRegex.*;


@RestController
public class UserController {

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("/users")
    public BaseResponse<List<GetUserRes>> getUserList(){
        int userIdx = -1;
        try {
             userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        if (userIdx == 1) {
            List<GetUserRes> userList = userProvider.getUserList();
            return new BaseResponse<>(userList);
        } else {
            return new BaseResponse(INVALID_USER_JWT);
        }
    }

    @GetMapping("/users/{userIdx}")
    public BaseResponse<GetUserRes> getUser(@PathVariable int userIdx){
        GetUserRes getUserRes = userProvider.getUser(userIdx);
        return new BaseResponse<>(getUserRes);
    }

    @GetMapping("/useridx")
    public BaseResponse<Integer> getUserIdx() {
        int userIdx = -1;
        try {
            userIdx = jwtService.getUserIdx();
        } catch (BaseException e) {
            return new BaseResponse(e.getStatus());
        }
        return new BaseResponse<>(userIdx);
    }

    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<LoginRes> login(@RequestBody LoginReq loginReq) {
        int userIdx = userProvider.getUserByPhone(loginReq.getPhone());
        if (userIdx == -1)
            return new BaseResponse(FAILED_TO_LOGIN);
        final String token = jwtService.createJwt(userIdx);
        return new BaseResponse<>(new LoginRes(token));
    }

    @ResponseBody
    @PostMapping("/users")
    public BaseResponse<PostUserRes> postUser(@RequestBody PostUserReq postUserReq){
        PostUserRes postUserRes = userService.postUser(postUserReq);
        return new BaseResponse<>(postUserRes);
    }

}
