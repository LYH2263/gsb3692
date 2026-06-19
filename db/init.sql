SET NAMES utf8mb4;

-- 创建数据库
CREATE DATABASE IF NOT EXISTS xiaohongshu DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE xiaohongshu;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `profile` VARCHAR(255) DEFAULT NULL COMMENT '个人简介',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 笔记表
CREATE TABLE IF NOT EXISTS `note` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `content` TEXT COMMENT '内容',
    `images` TEXT COMMENT '图片URL列表，逗号分隔',
    `category` VARCHAR(20) DEFAULT '全部' COMMENT '分类',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '评论者ID',
    `note_id` BIGINT NOT NULL COMMENT '笔记ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
    INDEX `idx_note_id` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 点赞表
CREATE TABLE IF NOT EXISTS `note_like` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `note_id` BIGINT NOT NULL COMMENT '笔记ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
    UNIQUE KEY `uk_user_note` (`user_id`, `note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表';

-- 收藏表
CREATE TABLE IF NOT EXISTS `note_collect` (
    `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `note_id` BIGINT NOT NULL COMMENT '笔记ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    UNIQUE KEY `uk_user_note` (`user_id`, `note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 初始化测试数据
-- 所有初始用户的默认密码均为 123456 的 BCrypt 加密密文
INSERT INTO `user` (`username`, `password`, `avatar`, `profile`) VALUES 
('admin', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=200', '小红书官方管理员，欢迎大家分享生活。'),
('test_user', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1599566150163-29194dcaad36?w=200', '摄影爱好者，记录每一个瞬间。'),
('xiaohong', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=200', '喜欢分享生活的每一天，关注我，不迷路。'),
('lily_smith', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200', '资深美食点评官'),
('traveler_bob', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200', '环球旅行家，已经在路上。'),
('tech_guru', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200', '专注前沿科技分享'),
('fashion_queen', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=200', '你的私人穿搭顾问'),
('sport_man', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=200', '健身狂热者'),
('nature_lover', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1554151228-14d9def656e4?w=200', '发现大自然的美'),
('coder_life', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=200', '代码改变世界'),
('music_fan', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1521119956141-1a4129ee15f9?w=200', '听见心跳的声音'),
('art_design', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1531123897727-8f129e1688ce?w=200', '设计改变生活'),
('yoga_girl', '$2a$10$uNeAibyvMaCe8BXwtnqgJOphYn4wcPk0hwlWmiPwLAH0JC94dK18a', 'https://images.unsplash.com/photo-1517841905240-472988babdf9?w=200', '静心瑜伽，寻找自我');

INSERT INTO `note` (`user_id`, `title`, `content`, `images`, `category`) VALUES 
(1, '今天的穿搭分享', '今天穿了一件白色的衬衫，感觉很清爽。', 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=500,https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=500', '时尚'),
(1, '好吃的日料店推荐', '这家日料店的寿司真的很新鲜，推荐给大家！', 'https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=500,https://images.unsplash.com/photo-1580822184713-fc5400e7fe10?w=500', '美食'),
(2, '周末去爬山', '周末和朋友一起去爬了山，空气真的很清新。', 'https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=500,https://images.unsplash.com/photo-1501555088652-021faa106b9b?w=500', '旅行'),
(2, '最近在看的书', '最近在看《百年孤独》，感触颇深。', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500,https://images.unsplash.com/photo-1512820790803-83ca734da794?w=500', '生活'),
(1, '春季护肤指南', '春天到了，皮肤容易过敏，大家要注意补水哦。', 'https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500,https://images.unsplash.com/photo-1570172619644-dfd03ed5d897?w=500', '生活'),
(1, '极简主义装修', '分享一下我家的装修风格，极简但不简单。', 'https://images.unsplash.com/photo-1484154218962-a197022b5858?w=500,https://images.unsplash.com/photo-1513519247388-19345420d4c7?w=500', '生活'),
(2, '成都美食地图', '成都的火锅真的是一绝，太好吃了！', 'https://images.unsplash.com/photo-1585032226651-759b368d7246?w=500,https://images.unsplash.com/photo-1563379926898-05f4575a45d8?w=500', '美食'),
(1, '年度好物分享', '今年买到的最满意的几样东西，强烈安利。', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500,https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', '生活'),
(2, '西藏自驾游', '人生总要去一次西藏，洗涤心灵的旅程。', 'https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=500,https://images.unsplash.com/photo-1454496522488-7a8e488e8606?w=500', '旅行'),
(1, '复古穿搭风格', '最近迷上了复古风，这种色调太有质感了。', 'https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=500,https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?w=500', '时尚'),
(2, '高效学习方法', '分享几个我一直在用的学习工具和方法。', 'https://images.unsplash.com/photo-1434030216411-0b793f4b4173?w=500,https://images.unsplash.com/photo-1456513080510-7bf3a84b82f8?w=500', '科技'),
(1, '自制下午茶', '今天自己动手做了一份下午茶，心情美美哒。', 'https://images.unsplash.com/photo-1552611052-33e04de081de?w=500,https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=500', '美食'),
(2, '运动打卡第一天', '开始健身啦，希望能坚持下去！', 'https://images.unsplash.com/photo-1517836357463-d25dfeac3438?w=500,https://images.unsplash.com/photo-1517130038641-a774d04afb3c?w=500', '运动'),
(1, '夏日清凉饮品', '教大家做几款好喝又简单的夏日冷饮。', 'https://images.unsplash.com/photo-1499638472904-ea5c6178a300?w=500,https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?w=500', '美食'),
(3, '上海迪士尼攻略', '避雷指南！一定要提前预约项目。', 'https://images.unsplash.com/photo-1505993597083-3bd19fb75e57?w=500,https://images.unsplash.com/photo-1533107862482-0e6974b06ec4?w=500', '旅行'),
(3, '宅家办公好物', '这款人体工学椅真的救了我的老腰。', 'https://images.unsplash.com/photo-1593642532842-98d0fd5ebc1a?w=500,https://images.unsplash.com/photo-1518455027359-f3f81390e686?w=500', '生活'),
(3, '如何拍出氛围感', '分享几个拍照构图小技巧，小白也能学。', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500,https://images.unsplash.com/photo-1452721226468-f9c902cbffaf?w=500', '生活'),
(3, '深夜食堂：螺蛳粉', '虽然闻着臭，但吃着是真的香啊！', 'https://images.unsplash.com/photo-1512058564366-18510be2db19?w=500,https://images.unsplash.com/photo-1552611052-33e04de081de?w=500', '美食'),
(3, '冬季滑雪初体验', '第一次滑雪，摔了好几个跟头，但很开心。', 'https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=500,https://images.unsplash.com/photo-1452933006409-19b87dc327b7?w=500', '运动'),
(3, 'OOTD：美拉德风', '秋天就是要穿得暖洋洋的。', 'https://images.unsplash.com/photo-1539109136881-3be0616acf4b?w=500,https://images.unsplash.com/photo-1551488831-00ddcb6c6bd3?w=500', '时尚'),
(3, '程序员的桌面', '简单清爽的桌面环境能提高工作效率。', 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=500,https://images.unsplash.com/photo-1504384308090-c894fdcc538d?w=500', '科技'),
(3, '手工DIY：手机壳', '自己动手做一个独一无二的手机壳。', 'https://images.unsplash.com/photo-1558021211-6d1403321394?w=500,https://images.unsplash.com/photo-1535157412991-2ef801c1748b?w=500', '生活'),
(3, '早起挑战：第7天', '坚持早起，感觉一整天的精神都很好。', 'https://images.unsplash.com/photo-1506126613408-eca07ce68773?w=500,https://images.unsplash.com/photo-1552650272-b8a34e21bc4b?w=500', '生活'),
(3, '周末露营日记', '远离城市喧嚣，在大自然里充电。', 'https://images.unsplash.com/photo-1504280390367-361c6d9f38f4?w=500,https://images.unsplash.com/photo-1533873984035-25970ab07461?w=500', '旅行');

INSERT INTO `comment` (`user_id`, `note_id`, `content`) VALUES 
(2, 1, '这件衬衫真好看！'),
(1, 3, '爬山好累但是风景很美。'),
(2, 2, '我也喜欢吃这家，味道很正宗。'),
(1, 5, '这装修风格我也很喜欢，很有质感。'),
(2, 6, '成都是真的好玩，火锅也是真的辣。'),
(1, 8, '西藏是我一直想去的地方，羡慕！'),
(2, 10, '这学习方法看起来很受用，收下了。'),
(3, 2, '看着就好吃，求店名！'),
(1, 15, '迪士尼确实需要攻略，不然排队排死。'),
(2, 17, '这拍照技巧太实用了，明天就去试试。'),
(3, 7, '成都是真的巴适。');

INSERT INTO `note_like` (`user_id`, `note_id`) VALUES 
(1, 3), (1, 5), (2, 1), (2, 2),
(4, 1), (4, 2), (4, 3), (4, 15), (4, 17),
(5, 3), (5, 9), (5, 15), (5, 24),
(6, 11), (6, 21), (6, 16),
(7, 1), (7, 10), (7, 20),
(8, 13), (8, 19),
(9, 3), (9, 9), (9, 24),
(10, 11), (10, 21),
(11, 18), (11, 12),
(12, 6), (12, 17), (12, 22),
(13, 13), (13, 19);

INSERT INTO `note_collect` (`user_id`, `note_id`) VALUES 
(1, 2), (1, 6), (2, 3), (2, 4),
(4, 1), (4, 15), (4, 17),
(5, 9), (5, 24),
(6, 21),
(7, 1), (7, 10), (7, 20),
(8, 19),
(12, 6), (12, 22);
