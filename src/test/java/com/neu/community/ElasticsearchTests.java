package com.neu.community;

import com.neu.community.dao.DiscussPostMapper;
import com.neu.community.dao.elasticsearch.DiscussPostRepository;
import com.neu.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
public class ElasticsearchTests {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    // private ElasticsearchTemplate elasticsearchTemplate;
    // ElasticsearchTemplate class which was deprecated since Spring Data Elasticsearch 4.0 has been removed
    // Connections to Elasticsearch must be made using either the imperative ElasticsearchRestTemplate or the reactive ReactiveElasticsearchTemplate.
    // https://docs.spring.io/spring-data/elasticsearch/docs/4.4.13/reference/html/
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Test
    public void testInsert () {
        discussPostRepository.save(discussPostMapper.selectDiscussPostByID(241));
        discussPostRepository.save(discussPostMapper.selectDiscussPostByID(242));
        discussPostRepository.save(discussPostMapper.selectDiscussPostByID(243));
    }

    @Test
    public void testInsertList () {
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(103, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(111, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(112, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(131, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(132, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(133, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.selectDiscussPosts(134, 0, 100));
    }

    @Test
    public void testUpdate () {
        DiscussPost post = discussPostMapper.selectDiscussPostByID(231);
        post.setContent("我是新人，使劲灌水！");
        discussPostRepository.save(post);
    }

    @Test
    public void testDelete () {
        // discussPostRepository.deleteById(231);
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearch () {
        // 高亮显示：在匹配到的字符前后加标签
        // The SearchQuery interface has been merged into it’s base interface Query, so it’s occurrences can just be replaced with Query.
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .withSorts(
                        SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC)
                )
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> searchHits = elasticsearchRestTemplate.search(query, DiscussPost.class);
        System.out.println(searchHits.getTotalHits());
        SearchPage<DiscussPost> searchPage = SearchHitSupport.searchPageFor(searchHits, query.getPageable());
        System.out.println(searchPage.getTotalElements()); // 总条数
        System.out.println(searchPage.getTotalPages()); // 总页数
        System.out.println(searchPage.getNumber()); // 当前页
        System.out.println(searchPage.getSize()); // 页的大小

        List<DiscussPost> list = new ArrayList<>();
        for (SearchHit<DiscussPost> hit : searchHits) {
            DiscussPost post = hit.getContent();
            List<String> title = hit.getHighlightField("title"); // 返回值永不为null
            if (!title.isEmpty()) {
                post.setTitle(title.toString());
            }
            List<String> content = hit.getHighlightField("content");
            if (!content.isEmpty()) {
                post.setContent(content.toString());
            }
            System.out.println(post);
            list.add(post);
        }
    }
}
