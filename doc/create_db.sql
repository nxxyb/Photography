
/**建立数据库*/
CREATE DATABASE `photography` DEFAULT CHARACTER SET utf8;

use `photography`;

/**用户表 */
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `login_name` varchar(40) comment '登录名称',
  `password` varchar(255) comment '密码',
  `type` varchar(2) comment '用户类型',
  `nack_name` varchar(40) comment '昵称',
  `real_name` varchar(40) comment '真实姓名',
  `sex` varchar(2) comment '性别',
  `mobile` varchar(20) comment '手机号码',
  `email` varchar(20) comment '邮箱',
  `birth_day` date comment '生日',
  `verify` varchar(2) comment '审核状态',
  `verify_time` datetime comment '审核时间',
  `verify_message` varchar(2000) comment '审核结果信息',
  `enable` varchar(2) comment '是否激活',
  `province` varchar(20) comment '省',
  `city` varchar(20) comment '市',
  `county` varchar(20) comment '区',
  `id_card` varchar(20) comment '身份证号',
  `company_name` varchar(20) comment '单位名称',
  `comfirm_pic` varchar(300) comment '验证照片地址',
  `head_pic` varchar(300) comment '头像照片地址',
  `qq_number` varchar(40) comment 'QQ',
  `person_signature` varchar(300) comment '个性签名',
  `coupon_num` varchar(20) comment '胶卷数量',
  `create_time` datetime comment '注册时间',
  `last_update_time` datetime comment '最后登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/**活动表 */
DROP TABLE IF EXISTS `project`;
CREATE TABLE `project` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `name` varchar(200) comment '活动名称',
  `type` varchar(10) comment '活动类型',
  `start_time` datetime comment '活动开始时间',
  `time_length` varchar(50) comment '活动时长(天)',
  `time_night_length` varchar(50) comment '活动时长(晚)',
  `people_num` varchar(50) comment '活动人数',
  `less_num` varchar(50) comment '最低人数',
  `early_days` datetime comment '提前报名天数',
  `model_num` varchar(50) comment '模特数量',
  `place` varchar(200) comment '活动地点',
  `venue_place` varchar(200) comment '集合地点',
  `destination_place` varchar(200) comment '目的地点',
  `province` varchar(20) comment '省',
  `city` varchar(20) comment '市',
  `county` varchar(20) comment '区',
  `contact` varchar(200) comment '联系方式',
  `des` varchar(2000) comment '活动介绍',
  `fee_des` varchar(2000) comment '费用介绍',
  `xc_des` varchar(2000) comment '行程介绍',
  `cost` varchar(300) comment '活动费用',
  `photos` varchar(500) comment '活动照片',
  `des_photos` varchar(500) comment '介绍图片',
  `create_user` varchar(36) comment '创建用户',
  `joined_number` varchar(10) comment '已参加人数',
  `viewed_number` varchar(10) comment '浏览次数',
  `status` varchar(10) comment '活动状态 1-未开始 2-已开始 3-结束',
  `verify` varchar(2) comment '审核状态',
  `verify_message` varchar(2000) comment '审核结果信息',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**活动行程表 */
DROP TABLE IF EXISTS `project_trip`;
CREATE TABLE `project_trip` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `title` varchar(200) comment '行程标题',
  `des` varchar(2000) comment '行程介绍',
  `des_photos` varchar(500) comment '行程介绍图片',
  `project` varchar(36) comment '所属活动',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  

/**文件组 */
DROP TABLE IF EXISTS `file_group`;
CREATE TABLE `file_group` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `create_time` datetime comment '注册时间',
  `last_update_time` datetime comment '最后登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**文件表 */
DROP TABLE IF EXISTS `file_info`;
CREATE TABLE `file_info` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `real_name` varchar(200) comment '真实文件名',
  `real_path` varchar(500) comment '存储文件全路径',
  `ext` varchar(20) comment '扩展名',
  `file_group` varchar(36) comment '附件组',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**活动订单表 */
DROP TABLE IF EXISTS `project_order`;
CREATE TABLE `project_order` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `user` varchar(36) comment '用户',
  `project` varchar(36) comment '活动',
  `status` varchar(10) comment '状态 1-未支付 2-已取消  3-已支付',
  `number` varchar(10) comment '数量',
  `unit_price` varchar(10) comment '单价',
  `coupon` varchar(10) comment '使用胶卷数',
  `original_price` varchar(10) comment '原总价',
  `actual_price` varchar(10) comment '实际支付总价',
  `is_comment` varchar(2) comment '是否评价',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**活动评论表 */
DROP TABLE IF EXISTS `project_comment`;
CREATE TABLE `project_comment` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `create_user` varchar(36) comment '用户',
  `project` varchar(36) comment '活动',
  `type` varchar(10) comment '评论类型  1-好评  2-中评  3-差评',
  `photos` varchar(500) comment '评论图片',
  `content` varchar(2000) comment '评论内容',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**活动收藏表 */
DROP TABLE IF EXISTS `project_collect`;
CREATE TABLE `project_collect` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `project` varchar(36) comment '活动',
  `user` varchar(36) comment '用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**作品表 */
DROP TABLE IF EXISTS `work`;
CREATE TABLE `work` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `name` varchar(200) comment '名称',
  `des` varchar(2000) comment '介绍',
  `cost` varchar(300) comment '费用',
  `photos` varchar(500) comment '照片',
  `create_user` varchar(36) comment '创建用户',
  `joined_number` varchar(10) comment '已订购人数',
  `viewed_number` varchar(10) comment '浏览次数',
  `verify` varchar(2) comment '审核状态',
  `verify_message` varchar(2000) comment '审核结果信息',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**作品订单表 */
DROP TABLE IF EXISTS `work_order`;
CREATE TABLE `work_order` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `user` varchar(36) comment '用户',
  `work` varchar(36) comment '作品',
  `status` varchar(10) comment '状态 1-未支付 2-已取消  3-已支付',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**作品评论表 */
DROP TABLE IF EXISTS `work_comment`;
CREATE TABLE `work_comment` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `create_user` varchar(36) comment '用户',
  `work` varchar(36) comment '作品',
  `type` varchar(10) comment '评论类型  1-好评  2-中评  3-差评',
  `photos` varchar(500) comment '评论图片',
  `content` varchar(2000) comment '评论内容',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**活动收藏表 */
DROP TABLE IF EXISTS `work_collect`;
CREATE TABLE `work_collect` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `work` varchar(36) comment '作品',
  `user` varchar(36) comment '用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**胶卷记录表 */
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `coupon_num` varchar(36) comment '胶卷数量',
  `type` varchar(10) comment '胶卷类型',
  `in_or_exp` varchar(10) comment '胶卷类别  1-收入  2-支出',
  `message` varchar(2000) comment '信息',
  `user` varchar(36) comment '用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**派文表*/
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `content` varchar(2000) comment '内容',
  `video` varchar(1000) comment '视频',
  `photos` varchar(500) comment '图片',
  `create_user` varchar(36) comment '用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**派友表*/
DROP TABLE IF EXISTS `blog_friend`;
CREATE TABLE `blog_friend` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `create_user` varchar(36) comment '创建用户',
  `friend_user` varchar(36) comment '好友用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**派文评论表*/
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `blog` varchar(36) comment '派文',
  `content` varchar(2000) comment '内容',
  `create_user` varchar(36) comment '创建用户',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**评论回复表*/
DROP TABLE IF EXISTS `comment_reploy`;
CREATE TABLE `comment_reploy` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `content` varchar(2000) comment '内容',
  `create_user` varchar(36) comment '创建用户',
  `project_comment` varchar(36) comment '活动评论',
  `work_comment` varchar(36) comment '作品评论',
  `blog_comment` varchar(36) comment '派文评论',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/**点赞表*/
DROP TABLE IF EXISTS `love`;
CREATE TABLE `love` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `create_user` varchar(36) comment '创建用户',
  `project` varchar(36) comment '活动',
  `work` varchar(36) comment '作品',
  `blog` varchar(36) comment '派文',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** 后台管理 */
/**首页配置表 */
DROP TABLE IF EXISTS `admin_lb`;
CREATE TABLE `admin_lb` (
  `id` varchar(36) NOT NULL PRIMARY KEY,
  `project` varchar(36) comment '所属活动',
  `work` varchar(36) comment '作品',
  `sort` varchar(10) comment '排序',
  `type` varchar(10) comment '类型',
  `create_time` datetime comment '创建时间',
  `last_update_time` datetime comment '最后修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/** 初始化管理员用户 */
INSERT INTO `user` (`id`, `login_name`, `password`, `type`, `nack_name`, `real_name`, `sex`, `mobile`, `email`, `birth_day`, `verify`, `verify_time`, `verify_message`, `enable`, `province`, `city`, `county`, `id_card`, `company_name`, `comfirm_pic`, `qq_number`, `person_signature`, `coupon_num`, `create_time`, `last_update_time`, `head_pic`) VALUES ('297ea9d450d5addf0150d5cc3c840006', NULL, '21232F297A57A5A743894A0E4A801FC3', '1', NULL, '管理员', NULL, '13920672357', NULL, NULL, '0', NULL, NULL, '1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '0', '2015-11-05 11:59:03', '2015-11-05 11:59:03', NULL);

