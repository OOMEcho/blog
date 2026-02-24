DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`       BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`       BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`         INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`          VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `username`        VARCHAR(16)     NOT NULL COMMENT '用户名',
    `password`        VARCHAR(255)    NOT NULL COMMENT '密码',
    `nickname`        VARCHAR(16)              DEFAULT NULL COMMENT '昵称',
    `email`           VARCHAR(32)     NOT NULL COMMENT '邮箱',
    `sex`             CHAR(1)                  DEFAULT '0' COMMENT '性别(0-男,1-女)',
    `phone`           VARCHAR(11)              DEFAULT NULL COMMENT '电话',
    `avatar`          VARCHAR(255)             DEFAULT NULL COMMENT '头像',
    `status`          CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `last_login_ip`   VARCHAR(32)              DEFAULT NULL COMMENT '最后登录IP',
    `last_login_time` DATETIME                 DEFAULT NULL COMMENT '最后登录时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_username` (`username`) USING BTREE,
    UNIQUE KEY `uk_email` (`email`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户信息表';

DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    `role_name`   VARCHAR(32)     NOT NULL COMMENT '角色名称',
    `role_code`   VARCHAR(16)     NOT NULL COMMENT '角色编码',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '角色状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '角色信息表';

DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `id`      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT          NOT NULL COMMENT '用户ID',
    `role_id` BIGINT          NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY uk_user_role (`user_id`, `role_id`),
    INDEX idx_user_id (`user_id`),
    INDEX idx_role_id (`role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户和角色关联表';

DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `perm_code`   VARCHAR(64)     NOT NULL COMMENT '权限编码(全局唯一，如 blog:article:add)',
    `perm_name`   VARCHAR(64)     NOT NULL COMMENT '权限名称',
    `perm_type`   CHAR(1)         NOT NULL COMMENT '权限类型(M=页面,B=按钮,A=API)',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `remark`      VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='功能权限表';

DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id`   BIGINT          NOT NULL COMMENT '角色ID',
    `perm_code` VARCHAR(64)     NOT NULL COMMENT '权限编码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_perm` (`role_id`, `perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色与权限关联表';

DROP TABLE IF EXISTS `t_resource`;
CREATE TABLE `t_resource`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`      BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `request_method` VARCHAR(16)     NOT NULL COMMENT '请求方法(GET/POST/PUT/DELETE/ALL)',
    `request_uri`    VARCHAR(255)    NOT NULL COMMENT 'URI匹配模式(Ant风格)',
    `perm_code`      VARCHAR(64)     NOT NULL COMMENT '关联权限编码',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    `remark`         VARCHAR(128)             DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    INDEX `idx_method_uri` (`request_method`, `request_uri`),
    INDEX `idx_perm_code` (`perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='资源(URL)与权限映射表';

DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `menu_code`   VARCHAR(64)     NOT NULL COMMENT '菜单编码(唯一,前端组件映射用)',
    `menu_name`   VARCHAR(50)     NOT NULL COMMENT '菜单名称',
    `parent_id`   BIGINT          NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `order_num`   INT             NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `name`        VARCHAR(50)              DEFAULT NULL COMMENT '前端路由名称',
    `path`        VARCHAR(128)             DEFAULT NULL COMMENT '前端路由路径',
    `menu_type`   CHAR(1)         NOT NULL COMMENT '菜单类型(D-目录,M-菜单)',
    `icon`        VARCHAR(64)              DEFAULT '#' COMMENT '菜单图标',
    `hidden`      TINYINT                  DEFAULT 0 COMMENT '菜单状态(0-显示,1-隐藏)',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '菜单状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_menu_code` (`menu_code`),
    UNIQUE KEY `uk_menu_route_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '菜单表';

DROP TABLE IF EXISTS `t_menu_permission`;
CREATE TABLE `t_menu_permission`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `menu_id`   BIGINT          NOT NULL COMMENT '菜单ID',
    `perm_code` VARCHAR(64)     NOT NULL COMMENT '权限编码',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_menu_perm` (`menu_id`, `perm_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单与权限关联表';

DROP TABLE IF EXISTS `t_dictionary`;
CREATE TABLE `t_dictionary`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`      VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `dict_name`   VARCHAR(32)     NOT NULL COMMENT '字典名称',
    `dict_type`   VARCHAR(32)     NOT NULL COMMENT '字典类型',
    `dict_sort`   INT                      DEFAULT 0 COMMENT '字典排序',
    `dict_label`  VARCHAR(32)     NOT NULL COMMENT '字典标签',
    `dict_value`  VARCHAR(32)     NOT NULL COMMENT '字典键值',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '字典表';

DROP TABLE IF EXISTS `t_whitelist`;
CREATE TABLE `t_whitelist`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`      BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`      BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`        TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`        INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`         VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `request_method` VARCHAR(8)      NOT NULL COMMENT '请求方法,GET,POST,PUT,DELETE,ALL=不限制',
    `request_uri`    VARCHAR(255)    NOT NULL COMMENT 'URI匹配模式,支持Ant风格,比如/api/user/**',
    `description`    VARCHAR(128)             DEFAULT NULL COMMENT '描述',
    `status`         CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '白名单表';

DROP TABLE IF EXISTS `t_sys_operate_log`;
CREATE TABLE `t_sys_operate_log`
(
    `id`              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `trace_id`        VARCHAR(64)              DEFAULT NULL COMMENT '链路追踪ID',
    `module_title`    VARCHAR(64)              DEFAULT NULL COMMENT '模块标题',
    `business_type`   INT                      DEFAULT NULL COMMENT '业务类型(0-其它,1-新增,2-修改,3-删除)',
    `request_url`     VARCHAR(128)             DEFAULT NULL COMMENT '请求地址',
    `request_ip`      VARCHAR(64)              DEFAULT NULL COMMENT '请求IP',
    `request_local`   VARCHAR(64)              DEFAULT NULL COMMENT '请求地点',
    `request_type`    VARCHAR(32)              DEFAULT NULL COMMENT '请求方式',
    `request_method`  VARCHAR(255)             DEFAULT NULL COMMENT '请求方法',
    `request_args`    LONGTEXT                 DEFAULT NULL COMMENT '请求参数',
    `response_result` LONGTEXT                 DEFAULT NULL COMMENT '响应结果',
    `error_message`   LONGTEXT                 DEFAULT NULL COMMENT '错误响应',
    `operate_user`    VARCHAR(64)              DEFAULT NULL COMMENT '操作用户',
    `operate_time`    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `deplete_time`    BIGINT                   DEFAULT 0 COMMENT '消耗时间(单位：毫秒)',
    `operate_status`  CHAR(1)                  DEFAULT '0' COMMENT '操作状态(0-成功,1-失败)',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_trace_id` (`trace_id`) USING BTREE COMMENT '链路追踪ID索引'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志表';

DROP TABLE IF EXISTS `t_sys_login_log`;
CREATE TABLE `t_sys_login_log`
(
    `id`             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `login_username` VARCHAR(64)              DEFAULT NULL COMMENT '用户名',
    `login_ip`       VARCHAR(64)              DEFAULT NULL COMMENT '登录IP地址',
    `login_local`    VARCHAR(128)             DEFAULT NULL COMMENT '登录地点',
    `login_browser`  VARCHAR(64)              DEFAULT NULL COMMENT '浏览器类型',
    `login_os`       VARCHAR(64)              DEFAULT NULL COMMENT '操作系统',
    `login_time`     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `login_status`   CHAR(1)         NOT NULL DEFAULT '0' COMMENT '操作状态(0-成功,1-失败)',
    `error_message`  LONGTEXT                 DEFAULT NULL COMMENT '错误响应',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='登录日志表';

DROP TABLE IF EXISTS `t_file_metadata`;
CREATE TABLE `t_file_metadata`
(
    `id`                 BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`          BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`          BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`            TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`            INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `remark`             VARCHAR(100)             DEFAULT NULL COMMENT '备注',
    `file_name`          VARCHAR(255)    NOT NULL COMMENT '文件名称',
    `original_file_name` VARCHAR(255)    NOT NULL COMMENT '原始文件名称',
    `suffix`             VARCHAR(32)              DEFAULT NULL COMMENT '文件后缀',
    `file_path`          VARCHAR(255)    NOT NULL COMMENT '文件存储路径',
    `file_size`          BIGINT          NOT NULL COMMENT '文件大小,单位字节',
    `content_type`       VARCHAR(128)             DEFAULT NULL COMMENT '文件类型',
    `platform`           VARCHAR(16)              DEFAULT NULL COMMENT '存储平台',
    `upload_time`        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    `md5`                VARCHAR(64)              DEFAULT NULL COMMENT '文件MD5值',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文件元数据表';

DROP TABLE IF EXISTS `t_category`;
CREATE TABLE `t_category`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `name`        VARCHAR(32)     NOT NULL COMMENT '分类名称',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序',
    `description` VARCHAR(255)             DEFAULT NULL COMMENT '描述',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文章分类表';

DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `name`        VARCHAR(32)     NOT NULL COMMENT '标签名称',
    `color`       VARCHAR(16)              DEFAULT NULL COMMENT '标签颜色(如#409EFF)',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文章标签表';

DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人(作者ID)',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `title`       VARCHAR(128)    NOT NULL COMMENT '文章标题',
    `summary`     VARCHAR(512)             DEFAULT NULL COMMENT '文章摘要',
    `content`     LONGTEXT        NOT NULL COMMENT '文章内容(Markdown)',
    `cover_image` VARCHAR(255)             DEFAULT NULL COMMENT '封面图片路径',
    `category_id` BIGINT                   DEFAULT NULL COMMENT '分类ID',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-草稿,1-已发布,2-待审核,3-审核拒绝)',
    `reject_reason` VARCHAR(255)           DEFAULT NULL COMMENT '审核拒绝原因',
    `is_top`      TINYINT         NOT NULL DEFAULT 0 COMMENT '是否置顶(0-否,1-是)',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序值(越大越靠前)',
    `view_count`  BIGINT          NOT NULL DEFAULT 0 COMMENT '浏览量',
    `like_count`  BIGINT          NOT NULL DEFAULT 0 COMMENT '点赞数',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_category_id` (`category_id`),
    INDEX `idx_create_by` (`create_by`),
    INDEX `idx_status` (`status`),
    INDEX `idx_create_time` (`create_time`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文章表';

DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `article_id` BIGINT          NOT NULL COMMENT '文章ID',
    `tag_id`     BIGINT          NOT NULL COMMENT '标签ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_article_tag` (`article_id`, `tag_id`),
    INDEX `idx_article_id` (`article_id`),
    INDEX `idx_tag_id` (`tag_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '文章与标签关联表';

DROP TABLE IF EXISTS `t_comment`;
CREATE TABLE `t_comment`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `article_id`  BIGINT          NOT NULL COMMENT '文章ID',
    `user_id`     BIGINT          NOT NULL COMMENT '评论用户ID',
    `parent_id`   BIGINT                   DEFAULT NULL COMMENT '父评论ID(为空则是顶级评论)',
    `reply_to_id` BIGINT                   DEFAULT NULL COMMENT '回复目标评论ID',
    `content`     VARCHAR(1024)   NOT NULL COMMENT '评论内容',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-待审核,1-已通过,2-已拒绝)',
    `ip`          VARCHAR(64)              DEFAULT NULL COMMENT '评论者IP',
    `location`    VARCHAR(128)             DEFAULT NULL COMMENT '评论者地区',
    `browser`     VARCHAR(64)              DEFAULT NULL COMMENT '浏览器',
    `os`          VARCHAR(64)              DEFAULT NULL COMMENT '操作系统',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_article_id` (`article_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '评论表';

DROP TABLE IF EXISTS `t_friend_link`;
CREATE TABLE `t_friend_link`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `create_by`   BIGINT                   DEFAULT NULL COMMENT '创建人',
    `update_by`   BIGINT                   DEFAULT NULL COMMENT '更新人',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除标记(0=正常,1=删除)',
    `version`     INT             NOT NULL DEFAULT 1 COMMENT '版本号,用于乐观锁',
    `name`        VARCHAR(64)     NOT NULL COMMENT '网站名称',
    `url`         VARCHAR(255)    NOT NULL COMMENT '网站地址',
    `logo`        VARCHAR(255)             DEFAULT NULL COMMENT '网站Logo',
    `description` VARCHAR(255)             DEFAULT NULL COMMENT '网站描述',
    `sort`        INT             NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      CHAR(1)         NOT NULL DEFAULT '0' COMMENT '状态(0-正常,1-停用)',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '友情链接表';

DROP TABLE IF EXISTS `t_blog_config`;
CREATE TABLE `t_blog_config`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `config_key`   VARCHAR(64)     NOT NULL COMMENT '配置键',
    `config_value` TEXT                     DEFAULT NULL COMMENT '配置值',
    `description`  VARCHAR(255)             DEFAULT NULL COMMENT '配置描述',
    `update_by`    BIGINT                   DEFAULT NULL COMMENT '更新人',
    `update_time`  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '博客配置表';

DROP TABLE IF EXISTS `t_notification`;
CREATE TABLE `t_notification`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id`     BIGINT          NOT NULL COMMENT '接收人用户ID',
    `title`       VARCHAR(128)    NOT NULL COMMENT '通知标题',
    `content`     VARCHAR(512)             DEFAULT NULL COMMENT '通知内容',
    `type`        VARCHAR(16)     NOT NULL COMMENT '通知类型(AUDIT-审核,SYSTEM-系统,COMMENT-评论)',
    `related_id`  BIGINT                   DEFAULT NULL COMMENT '关联业务ID(如文章ID)',
    `is_read`     TINYINT         NOT NULL DEFAULT 0 COMMENT '是否已读(0-未读,1-已读)',
    `create_time` DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_user_read` (`user_id`, `is_read`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT = '站内通知表';
