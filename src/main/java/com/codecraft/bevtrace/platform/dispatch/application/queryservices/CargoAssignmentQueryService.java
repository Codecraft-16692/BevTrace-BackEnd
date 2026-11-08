package com.codecraft.bevtrace.platform.dispatch.application.queryservices;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetCargoAssignmentByIdQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.model.queries.GetAllCargoAssignmentsQuery;
import com.codecraft.bevtrace.platform.dispatch.domain.repositories.CargoAssignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Reads of cargo assignments.
 */
@Service
@Transactional(readOnly = true)
public class CargoAssignmentQueryService {

    private final CargoAssignmentRepository repository;

    public CargoAssignmentQueryService(CargoAssignmentRepository repository) {
        this.repository = repository;
    }

    public List<CargoAssignment> handle(GetAllCargoAssignmentsQuery query) {
        return repository.search(query.filters(), query.sort(), query.order(), query.limit());
    }

    public Optional<CargoAssignment> handle(GetCargoAssignmentByIdQuery query) {
        return repository.findById(query.id());
    }
}
