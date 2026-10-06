CREATE DATABASE IF NOT EXISTS `teasmile` DEFAULT CHARACTER SET utf8mb3 COLLATE utf8_bin;
USE `teasmile`;

DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='员工信息';

INSERT INTO `employee` VALUES (1,'管理员','admin','e10adc3949ba59abbe56e057f20f883e','13812341234','1','452225199001010047',1,'2026-06-15 15:31:20','2026-06-17 09:26:20',10,1);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `openid` varchar(45) COLLATE utf8_bin DEFAULT NULL COMMENT '微信用户唯一标识',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `id_number` varchar(18) COLLATE utf8_bin DEFAULT NULL COMMENT '身份证号',
  `avatar` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='用户信息';

DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `consignee` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4  DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认 0 否 1是',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='地址簿';

DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` int NOT NULL DEFAULT 1 COMMENT '分类类型：1饮品分类，2套餐分类',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT '0' COMMENT '分类排序权重',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态：0禁用，1启用',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='商品分类表';

INSERT INTO `category`(`name`, `type`, `sort`, `status`) VALUES
('冰淇淋大圣代', 1, 1, 1),
('清新果茶',     1, 2, 1),
('经典奶茶',     1, 3, 1),
('Coffee咖啡',   1, 4, 1),
('零食',         1, 5, 1),
('优惠套餐',     2, 6, 1);

DROP TABLE IF EXISTS `drink`;
CREATE TABLE `drink` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '饮品名称',
  `category_id` bigint NOT NULL COMMENT '所属分类id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售卖现价',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价（划线价）',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片url',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '饮品描述',
  `hot` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否热门：0否，1是',
  `sales` int NOT NULL DEFAULT 0 COMMENT '累计销量',
  `stock` int NOT NULL DEFAULT 0 COMMENT '成品库存数量',
  `sort` int NOT NULL DEFAULT 0 COMMENT '商品列表排序权重',
  `status` int DEFAULT '1' COMMENT '0停售 1起售',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人',
  `update_user` bigint DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_drink_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='饮品商品表';

INSERT INTO `drink`
(`name`,`category_id`,`price`,`original_price`,`image`,`description`,`hot`,`sales`,`stock`,`sort`,`status`)
VALUES
('芒果大圣代',1,6.00,8.00,'/static/logo.png','浓郁芒果+绵密冰淇淋',0,450,20,13,1),
('脆皮大圣代',1,6.00,8.00,'/static/logo.png','酥脆脆皮搭配绵密冰淇淋大圣代',0,420,20,10,1),
('黑糖珍珠大圣代',1,6.00,8.00,'/static/logo.png','Q弹黑糖珍珠搭配冰淇淋大圣代',0,430,20,11,1),
('草莓大圣代',1,6.00,8.00,'/static/logo.png','新鲜草莓果肉配上香甜冰淇淋',0,440,20,12,1),
('杨枝甘露',2,7.00,9.00,'https://pica.zhimg.com/v2-be0932f825258f40cab69b7ed55a2623_720w.jpg?source=172ae18b','新鲜芒果+西柚粒+西米露',1,900,30,1,1),
('水果撞奶',2,7.00,9.00,'/static/logo.png','多种鲜果搭配香浓牛奶',0,600,20,14,1),
('冰鲜柠檬水',2,4.00,6.00,'https://ts1.tc.mm.bing.net/th/id/OIP-C.o01sM1Ap6LSIIXPlMy1b9QHaHa?rs=1&pid=ImgDetMain&o=7&rm=3','新鲜柠檬+清爽冰饮',1,900,30,5,1),
('蜜桃四季春',2,8.00,10.00,'https://pic.nximg.cn/file/20240326/35258331_153019997102_2.jpg','水蜜桃+四季春茶底',1,900,30,2,1),
('香拧百香果',2,7.00,9.00,'/static/logo.png','香水柠檬+酸甜百香果',0,500,20,15,1),
('蓝莓果粒茶',2,7.00,9.00,'/static/logo.png','蓝莓果粒+清香茶底',0,580,20,16,1),
('珍珠奶茶',3,6.00,8.00,'/static/logo.png','经典珍珠+香浓奶茶',0,450,20,17,1),
('黑糖珍珠奶茶',3,8.00,10.00,'https://gd-hbimg.huaban.com/7711133be4a296fe81e6e5139c18fa272d3282e53050e-uxJGOj_fw658webp','黑糖挂壁+Q弹珍珠奶茶',1,900,30,3,1),
('布丁奶茶',3,6.00,8.00,'/static/logo.png','嫩滑布丁+醇香奶茶',0,520,20,18,1),
('双皮奶',3,6.00,8.00,'/static/logo.png','顺德风味嫩滑双皮奶',0,520,20,19,1),
('冰美式',4,7.00,9.00,'/static/logo.png','醇香浓缩+清爽冰美式',0,600,20,20,1),
('生椰拿铁',4,9.00,11.00,'https://gd-hbimg.huaban.com/678115767e14d507e8f0bf22a56cadeb6efd6177d005b-AmsM7q_fw658','新鲜生椰+浓缩咖啡',1,890,30,4,1),
('黑糖拿铁',4,9.00,11.00,'/static/logo.png','黑糖风味+丝滑拿铁',0,600,20,21,1),
('冷萃咖啡',4,7.00,9.00,'/static/logo.png','低温慢萃清爽咖啡',0,580,20,22,1),
('卫龙的辣条',5,1.50,2.00,'/static/logo.png','经典卫龙辣条，解馋小零食',0,400,20,23,1),
('生日小蛋糕',5,4.00,6.00,'/static/logo.png','迷你可爱小蛋糕',0,420,20,24,1);

DROP TABLE IF EXISTS `drink_flavor`;
CREATE TABLE `drink_flavor` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `drink_id` bigint NOT NULL COMMENT '关联饮品id',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '规格名称：甜度｜冰度｜加料',
  `value` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '可选值JSON数组',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='饮品口味规格表';

INSERT INTO `drink_flavor`(`drink_id`,`name`,`value`) VALUES
-- 杨枝甘露 drink_id=5
(5,'甜度','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]'),
(5,'冰度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]'),
(5,'加料','[\"西米多加\",\"西柚粒多加\",\"不加额外加料\"]'),
-- 蜜桃四季春 drink_id=8
(8,'甜度','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]'),
(8,'冰度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]'),
-- 黑糖珍珠奶茶 drink_id=12
(12,'甜度','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]'),
(12,'冰度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]'),
-- 生椰拿铁 drink_id=16
(16,'甜度','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]'),
(16,'冰度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]'),
-- 脆皮大圣代 drink_id=2
(2,'甜度','[\"少糖\",\"正常糖\"]'),
(2,'冰度','[\"常温\",\"少冰\"]'),
(2,'加料','[\"奥利奥碎\",\"花生碎\",\"不额外加料\"]'),
-- 黑糖珍珠大圣代 drink_id=3
(3,'甜度','[\"少糖\",\"正常糖\",\"多糖\"]'),
(3,'冰度','[\"常温\",\"少冰\"]'),
(3,'加料','[\"奥利奥碎\",\"花生碎\",\"椰果\",\"不加料\"]'),
-- 草莓大圣代 drink_id=4
(4,'甜度','[\"少糖\",\"正常糖\"]'),
(4,'冰度','[\"常温\",\"少冰\"]'),
(4,'加料','[\"奥利奥碎\",\"花生碎\",\"草莓碎\",\"不额外加料\"]');


DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '所属套餐分类id（category.type=2）',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '套餐售卖价格',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '套餐原价（划线价）',
  `status` int DEFAULT '1' COMMENT '售卖状态 0:停售 1:起售',
  `description` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐描述',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '套餐封面图片',
  `hot` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否热门套餐 0否1是',
  `sales` int NOT NULL DEFAULT 0 COMMENT '套餐销量',
  `stock` int NOT NULL DEFAULT 0 COMMENT '套餐库存',
  `sort` int NOT NULL DEFAULT 0 COMMENT '套餐排序权重',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_user` bigint DEFAULT NULL COMMENT '创建人id',
  `update_user` bigint DEFAULT NULL COMMENT '修改人id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='奶茶套餐表';

DROP TABLE IF EXISTS `setmeal_drink`;
CREATE TABLE `setmeal_drink` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id',
  `drink_id` bigint DEFAULT NULL COMMENT '饮品id',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '饮品名称【冗余快照】',
  `price` decimal(10,2) DEFAULT NULL COMMENT '饮品单价【冗余快照】',
  `copies` int DEFAULT NULL COMMENT '该套餐内此饮品的份数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='套餐和饮品关联中间表';

INSERT INTO `setmeal`
(`name`,`category_id`,`price`,`original_price`,`image`,`description`,`hot`,`sales`,`stock`,`sort`,`status`)
VALUES
('双人欢乐果茶套餐',6,13.00,16.00,'/static/logo.png','杨枝甘露+蜜桃四季春，双人分享套餐',1,120,20,1,1);

INSERT INTO `setmeal_drink`(`setmeal_id`,`drink_id`,`name`,`price`,`copies`)
VALUES
(1,5,'杨枝甘露',7.00,1),
(1,8,'蜜桃四季春',8.00,1);

DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称冗余快照',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片快照',
  `user_id` bigint NOT NULL COMMENT '所属C端用户id',
  `drink_id` bigint DEFAULT NULL COMMENT '饮品id，选购单品时填写',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id，选购套餐时填写',
  `drink_flavor` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户选中的口味规格JSON，套餐时可为null',
  `number` int NOT NULL DEFAULT '1' COMMENT '购买数量',
  `amount` decimal(10,2) NOT NULL COMMENT '该条购物车小计金额',
  `create_time` datetime DEFAULT NULL COMMENT '加入购物车时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='购物车表';

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '业务订单唯一编号',
  `status` int NOT NULL DEFAULT '1' COMMENT '订单状态：1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
  `user_id` bigint NOT NULL COMMENT '下单C端用户id',
  `address_book_id` bigint NOT NULL COMMENT '收货地址簿id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `checkout_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `pay_method` int NOT NULL DEFAULT '1' COMMENT '支付方式：1微信，2支付宝',
  `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT '支付状态：0未支付，1已支付，2退款',
  `amount` decimal(10,2) NOT NULL COMMENT '订单实付总金额',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '用户下单备注',
  `phone` varchar(11) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人手机号【快照冗余】',
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '完整收货地址【快照冗余】',
  `user_name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '用户姓名【快照冗余】',
  `consignee` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '收货人【快照冗余】',
  `cancel_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '订单取消原因',
  `rejection_reason` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商家拒单原因',
  `cancel_time` datetime DEFAULT NULL COMMENT '订单取消时间',
  `estimated_delivery_time` datetime DEFAULT NULL COMMENT '预计送达时间',
  `delivery_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '配送状态：1立即送出，0选择指定时间',
  `delivery_time` datetime DEFAULT NULL COMMENT '实际送达时间',
  `pack_amount` int DEFAULT 0 COMMENT '打包费',
  `tableware_number` int DEFAULT NULL COMMENT '餐具数量',
  `tableware_status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '餐具模式：1按餐量提供，0自定义数量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单主表';

DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称快照',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '商品图片快照',
  `order_id` bigint NOT NULL COMMENT '关联订单主表id',
  `drink_id` bigint DEFAULT NULL COMMENT '饮品id，单品下单填写',
  `setmeal_id` bigint DEFAULT NULL COMMENT '套餐id，套餐下单填写',
  `drink_flavor` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '下单选择口味规格快照，套餐可为null',
  `number` int NOT NULL DEFAULT '1' COMMENT '购买份数',
  `amount` decimal(10,2) NOT NULL COMMENT '本条明细小计金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb3 COLLATE=utf8_bin COMMENT='订单明细表';
