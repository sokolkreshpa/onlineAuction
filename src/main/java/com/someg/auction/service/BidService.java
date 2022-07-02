package com.someg.auction.service;

import com.someg.auction.domain.Bid;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Bid}.
 */
public interface BidService {
    /**
     * Save a bid.
     *
     * @param bid the entity to save.
     * @return the persisted entity.
     */
    Bid save(Bid bid);

    /**
     * Updates a bid.
     *
     * @param bid the entity to update.
     * @return the persisted entity.
     */
    Bid update(Bid bid);

    /**
     * Partially updates a bid.
     *
     * @param bid the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Bid> partialUpdate(Bid bid);

    /**
     * Get all the bids.
     *
     * @return the list of entities.
     */
    List<Bid> findAll();

    /**
     * Get the "id" bid.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Bid> findOne(Long id);

    /**
     * Delete the "id" bid.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the bid corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    List<Bid> search(String query);
}
