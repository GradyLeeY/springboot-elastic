package cn.imooc.demo.springboot.es.entity.mysql;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/*    DROP TABLE IF EXISTS `t_blog`;
    CREATE TABLE `t_blog` (
            `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '电影id',
            `title` varchar(64) DEFAULT NULL COMMENT '电影名字',
            `author` varchar(64) DEFAULT NULL COMMENT '电影类型',
            `content` mediumtext DEFAULT NULL COMMENT '上映时间',
            `create_time` datetime DEFAULT NULL COMMENT '电影时长',
            `update_time` datetime DEFAULT NULL COMMENT '电影主演',

    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
*/

@Data
@Table(name = "t_blog")
@Entity
public class MysqlBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String author;
    @Column(columnDefinition = "mediumtext")
    private String content;
    private Date createTime;
    private Date updateTime;


}
