package com.someg.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Bid.
 */
@Entity
@Table(name = "bid")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "bid")
public class Bid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "bid_time")
    private Instant bidTime;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "ccy")
    private String ccy;

    @JsonIgnoreProperties(value = { "id" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Users userId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids", "productId" }, allowSetters = true)
    private Auction auctionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Bid id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBidTime() {
        return this.bidTime;
    }

    public Bid bidTime(Instant bidTime) {
        this.setBidTime(bidTime);
        return this;
    }

    public void setBidTime(Instant bidTime) {
        this.bidTime = bidTime;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Bid amount(Long amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCcy() {
        return this.ccy;
    }

    public Bid ccy(String ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Users getUserId() {
        return this.userId;
    }

    public void setUserId(Users users) {
        this.userId = users;
    }

    public Bid userId(Users users) {
        this.setUserId(users);
        return this;
    }

    public Auction getAuctionId() {
        return this.auctionId;
    }

    public void setAuctionId(Auction auction) {
        this.auctionId = auction;
    }

    public Bid auctionId(Auction auction) {
        this.setAuctionId(auction);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bid)) {
            return false;
        }
        return id != null && id.equals(((Bid) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bid{" +
            "id=" + getId() +
            ", bidTime='" + getBidTime() + "'" +
            ", amount=" + getAmount() +
            ", ccy='" + getCcy() + "'" +
            "}";
    }
}
