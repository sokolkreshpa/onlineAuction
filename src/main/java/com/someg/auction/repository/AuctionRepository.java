package com.someg.auction.repository;

import com.someg.auction.domain.Auction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Auction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {}
