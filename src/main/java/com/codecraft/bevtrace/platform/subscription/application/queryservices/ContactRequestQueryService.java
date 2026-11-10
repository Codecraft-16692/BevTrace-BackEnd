package com.codecraft.bevtrace.platform.subscription.application.queryservices;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.ContactRequest;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetContactRequestByIdQuery;
import com.codecraft.bevtrace.platform.subscription.domain.model.queries.GetAllContactRequestsQuery;
import com.codecraft.bevtrace.platform.subscription.domain.repositories.ContactRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of contact requests.
 */
@Service
@Transactional(readOnly = true)
public class ContactRequestQueryService {

    private final ContactRequestRepository repository;

    public ContactRequestQueryService(ContactRequestRepository repository) {
        this.repository = repository;
    }

    public List<ContactRequest> handle(GetAllContactRequestsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<ContactRequest> handle(GetContactRequestByIdQuery query) {
        return repository.findById(query.id());
    }
}
