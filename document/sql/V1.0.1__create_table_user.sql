-- 用户表:ums_user
CREATE TABLE `ums_user`
(
    `id`          int COMMENT 'id',
    `username`    varchar(30) COMMENT '用户名',
    `password`    varchar(256) COMMENT '密文密码',
    `status`      varchar(1) COMMENT '状态',
    `create_time` timestamp COMMENT '创建时间',
    `update_time` timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- 角色表:ums_role
drop table if exists `ums_role`;
CREATE TABLE `ums_role`
(
    `id`          int COMMENT 'id',
    `name`        varchar(30) COMMENT '角色名称',
    `code`        varchar(30) COMMENT '角色代码',
    `create_time` timestamp COMMENT '创建时间',
    `update_time` timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;
-- 权限表:ums_permission
drop table if exists `ums_permission`;
CREATE TABLE `ums_permission`
(
    `id`          int COMMENT 'id',
    `name`        varchar(30) COMMENT '权限名称',
    `url`         varchar(256) COMMENT '权限地址' UNIQUE,
    `create_time` timestamp COMMENT '创建时间',
    `update_time` timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;
-- 用户_角色表:ums_user_role
CREATE TABLE `ums_user_role`
(
    `id`          int COMMENT 'id',
    `user_id`     int COMMENT '用户id',
    `role_id`     int COMMENT '角色id',
    `create_time` timestamp COMMENT '创建时间',
    `update_time` timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;
-- 角色_权限表:ums_role_permission
CREATE TABLE `ums_role_permission`
(
    `id`            int COMMENT 'id',
    `role_id`       int COMMENT '角色id',
    `permission_id` int COMMENT '权限id',
    `create_time`   timestamp COMMENT '创建时间',
    `update_time`   timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;