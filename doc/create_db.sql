
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
  `verify` varchar(2) comment '是否审核',
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
  `create_time` datetime comment '注册时间',
  `last_update_time` datetime comment '最后登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;