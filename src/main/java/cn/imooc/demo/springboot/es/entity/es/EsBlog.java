package cn.imooc.demo.springboot.es.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

/**
 * 当感知到应景引入依赖之后，会查看所有的document实体，如果document里面没有配置useServerConfiguration(线上的配置)，
 * 对document任何的编码的配置都会在springboot中启动时同步到集群中去
 * createIndex如果不是false的话会把线上的blog的index删除掉，然后重新创建blog的index
 */
@Data
@Document(indexName = "blog",type = "doc",useServerConfiguration = true,createIndex = false)
public class EsBlog {

    @Id
    private Integer id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String author;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String content;

    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd MM:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date createTime;

    @Field(type = FieldType.Date,format = DateFormat.custom,pattern = "yyyy-MM-dd MM:mm:ss||yyyy-MM-dd||epoch_millis")
    private Date updateTime;
}
