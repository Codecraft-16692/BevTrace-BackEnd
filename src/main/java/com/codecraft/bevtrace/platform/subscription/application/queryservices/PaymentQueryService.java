package com.codecraft.bevtrace.platform.subscription.application.queryservices;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Payment;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetPaymentByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllPaymentsQuery;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of payments.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService {

    private final PaymentRepository repository;

    public PaymentQueryService(PaymentRepository repository) {
        this.repository = repository;
    }

    public List<Payment> handle(GetAllPaymentsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<Payment> handle(GetPaymentByIdQuery query) {
        return repository.findById(query.id());
    }
}
