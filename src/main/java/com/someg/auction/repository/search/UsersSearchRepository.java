package com.someg.auction.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.someg.auction.domain.Users;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Users} entity.
 */
public interface UsersSearchRepository extends ElasticsearchRepository<Users, Long>, UsersSearchRepositoryInternal {}

interface UsersSearchRepositoryInternal {
    Page<Users> search(String query, Pageable pageable);
}

class UsersSearchRepositoryInternalImpl implements UsersSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    UsersSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Users> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Users> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Users.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
