# springboot-elastic
springboot集成elasticsearch

本项目使用的elasticsearch,logstash,kibana的版本均为6.3.2
从实际需求打造个人博客检索系统，内容涵盖es到mysql的数据同步以及springboot操作es。
es和关系型数据亏对比:
mysql                 es
database             index
table                type
row                  document
column               feild
schema               mapping

mysql:select*from blog.t_blog where name = "张三"
es:get /blog/t_blog/_search?q=name="张三"

使用postman和es进行交互：

Get 查看所有索引

localhost:9200/_all

PUT 创建索引-test

localhost:9200/test  


DEL 删除索引-test

localhost:9200/test  


PUT 创建索引-person-1

localhost:9200/person


PUT 新增数据-person-1

localhost:9200/person/_doc/1

{
	"first_name" : "John",
  "last_name" : "Smith",
  "age" : 25,
  "about" : "I love to go rock climbing",
  "interests" : [ "sports", "music" ]
}

PUT 新增数据-person-2

localhost:9200/person/_doc/2

{
	"first_name" : "Eric",
  "last_name" : "Smith",
  "age" : 23,
  "about" : "I love basketball",
  "interests" : [ "sports", "reading" ]
}

GET 搜索数据-person-id

localhost:9200/person/_doc/1

GET 搜索数据-person-name

localhost:9200/person/_doc/_search?q=first_name:john

{
  "took": 56,
  "timed_out": false,
  "_shards": {
    "total": 1,
    "successful": 1,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": {
      "value": 1,
      "relation": "eq"
    },
    "max_score": 0.6931472,
    "hits": [
      {
        "_index": "person",
        "_type": "_doc",
        "_id": "1",
        "_score": 0.6931472,
        "_source": {
          "first_name": "John",
          "last_name": "Smith",
          "age": 25,
          "about": "I love to go rock climbing",
          "interests": [
            "sports",
            "music"
          ]
        }
      }
    ]
  }
}


使用kibana和es进行交互
GET _search
{
  "query": {
    "match_all": {}
  }
}

GET _all

GET /person/_doc/1

POST /person/_search
{
	"query": {
  	"bool": {
			"should": [
      	{"match": {
        		"first_name": "Eric"
        	}
   			}
      ]
  	}
}

POST /person/_search
{
	"query": {
  	"bool": {
			"should": [
      	{"match": {
        		"last_name": "Smith"
        	}
   			},
        {
        	"match": {
          	"about": "basketball"
          }
				}
      ]
  	}
  }
}



POST /person/_search
{
	"query": {
  	"bool": {
			"must": [
      	{"match": {
        		"last_name": "Smith"
        	}
   			},
        {
        	"match": {
          	"about": "basketball"
          }
				}
      ]
  	}
  }
}

创建sql表
CREATE DATABASE blog;

USE blog;

CREATE TABLE `t_blog` (    
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',    
   `title` varchar(60) DEFAULT NULL COMMENT '博客标题',    
   `author` varchar(60) DEFAULT NULL COMMENT '博客作者',    
   `content` mediumtext COMMENT '博客内容',    
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',    
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',    
   PRIMARY KEY (`id`)    
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4


# 自己造的几条数据
INSERT INTO `blog`.`t_blog`(`id`, `title`, `author`, `content`, `create_time`, `update_time`) VALUES (1, 'Springboot 为什么这', 'bywind', '没错 Springboot ', '2019-12-08 01:44:29', '2019-12-08 01:44:34');
INSERT INTO `blog`.`t_blog`(`id`, `title`, `author`, `content`, `create_time`, `update_time`) VALUES (3, 'Springboot 中 Redis', 'bywind', 'Spring Boot', '2019-12-08 01:44:29', '2019-12-08 01:44:29');
INSERT INTO `blog`.`t_blog`(`id`, `title`, `author`, `content`, `create_time`, `update_time`) VALUES (4, 'Springboot 中如何优化', 'bywind', NULL, '2019-12-08 01:44:29', '2019-12-08 01:44:29');
INSERT INTO `blog`.`t_blog`(`id`, `title`, `author`, `content`, `create_time`, `update_time`) VALUES (5, 'Springboot 消息队列', 'bywind', NULL, '2019-12-08 01:44:29', '2019-12-08 01:44:29');
INSERT INTO `blog`.`t_blog`(`id`, `title`, `author`, `content`, `create_time`, `update_time`) VALUES (6, 'Docker Compose + Springboot', 'bywind', NULL, '2019-12-08 01:44:29', '2019-12-08 01:44:29');


es中新建mysql.config文件与mysql进行交互
input{
    jdbc{
        # jdbc驱动包位置
        jdbc_driver_library => "/mysql-connector-java-5.1.31.jar"
        # 要使用的驱动包类
        jdbc_driver_class => "com.mysql.jdbc.Driver"
        # mysql数据库的连接信息
        jdbc_connection_string => "jdbc:mysql://127.0.0.1:3306/blog"
        # mysql用户
        jdbc_user => "root"
        # mysql密码
        jdbc_password => "root"
        # 定时任务，多久执行一次查询，默认一分钟，如果想要没有延迟，可以使用 schedule => "* * * * * *"
        schedule => "* * * * *"
        # 清空上传的sql_last_value记录
        clean_run => true
        # 你要执行的语句
        statement => "select * FROM t_blog WHERE update_time > :sql_last_value AND update_time < NOW() ORDER BY update_time desc"
    }
}

output {
    elasticsearch{
        # es host : port
        hosts => ["127.0.0.1:9200"]
        # 索引
        index => "blog"
        # _id
        document_id => "%{id}"
    }
}
https://github.com/medcl/elasticsearch-analysis-ik/releases 下载ik分词器
在kibana中进行ik分词器的测试
POST _analyze
{
  "analyzer": "ik_max_word",
  "text" : "我是中国人"
}
最后在idea中创建springboot项目，启动，ok
