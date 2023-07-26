package com.neu.community.service;


import com.neu.community.dao.elasticsearch.DiscussPostRepository;
import com.neu.community.entity.DiscussPost;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ElasticsearchService {

    @Autowired
    private DiscussPostRepository repository;

    @Autowired
    private ElasticsearchRestTemplate template;

    public void saveDiscussPost(DiscussPost post) {
        repository.save(post);
    }

    public void deleteDiscussPost(int id) {
        repository.deleteById(id);
    }

    public Map<String, Object> searchDiscussPost(String keyword, int current, int limit) {
        Query query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(
                        SortBuilders.fieldSort("type").order(SortOrder.DESC),
                        SortBuilders.fieldSort("score").order(SortOrder.DESC),
                        SortBuilders.fieldSort("createTime").order(SortOrder.DESC)
                )
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields(
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();
        SearchHits<DiscussPost> searchHits = template.search(query, DiscussPost.class);
        Map<String, Object> map = new HashMap<>();
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
            list.add(post);
        }
        map.put("searchResult", list);
        map.put("resultCount", searchHits.getTotalHits());
        return map;
    }
}
