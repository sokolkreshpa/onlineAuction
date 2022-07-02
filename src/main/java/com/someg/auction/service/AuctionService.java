package com.someg.auction.service;

import com.someg.auction.domain.Auction;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Auction}.
 */
public interface AuctionService {
    /**
     * Save a auction.
     *
     * @param auction the entity to save.
     * @return the persisted entity.
     */
    Auction save(Auction auction);

    /**
     * Updates a auction.
     *
     * @param auction the entity to update.
     * @return the persisted entity.
     */
    Auction update(Auction auction);

    /**
     * Partially updates a auction.
     *
     * @param auction the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Auction> partialUpdate(Auction auction);

    /**
     * Get all the auctions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Auction> findAll(Pageable pageable);

    /**
     * Get the "id" auction.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Auction> findOne(Long id);

    /**
     * Delete the "id" auction.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Search for the auction corresponding to the query.
     *
     * @param query the query of the search.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Auction> search(String query, Pageable pageable);
}
