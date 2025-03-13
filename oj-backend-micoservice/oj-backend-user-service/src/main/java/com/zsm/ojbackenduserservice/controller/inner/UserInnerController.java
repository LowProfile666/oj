package com.zsm.ojbackenduserservice.controller.inner;

import com.zsm.ojbackendmodel.entity.User;
import com.zsm.ojbackendserviceclient.UserFeignClient;
import com.zsm.ojbackenduserservice.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * author: ZSM
 * time: 2025-03-12 19:41
 */
@RequestMapping("/inner")
@RestController
public class UserInnerController implements UserFeignClient {
    @Resource
    private UserService userService;
    /**
     * 根据id获取用户
     */
    @GetMapping("get")
    @Override
    public User getById(@RequestParam("id") Long id) {
        return userService.getById(id);
    }

    /**
     * 根据id获取用户列表
     */
    @GetMapping("list")
    @Override
    public List<User> getUsersByIds(@RequestParam("ids") Collection<Long> ids) {
        return userService.listByIds(ids);
    }
}
