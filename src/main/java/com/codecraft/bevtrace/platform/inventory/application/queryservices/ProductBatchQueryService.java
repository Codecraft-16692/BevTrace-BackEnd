package com.codecraft.bevtrace.platform.inventory.application.queryservices;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetProductBatchByIdQuery;
import com.codecraft.bevtrace.platform.inventory.domain.model.queries.GetAllProductBatchesQuery;
import com.codecraft.bevtrace.platform.inventory.domain.repositories.ProductBatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of product batchs.
 */
@Service
@Transactional(readOnly = true)
public class ProductBatchQueryService {

    private final ProductBatchRepository repository;

    public ProductBatchQueryService(ProductBatchRepository repository) {
        this.repository = repository;
    }

    public List<ProductBatch> handle(GetAllProductBatchesQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<ProductBatch> handle(GetProductBatchByIdQuery query) {
        return repository.findById(query.id());
    }
}
