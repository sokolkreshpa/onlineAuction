package com.someg.auction.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProductCategory.
 */
@Entity
@Table(name = "product_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "productcategory")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "category_description")
    private String categoryDescription;

    @OneToMany(mappedBy = "productCategoryId")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ids", "productCategoryId", "locationId" }, allowSetters = true)
    private Set<Product> ids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryDescription() {
        return this.categoryDescription;
    }

    public ProductCategory categoryDescription(String categoryDescription) {
        this.setCategoryDescription(categoryDescription);
        return this;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public Set<Product> getIds() {
        return this.ids;
    }

    public void setIds(Set<Product> products) {
        if (this.ids != null) {
            this.ids.forEach(i -> i.setProductCategoryId(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProductCategoryId(this));
        }
        this.ids = products;
    }

    public ProductCategory ids(Set<Product> products) {
        this.setIds(products);
        return this;
    }

    public ProductCategory addId(Product product) {
        this.ids.add(product);
        product.setProductCategoryId(this);
        return this;
    }

    public ProductCategory removeId(Product product) {
        this.ids.remove(product);
        product.setProductCategoryId(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategory)) {
            return false;
        }
        return id != null && id.equals(((ProductCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", categoryDescription='" + getCategoryDescription() + "'" +
            "}";
    }
}
