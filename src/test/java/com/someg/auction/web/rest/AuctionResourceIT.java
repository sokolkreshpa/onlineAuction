package com.someg.auction.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.someg.auction.IntegrationTest;
import com.someg.auction.domain.Auction;
import com.someg.auction.repository.AuctionRepository;
import com.someg.auction.repository.search.AuctionSearchRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AuctionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AuctionResourceIT {

    private static final Instant DEFAULT_BID_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BID_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_BID_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BID_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_AMOUNT = 1L;
    private static final Long UPDATED_AMOUNT = 2L;

    private static final String DEFAULT_CCY = "AAAAAAAAAA";
    private static final String UPDATED_CCY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/auctions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/_search/auctions";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AuctionRepository auctionRepository;

    /**
     * This repository is mocked in the com.someg.auction.repository.search test package.
     *
     * @see com.someg.auction.repository.search.AuctionSearchRepositoryMockConfiguration
     */
    @Autowired
    private AuctionSearchRepository mockAuctionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAuctionMockMvc;

    private Auction auction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createEntity(EntityManager em) {
        Auction auction = new Auction()
            .bidStartTime(DEFAULT_BID_START_TIME)
            .bidEndTime(DEFAULT_BID_END_TIME)
            .amount(DEFAULT_AMOUNT)
            .ccy(DEFAULT_CCY);
        return auction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auction createUpdatedEntity(EntityManager em) {
        Auction auction = new Auction()
            .bidStartTime(UPDATED_BID_START_TIME)
            .bidEndTime(UPDATED_BID_END_TIME)
            .amount(UPDATED_AMOUNT)
            .ccy(UPDATED_CCY);
        return auction;
    }

    @BeforeEach
    public void initTest() {
        auction = createEntity(em);
    }

    @Test
    @Transactional
    void createAuction() throws Exception {
        int databaseSizeBeforeCreate = auctionRepository.findAll().size();
        // Create the Auction
        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isCreated());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeCreate + 1);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getBidStartTime()).isEqualTo(DEFAULT_BID_START_TIME);
        assertThat(testAuction.getBidEndTime()).isEqualTo(DEFAULT_BID_END_TIME);
        assertThat(testAuction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAuction.getCcy()).isEqualTo(DEFAULT_CCY);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(1)).save(testAuction);
    }

    @Test
    @Transactional
    void createAuctionWithExistingId() throws Exception {
        // Create the Auction with an existing ID
        auction.setId(1L);

        int databaseSizeBeforeCreate = auctionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAuctionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void getAllAuctions() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get all the auctionList
        restAuctionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auction.getId().intValue())))
            .andExpect(jsonPath("$.[*].bidStartTime").value(hasItem(DEFAULT_BID_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].bidEndTime").value(hasItem(DEFAULT_BID_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY)));
    }

    @Test
    @Transactional
    void getAuction() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        // Get the auction
        restAuctionMockMvc
            .perform(get(ENTITY_API_URL_ID, auction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(auction.getId().intValue()))
            .andExpect(jsonPath("$.bidStartTime").value(DEFAULT_BID_START_TIME.toString()))
            .andExpect(jsonPath("$.bidEndTime").value(DEFAULT_BID_END_TIME.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.intValue()))
            .andExpect(jsonPath("$.ccy").value(DEFAULT_CCY));
    }

    @Test
    @Transactional
    void getNonExistingAuction() throws Exception {
        // Get the auction
        restAuctionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAuction() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();

        // Update the auction
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();
        // Disconnect from session so that the updates on updatedAuction are not directly saved in db
        em.detach(updatedAuction);
        updatedAuction.bidStartTime(UPDATED_BID_START_TIME).bidEndTime(UPDATED_BID_END_TIME).amount(UPDATED_AMOUNT).ccy(UPDATED_CCY);

        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAuction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAuction))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getBidStartTime()).isEqualTo(UPDATED_BID_START_TIME);
        assertThat(testAuction.getBidEndTime()).isEqualTo(UPDATED_BID_END_TIME);
        assertThat(testAuction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAuction.getCcy()).isEqualTo(UPDATED_CCY);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository).save(testAuction);
    }

    @Test
    @Transactional
    void putNonExistingAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, auction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void putWithIdMismatchAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(auction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void partialUpdateAuctionWithPatch() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();

        // Update the auction using partial update
        Auction partialUpdatedAuction = new Auction();
        partialUpdatedAuction.setId(auction.getId());

        partialUpdatedAuction.bidStartTime(UPDATED_BID_START_TIME).ccy(UPDATED_CCY);

        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuction))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getBidStartTime()).isEqualTo(UPDATED_BID_START_TIME);
        assertThat(testAuction.getBidEndTime()).isEqualTo(DEFAULT_BID_END_TIME);
        assertThat(testAuction.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testAuction.getCcy()).isEqualTo(UPDATED_CCY);
    }

    @Test
    @Transactional
    void fullUpdateAuctionWithPatch() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();

        // Update the auction using partial update
        Auction partialUpdatedAuction = new Auction();
        partialUpdatedAuction.setId(auction.getId());

        partialUpdatedAuction.bidStartTime(UPDATED_BID_START_TIME).bidEndTime(UPDATED_BID_END_TIME).amount(UPDATED_AMOUNT).ccy(UPDATED_CCY);

        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAuction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAuction))
            )
            .andExpect(status().isOk());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);
        Auction testAuction = auctionList.get(auctionList.size() - 1);
        assertThat(testAuction.getBidStartTime()).isEqualTo(UPDATED_BID_START_TIME);
        assertThat(testAuction.getBidEndTime()).isEqualTo(UPDATED_BID_END_TIME);
        assertThat(testAuction.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testAuction.getCcy()).isEqualTo(UPDATED_CCY);
    }

    @Test
    @Transactional
    void patchNonExistingAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, auction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(auction))
            )
            .andExpect(status().isBadRequest());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAuction() throws Exception {
        int databaseSizeBeforeUpdate = auctionRepository.findAll().size();
        auction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAuctionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(auction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Auction in the database
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(0)).save(auction);
    }

    @Test
    @Transactional
    void deleteAuction() throws Exception {
        // Initialize the database
        auctionRepository.saveAndFlush(auction);

        int databaseSizeBeforeDelete = auctionRepository.findAll().size();

        // Delete the auction
        restAuctionMockMvc
            .perform(delete(ENTITY_API_URL_ID, auction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Auction> auctionList = auctionRepository.findAll();
        assertThat(auctionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Auction in Elasticsearch
        verify(mockAuctionSearchRepository, times(1)).deleteById(auction.getId());
    }

    @Test
    @Transactional
    void searchAuction() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        auctionRepository.saveAndFlush(auction);
        when(mockAuctionSearchRepository.search("id:" + auction.getId(), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(auction), PageRequest.of(0, 1), 1));

        // Search the auction
        restAuctionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + auction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auction.getId().intValue())))
            .andExpect(jsonPath("$.[*].bidStartTime").value(hasItem(DEFAULT_BID_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].bidEndTime").value(hasItem(DEFAULT_BID_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].ccy").value(hasItem(DEFAULT_CCY)));
    }
}
