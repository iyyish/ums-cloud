package com.iyyish.ums.cloud.auth.manager;

import com.iyyish.ums.cloud.auth.model.vo.SysRolePermissionVo;
import com.iyyish.ums.cloud.auth.service.ISysRolePermissionService;
import com.iyyish.ums.cloud.common.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @desc: 加载role-url到redis
 * @date: 2022年12月18日
 */
@Slf4j
@Component
public class LoadPermissionRoleManager {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ISysRolePermissionService rolePermissionService;

    @PostConstruct
    public void init() {
        List<SysRolePermissionVo> rolePermissionVoList = rolePermissionService.queryPermissionRoles();
        rolePermissionVoList.forEach(vo -> {
            List<String> roles = vo.getRoles().stream().map(sysRole -> Constants.ROLE_PREFIX + sysRole.getCode()).collect(Collectors.toList());
            redisTemplate.opsForHash().put(Constants.OAUTH_URLS, vo.getPermissionUrl(), roles);
        });
    }
}
