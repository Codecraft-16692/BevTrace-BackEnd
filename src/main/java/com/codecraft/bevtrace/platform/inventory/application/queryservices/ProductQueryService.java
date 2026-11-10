package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.Product;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetProductByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllProductsQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of products.
 */
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductRepository repository;

    public ProductQueryService(ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> handle(GetAllProductsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Product> handle(GetProductByIdQuery query) {
        return repository.findById(query.id());
    }
}
