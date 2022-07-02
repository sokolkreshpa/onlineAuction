package com.someg.auction.service.impl;

import static org.elasticsearch.index.query.QueryBuilders.*;

import com.someg.auction.domain.Product;
import com.someg.auction.repository.ProductRepository;
import com.someg.auction.repository.search.ProductSearchRepository;
import com.someg.auction.service.ProductService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    private final ProductSearchRepository productSearchRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productSearchRepository = productSearchRepository;
    }

    @Override
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        Product result = productRepository.save(product);
        productSearchRepository.save(result);
        return result;
    }

    @Override
    public Product update(Product product) {
        log.debug("Request to save Product : {}", product);
        Product result = productRepository.save(product);
        productSearchRepository.save(result);
        return result;
    }

    @Override
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getProductname() != null) {
                    existingProduct.setProductname(product.getProductname());
                }
                if (product.getProductSpecification() != null) {
                    existingProduct.setProductSpecification(product.getProductSpecification());
                }
                if (product.getActualCost() != null) {
                    existingProduct.setActualCost(product.getActualCost());
                }
                if (product.getCcy() != null) {
                    existingProduct.setCcy(product.getCcy());
                }
                if (product.getCreationDate() != null) {
                    existingProduct.setCreationDate(product.getCreationDate());
                }

                return existingProduct;
            })
            .map(productRepository::save)
            .map(savedProduct -> {
                productSearchRepository.save(savedProduct);

                return savedProduct;
            });
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
        productSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Products for query {}", query);
        return productSearchRepository.search(query, pageable);
    }
}
