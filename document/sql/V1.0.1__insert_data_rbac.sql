-- 用户数据
insert into ums_user(id, username, password, status, create_time, update_time)
values (1, 'admin', '$2a$10$joMZAI5/Y14uBML8hZ8Kz.XnJUubg/XbMxPO9nB/Tty/l7a61O6l2', '1 ', now(), now());
insert into ums_user(id, username, password, status, create_time, update_time)
values (2, 'user', '$2a$10$a4RwkV6oR3IX.63iFGniM.rQTqND71faIyE5HnYNmqeQZxXZXHSGC', '1 ', now(), now());

-- 角色数据
insert into ums_role (id, name, code, create_time, update_time)
values (1, '管理员', 'ADMIN', now(), now());
insert into ums_role (id, name, code, create_time, update_time)
values (2, '普通用户', 'USER', now(), now());

-- 权限数据
insert into ums_permission(id, name, url, create_time, update_time)
values (1, '用户管理-查询', '*:/user/query', now(), now());

-- 用户角色数据
insert into ums_user_role(id, user_id, role_id, create_time, update_time)
values (1, 1, 1, now(), now());
insert into ums_user_role(id, user_id, role_id, create_time, update_time)
values (2, 2, 2, now(), now());

-- 角色权限数据
insert into ums_role_permission(id, role_id, permission_id, create_time, update_time)
values (1, 1, 1, now(), now());
insert into ums_role_permission(id, role_id, permission_id, create_time, update_time)
values (2, 2, 1, now(), now());