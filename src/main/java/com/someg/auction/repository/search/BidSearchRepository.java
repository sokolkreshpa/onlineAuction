package com.someg.auction.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.someg.auction.domain.Bid;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Bid} entity.
 */
public interface BidSearchRepository extends ElasticsearchRepository<Bid, Long>, BidSearchRepositoryInternal {}

interface BidSearchRepositoryInternal {
    Stream<Bid> search(String query);
}

class BidSearchRepositoryInternalImpl implements BidSearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    BidSearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<Bid> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, Bid.class).map(SearchHit::getContent).stream();
    }
}
