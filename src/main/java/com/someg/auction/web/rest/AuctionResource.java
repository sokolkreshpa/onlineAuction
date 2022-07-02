package com.someg.auction.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.someg.auction.domain.Auction;
import com.someg.auction.repository.AuctionRepository;
import com.someg.auction.service.AuctionService;
import com.someg.auction.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.someg.auction.domain.Auction}.
 */
@RestController
@RequestMapping("/api")
public class AuctionResource {

    private final Logger log = LoggerFactory.getLogger(AuctionResource.class);

    private static final String ENTITY_NAME = "auction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AuctionService auctionService;

    private final AuctionRepository auctionRepository;

    public AuctionResource(AuctionService auctionService, AuctionRepository auctionRepository) {
        this.auctionService = auctionService;
        this.auctionRepository = auctionRepository;
    }

    /**
     * {@code POST  /auctions} : Create a new auction.
     *
     * @param auction the auction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new auction, or with status {@code 400 (Bad Request)} if the auction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/auctions")
    public ResponseEntity<Auction> createAuction(@RequestBody Auction auction) throws URISyntaxException {
        log.debug("REST request to save Auction : {}", auction);
        if (auction.getId() != null) {
            throw new BadRequestAlertException("A new auction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Auction result = auctionService.save(auction);
        return ResponseEntity
            .created(new URI("/api/auctions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /auctions/:id} : Updates an existing auction.
     *
     * @param id the id of the auction to save.
     * @param auction the auction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auction,
     * or with status {@code 400 (Bad Request)} if the auction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the auction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/auctions/{id}")
    public ResponseEntity<Auction> updateAuction(@PathVariable(value = "id", required = false) final Long id, @RequestBody Auction auction)
        throws URISyntaxException {
        log.debug("REST request to update Auction : {}, {}", id, auction);
        if (auction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Auction result = auctionService.update(auction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /auctions/:id} : Partial updates given fields of an existing auction, field will ignore if it is null
     *
     * @param id the id of the auction to save.
     * @param auction the auction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated auction,
     * or with status {@code 400 (Bad Request)} if the auction is not valid,
     * or with status {@code 404 (Not Found)} if the auction is not found,
     * or with status {@code 500 (Internal Server Error)} if the auction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/auctions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Auction> partialUpdateAuction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Auction auction
    ) throws URISyntaxException {
        log.debug("REST request to partial update Auction partially : {}, {}", id, auction);
        if (auction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, auction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!auctionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Auction> result = auctionService.partialUpdate(auction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, auction.getId().toString())
        );
    }

    /**
     * {@code GET  /auctions} : get all the auctions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of auctions in body.
     */
    @GetMapping("/auctions")
    public ResponseEntity<List<Auction>> getAllAuctions(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Auctions");
        Page<Auction> page = auctionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /auctions/:id} : get the "id" auction.
     *
     * @param id the id of the auction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the auction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/auctions/{id}")
    public ResponseEntity<Auction> getAuction(@PathVariable Long id) {
        log.debug("REST request to get Auction : {}", id);
        Optional<Auction> auction = auctionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(auction);
    }

    /**
     * {@code DELETE  /auctions/:id} : delete the "id" auction.
     *
     * @param id the id of the auction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/auctions/{id}")
    public ResponseEntity<Void> deleteAuction(@PathVariable Long id) {
        log.debug("REST request to delete Auction : {}", id);
        auctionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/auctions?query=:query} : search for the auction corresponding
     * to the query.
     *
     * @param query the query of the auction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/auctions")
    public ResponseEntity<List<Auction>> searchAuctions(
        @RequestParam String query,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to search for a page of Auctions for query {}", query);
        Page<Auction> page = auctionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
