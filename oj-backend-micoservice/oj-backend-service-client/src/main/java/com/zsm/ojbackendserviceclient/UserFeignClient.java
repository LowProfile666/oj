package com.zsm.ojbackendserviceclient;

import com.zsm.ojbackendcommon.common.ErrorCode;
import com.zsm.ojbackendcommon.exception.BusinessException;
import com.zsm.ojbackendmodel.enums.UserRoleEnum;
import com.zsm.ojbackendmodel.vo.UserVO;
import com.zsm.ojbackendmodel.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Collection;
import java.util.List;

import static com.zsm.ojbackendcommon.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户服务
 */
@FeignClient(name = "oj-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据id获取用户
     */
    @GetMapping("get")
    User getById(@RequestParam("id") Long id);

    /**
     * 根据id获取用户列表
     */
    @GetMapping("list")
    List<User> getUsersByIds(@RequestParam("ids") Collection<Long> ids);

    /**
     * 获取当前登录用户
     */
    default User getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 是否为管理员
     */
    default boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    /**
     * 是否为管理员
     */
    default boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 获取脱敏的用户信息
     */
    default UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
}
