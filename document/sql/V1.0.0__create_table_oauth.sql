-- 授权码存储表 Table 'security_oauth2.oauth_code' doesn't exist
CREATE TABLE `oauth_code`
(
    `code`           varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    `authentication` blob                                                    NULL
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- 客户端信息表 Table 'security_oauth2.oauth_client_details' doesn't exist
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(256) COMMENT '客户端ID',
    `resource_ids`            varchar(256) COMMENT '访问资源ID',
    `client_secret`           varchar(256) COMMENT '客户端密钥',
    `scope`                   varchar(256) COMMENT '客户端申请权限',
    `authorized_grant_types`  varchar(256) COMMENT '授权类型',
    `web_server_redirect_uri` varchar(256) COMMENT '客户端重定向URI',
    `authorities`             varchar(256) COMMENT '客户端拥有权限值',
    `access_token_validity`   int(11) COMMENT 'access_token的有效时间',
    `refresh_token_validity`  int(11) COMMENT 'refresh_token的有效时间',
    `additional_information`  varchar(4096) COMMENT '扩展字段',
    `autoapprove`             varchar(256) COMMENT '是否允许自动授权',
    PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;


insert into oauth_client_details(client_id, resource_ids, client_secret, scope, authorized_grant_types,
                                 web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity,
                                 additional_information, autoapprove)
-- client_secret:abc1234!
values ('iyyish', 'ums-cloud', '$2a$10$r3lvfLdb/ash3LUFI5lYd.5zyKNRrF9ZCDTlp9PAFlIrjCjlqB632', 'all',
        'authorization_code,password,refresh_token',
        'http://www.baidu.com', null, null, null, null, 'true');