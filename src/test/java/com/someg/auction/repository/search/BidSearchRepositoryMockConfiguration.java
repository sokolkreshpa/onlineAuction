package com.someg.auction.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link BidSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class BidSearchRepositoryMockConfiguration {

    @MockBean
    private BidSearchRepository mockBidSearchRepository;
}
