package com.someg.auction.repository.search;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import com.someg.auction.domain.ProductCategory;
import java.util.stream.Stream;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ProductCategory} entity.
 */
public interface ProductCategorySearchRepository
    extends ElasticsearchRepository<ProductCategory, Long>, ProductCategorySearchRepositoryInternal {}

interface ProductCategorySearchRepositoryInternal {
    Stream<ProductCategory> search(String query);
}

class ProductCategorySearchRepositoryInternalImpl implements ProductCategorySearchRepositoryInternal {

    private final ElasticsearchRestTemplate elasticsearchTemplate;

    ProductCategorySearchRepositoryInternalImpl(ElasticsearchRestTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Stream<ProductCategory> search(String query) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(queryStringQuery(query));
        return elasticsearchTemplate.search(nativeSearchQuery, ProductCategory.class).map(SearchHit::getContent).stream();
    }
}
