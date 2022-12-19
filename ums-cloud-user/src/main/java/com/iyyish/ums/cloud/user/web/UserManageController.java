package com.iyyish.ums.cloud.user.web;

import com.alibaba.fastjson.JSON;
import com.iyyish.ums.cloud.common.core.constant.Constants;
import com.iyyish.ums.cloud.user.model.entity.SysUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc: 用户管理
 * @date: 2022年12月19日
 */
@RestController
@RequestMapping("/user")
public class UserManageController {

    @GetMapping("/query/{id}")
    public SysUser query(@PathVariable("id") Long id, HttpServletRequest request) {
        SysUser sysUser = new SysUser();
        sysUser.setId(id);
        sysUser.setUsername(JSON.toJSONString(request.getAttribute(Constants.LOGIN_USER_ATTRIBUTE)));
        return sysUser;
    }
}
