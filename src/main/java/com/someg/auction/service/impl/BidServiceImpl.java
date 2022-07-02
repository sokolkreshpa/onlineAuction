package com.someg.auction.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.someg.auction.domain.Bid;
import com.someg.auction.repository.BidRepository;
import com.someg.auction.repository.search.BidSearchRepository;
import com.someg.auction.service.BidService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Bid}.
 */
@Service
@Transactional
public class BidServiceImpl implements BidService {

    private final Logger log = LoggerFactory.getLogger(BidServiceImpl.class);

    private final BidRepository bidRepository;

    private final BidSearchRepository bidSearchRepository;

    public BidServiceImpl(BidRepository bidRepository, BidSearchRepository bidSearchRepository) {
        this.bidRepository = bidRepository;
        this.bidSearchRepository = bidSearchRepository;
    }

    @Override
    public Bid save(Bid bid) {
        log.debug("Request to save Bid : {}", bid);
        Bid result = bidRepository.save(bid);
        bidSearchRepository.save(result);
        return result;
    }

    @Override
    public Bid update(Bid bid) {
        log.debug("Request to save Bid : {}", bid);
        Bid result = bidRepository.save(bid);
        bidSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Bid> partialUpdate(Bid bid) {
        log.debug("Request to partially update Bid : {}", bid);

        return bidRepository
            .findById(bid.getId())
            .map(existingBid -> {
                if (bid.getBidTime() != null) {
                    existingBid.setBidTime(bid.getBidTime());
                }
                if (bid.getAmount() != null) {
                    existingBid.setAmount(bid.getAmount());
                }
                if (bid.getCcy() != null) {
                    existingBid.setCcy(bid.getCcy());
                }

                return existingBid;
            })
            .map(bidRepository::save)
            .map(savedBid -> {
                bidSearchRepository.save(savedBid);

                return savedBid;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bid> findAll() {
        log.debug("Request to get all Bids");
        return bidRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Bid> findOne(Long id) {
        log.debug("Request to get Bid : {}", id);
        return bidRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bid : {}", id);
        bidRepository.deleteById(id);
        bidSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bid> search(String query) {
        log.debug("Request to search Bids for query {}", query);
        return StreamSupport.stream(bidSearchRepository.search(query).spliterator(), false).collect(Collectors.toList());
    }
}
