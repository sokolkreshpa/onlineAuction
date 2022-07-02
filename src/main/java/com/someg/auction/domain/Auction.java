package com.someg.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Auction.
 */
@Entity
@Table(name = "auction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "auction")
public class Auction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bid_start_time")
    private Instant bidStartTime;

    @Column(name = "bid_end_time")
    private Instant bidEndTime;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "ccy")
    private String ccy;

    @OneToMany(mappedBy = "auctionId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userId", "auctionId" }, allowSetters = true)
    private Set<Bid> ids = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "productCategoryId", "locationId" }, allowSetters = true)
    private Product productId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Auction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBidStartTime() {
        return this.bidStartTime;
    }

    public Auction bidStartTime(Instant bidStartTime) {
        this.setBidStartTime(bidStartTime);
        return this;
    }

    public void setBidStartTime(Instant bidStartTime) {
        this.bidStartTime = bidStartTime;
    }

    public Instant getBidEndTime() {
        return this.bidEndTime;
    }

    public Auction bidEndTime(Instant bidEndTime) {
        this.setBidEndTime(bidEndTime);
        return this;
    }

    public void setBidEndTime(Instant bidEndTime) {
        this.bidEndTime = bidEndTime;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Auction amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCcy() {
        return this.ccy;
    }

    public Auction ccy(String ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Set<Bid> getIds() {
        return this.ids;
    }

    public void setIds(Set<Bid> bids) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setAuctionId(null));
        }
        if (bids != null) {
            bids.forEach(i -> i.setAuctionId(this));
        }
        this.ids = bids;
    }

    public Auction ids(Set<Bid> bids) {
        this.setIds(bids);
        return this;
    }

    public Auction addId(Bid bid) {
        this.ids.add(bid);
        bid.setAuctionId(this);
        return this;
    }

    public Auction removeId(Bid bid) {
        this.ids.remove(bid);
        bid.setAuctionId(null);
        return this;
    }

    public Product getProductId() {
        return this.productId;
    }

    public void setProductId(Product product) {
        this.productId = product;
    }

    public Auction productId(Product product) {
        this.setProductId(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Auction)) {
            return false;
        }
        return id != null && id.equals(((Auction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Auction{" +
            "id=" + getId() +
            ", bidStartTime='" + getBidStartTime() + "'" +
            ", bidEndTime='" + getBidEndTime() + "'" +
            ", amount=" + getAmount() +
            ", ccy='" + getCcy() + "'" +
            "}";
    }
}
