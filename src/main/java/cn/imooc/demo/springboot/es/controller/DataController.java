package cn.imooc.demo.springboot.es.controller;

import cn.imooc.demo.springboot.es.entity.es.EsBlog;
import cn.imooc.demo.springboot.es.entity.mysql.MysqlBlog;
import cn.imooc.demo.springboot.es.repository.es.EsBlogRepository;
import cn.imooc.demo.springboot.es.repository.mysql.MysqlBlogRepository;
import cn.imooc.demo.springboot.es.util.Const;
import cn.imooc.demo.springboot.es.util.Param;
import cn.imooc.demo.springboot.es.util.ServerResponse;
import lombok.Data;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/*POST  /blog/_search
            {
                "query": {
                "bool": {
                    "should": [
                    {
                        "match_phrase": {
                        "title": "spring"
                    }
                    },
                    {
                        "match_phrase": {
                        "content": "spring"
                    }
                    }
      ]
                }
            }
            }*/
@RestController
public class DataController {

    @Autowired
    private MysqlBlogRepository mysqlBlogRepository;
    @Autowired
    private EsBlogRepository esBlogRepository;

    @GetMapping("/blogs")
    public List<MysqlBlog> blog(){
        List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryAll();
        return mysqlBlogs;
    }

    @GetMapping("/blog/{id}")
    public Object blog(@PathVariable("id")Integer id){
        Optional<MysqlBlog> byId = mysqlBlogRepository.findById(id);
        MysqlBlog mysqlBlog = byId.get();
        return mysqlBlog;
    }
    @PostMapping("/search")
    public Object search(@RequestBody Param param){
        String type = param.getType();
        Map<String,Object> map = new HashMap<>();
        //统计耗时
        StopWatch watch = new StopWatch();
        watch.start();
        if (type.equalsIgnoreCase(Const.TYPE.MYSQL.getType())){
            List<MysqlBlog> mysqlBlogs = mysqlBlogRepository.queryBlogs(param.getKeyword());
            map.put("list",mysqlBlogs);
        }else if (type.equalsIgnoreCase(Const.TYPE.ELASTICSEARCH.getType())){
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title",param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("content",param.getKeyword()));
            String s = builder.toString();
            System.out.println(s);
            Page<EsBlog> search = (Page<EsBlog>)esBlogRepository.search(builder);
            List<EsBlog> content = search.getContent();
            map.put("list",content);
        }else {
            return ServerResponse.createByError();
        }
        watch.stop();
        long totalTimeMillis = watch.getTotalTimeMillis();
        map.put("duration",totalTimeMillis);
        return map;
    }

}
