package com.hyc.springbootinit.esdao;

import com.hyc.springbootinit.model.dto.post.PostEsDTO;
import com.hyc.springbootinit.model.dto.post.PostQueryRequest;
import com.hyc.springbootinit.model.entity.Post;
import com.hyc.springbootinit.service.PostService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.annotation.Resource;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.AggregationsContainer;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

/**
 * 帖子 ES 操作测试
 *
 * @author hyc
 */
@SpringBootTest
public class PostEsDaoTest {

    @Resource
    private PostEsDao postEsDao;

    @Resource
    private PostService postService;

    @Resource
    private ElasticsearchRestTemplate restTemplate;

    @Test
    void test() {
        PostQueryRequest postQueryRequest = new PostQueryRequest();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Post> page =
                postService.searchFromEs(postQueryRequest);
        System.out.println(page);
    }

    @Test
    void testSelect() {
        System.out.println(postEsDao.count());
        Page<PostEsDTO> PostPage = postEsDao.findAll(
                PageRequest.of(0, 5, Sort.by("createTime")));
        List<PostEsDTO> postList = PostPage.getContent();
        System.out.println(postList);
    }

    @Test
    void testAdd() {
        PostEsDTO postEsDTO = new PostEsDTO();
        postEsDTO.setId(1658875589511028737L);
        postEsDTO.setTitle("Java从入门到跑路");
        postEsDTO.setContent("作为一门广泛应用于软件开发领域的编程语言，Java深受开发者的喜爱。无论你是初学者还是有一些编程经验的开发者，掌握Java都是一项有价值的技能。本文将引导你从Java的入门阶段开始，一直到能够自如地运用它开发实际项目，成为一名合格的Java开发者。");
        postEsDTO.setTags(Arrays.asList("Java","学习","入门"));
        postEsDTO.setUserId(1658872915625451521L);
        postEsDTO.setCreateTime(new Date());
        postEsDTO.setUpdateTime(new Date());
        postEsDTO.setIsDelete(0);
        postEsDao.save(postEsDTO);
        System.out.println(postEsDTO.getId());
    }

    @Test
    void testFindById() {
        Optional<PostEsDTO> postEsDTO = postEsDao.findById(1L);
        System.out.println(postEsDTO);
    }

    @Test
    void testCount() {
        System.out.println(postEsDao.count());
    }

    @Test
    void testFindByCategory() {
        List<PostEsDTO> postEsDaoTestList = postEsDao.findByUserId(1L);
        System.out.println(postEsDaoTestList);
    }

    /** 搜索全部数据 , 分页显示 ， 按 createTime字段降序 排序 */
    @Test
    void testQueryAll() {
        // 构建搜索条件 搜索全部
        MatchAllQueryBuilder builder = new MatchAllQueryBuilder();
        // 分页
        PageRequest pageRequest = PageRequest.of(0, 5);
        // 排序
        FieldSortBuilder order = new FieldSortBuilder("createTime").order(SortOrder.ASC);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder)
                .withPageable(pageRequest)
                .withSorts(order)
                .build();
        // 执行查询
        SearchHits<PostEsDTO> hits = restTemplate.search(query, PostEsDTO.class);
        List<PostEsDTO> dtoList = new ArrayList<>();
        for (SearchHit<PostEsDTO> hit : hits) {
            dtoList.add(hit.getContent());
        }
        System.out.println(dtoList);

    }

    /**
     * 条件搜索
     */
    @Test
    void searchByTerm() {
        MatchQueryBuilder builder = new MatchQueryBuilder("title", "MySQL必知必会");
        MatchQueryBuilder builder1 = new MatchQueryBuilder("id", 1);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(builder1)
                .build();
        SearchHits<PostEsDTO> hits = restTemplate.search(query, PostEsDTO.class);
        for (SearchHit<PostEsDTO> hit : hits) {
            System.out.println(hit.getContent());
        }

    }

    /**
     * 组合搜索
     */
    @Test
    void combinedSearch() {
        // 搜索content必须包括Java和计算机的帖子
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.matchQuery("content", "java"));
        boolQuery.must(QueryBuilders.matchQuery("content", "smile"));
        // 查询
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        NativeSearchQuery searchQuery = builder.withQuery(boolQuery).build();
        SearchHits<PostEsDTO> hits = restTemplate.search(searchQuery, PostEsDTO.class);
        for (SearchHit<PostEsDTO> hit : hits) {
            System.out.println(hit.getContent());
        }


    }

    /**
     * 过滤查询
     */
    @Test
    void filterSearch() {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        RangeQueryBuilder rangeQueryBuilder = new RangeQueryBuilder("id").gte(1).lt(3);
        boolQueryBuilder.filter(rangeQueryBuilder);
        NativeSearchQuery query = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();
        SearchHits<PostEsDTO> hits = restTemplate.search(query, PostEsDTO.class);
        for (SearchHit<PostEsDTO> hit : hits) {
            System.out.println(hit.getContent());
        }

    }

    /**
     * 聚合搜索
     */
    @Test
    void aggsSearch() {
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withAggregations(AggregationBuilders.terms("count").field("id"))
                .build();
        SearchHits<PostEsDTO> hits = restTemplate.search(query, PostEsDTO.class);
        // 取出聚合的结果
        AggregationsContainer<?> aggregations = hits.getAggregations();

    }





}
