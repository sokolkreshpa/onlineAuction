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
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "productname")
    private String productname;

    @Column(name = "product_specification")
    private String productSpecification;

    @Column(name = "actual_cost")
    private Long actualCost;

    @Column(name = "ccy")
    private String ccy;

    @Column(name = "creation_date")
    private Instant creationDate;

    @OneToMany(mappedBy = "productId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ids", "productId" }, allowSetters = true)
    private Set<Auction> ids = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private ProductCategory productCategoryId;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ids" }, allowSetters = true)
    private Location locationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductname() {
        return this.productname;
    }

    public Product productname(String productname) {
        this.setProductname(productname);
        return this;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductSpecification() {
        return this.productSpecification;
    }

    public Product productSpecification(String productSpecification) {
        this.setProductSpecification(productSpecification);
        return this;
    }

    public void setProductSpecification(String productSpecification) {
        this.productSpecification = productSpecification;
    }

    public Long getActualCost() {
        return this.actualCost;
    }

    public Product actualCost(Long actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(Long actualCost) {
        this.actualCost = actualCost;
    }

    public String getCcy() {
        return this.ccy;
    }

    public Product ccy(String ccy) {
        this.setCcy(ccy);
        return this;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Instant getCreationDate() {
        return this.creationDate;
    }

    public Product creationDate(Instant creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(Instant creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Auction> getIds() {
        return this.ids;
    }

    public void setIds(Set<Auction> auctions) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductId(null));
        }
        if (auctions != null) {
            auctions.forEach(i -> i.setProductId(this));
        }
        this.ids = auctions;
    }

    public Product ids(Set<Auction> auctions) {
        this.setIds(auctions);
        return this;
    }

    public Product addId(Auction auction) {
        this.ids.add(auction);
        auction.setProductId(this);
        return this;
    }

    public Product removeId(Auction auction) {
        this.ids.remove(auction);
        auction.setProductId(null);
        return this;
    }

    public ProductCategory getProductCategoryId() {
        return this.productCategoryId;
    }

    public void setProductCategoryId(ProductCategory productCategory) {
        this.productCategoryId = productCategory;
    }

    public Product productCategoryId(ProductCategory productCategory) {
        this.setProductCategoryId(productCategory);
        return this;
    }

    public Location getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Location location) {
        this.locationId = location;
    }

    public Product locationId(Location location) {
        this.setLocationId(location);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productname='" + getProductname() + "'" +
            ", productSpecification='" + getProductSpecification() + "'" +
            ", actualCost=" + getActualCost() +
            ", ccy='" + getCcy() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            "}";
    }
}
