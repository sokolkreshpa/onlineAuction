package com.someg.auction.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.someg.auction.domain.Auction;
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
 * Spring Data Elasticsearch repository for the {@link Auction} entity.
 */
public interface AuctionSearchRepository extends ElasticsearchRepository<Auction, Long>, AuctionSearchRepositoryInternal {}

interface AuctionSearchRepositoryInternal {
    Page<Auction> search(String query, Pageable pageable);
}

class AuctionSearchRepositoryInternalImpl implements AuctionSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    AuctionSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<Auction> search(String query, Pageable pageable) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        nativeSearchQuery.setPageable(pageable);
        List<Auction> hits = elasticsearchTemplate
            .search(nativeSearchQuery, Auction.class)
            .map(SearchHit::getContent)
            .stream()
            .collect(Collectors.toList());

        return new PageImpl<>(hits, pageable, hits.size());
    }
}
