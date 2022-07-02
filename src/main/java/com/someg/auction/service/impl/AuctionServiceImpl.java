package com.someg.auction.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.someg.auction.domain.Auction;
import com.someg.auction.repository.AuctionRepository;
import com.someg.auction.repository.search.AuctionSearchRepository;
import com.someg.auction.service.AuctionService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Auction}.
 */
@Service
@Transactional
public class AuctionServiceImpl implements AuctionService {

    private final Logger log = LoggerFactory.getLogger(AuctionServiceImpl.class);

    private final AuctionRepository auctionRepository;

    private final AuctionSearchRepository auctionSearchRepository;

    public AuctionServiceImpl(AuctionRepository auctionRepository, AuctionSearchRepository auctionSearchRepository) {
        this.auctionRepository = auctionRepository;
        this.auctionSearchRepository = auctionSearchRepository;
    }

    @Override
    public Auction save(Auction auction) {
        log.debug("Request to save Auction : {}", auction);
        Auction result = auctionRepository.save(auction);
        auctionSearchRepository.save(result);
        return result;
    }

    @Override
    public Auction update(Auction auction) {
        log.debug("Request to save Auction : {}", auction);
        Auction result = auctionRepository.save(auction);
        auctionSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Auction> partialUpdate(Auction auction) {
        log.debug("Request to partially update Auction : {}", auction);

        return auctionRepository
            .findById(auction.getId())
            .map(existingAuction -> {
                if (auction.getBidStartTime() != null) {
                    existingAuction.setBidStartTime(auction.getBidStartTime());
                }
                if (auction.getBidEndTime() != null) {
                    existingAuction.setBidEndTime(auction.getBidEndTime());
                }
                if (auction.getAmount() != null) {
                    existingAuction.setAmount(auction.getAmount());
                }
                if (auction.getCcy() != null) {
                    existingAuction.setCcy(auction.getCcy());
                }

                return existingAuction;
            })
            .map(auctionRepository::save)
            .map(savedAuction -> {
                auctionSearchRepository.save(savedAuction);

                return savedAuction;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Auction> findAll(Pageable pageable) {
        log.debug("Request to get all Auctions");
        return auctionRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Auction> findOne(Long id) {
        log.debug("Request to get Auction : {}", id);
        return auctionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Auction : {}", id);
        auctionRepository.deleteById(id);
        auctionSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Auction> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Auctions for query {}", query);
        return auctionSearchRepository.search(query, pageable);
    }
}
