package com.someg.auction.repository;

import com.someg.auction.domain.Bid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Bid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {}
