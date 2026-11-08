package com.codecraft.bevtrace.platform.traceability.interfaces.acl;

import com.codecraft.bevtrace.platform.traceability.application.queryservices.TraceabilityLogQueryService;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetAllTraceabilityLogsQuery;
import com.codecraft.bevtrace.platform.traceability.domain.model.queries.GetTraceabilityLogByIdQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * The only door other contexts use to ask the traceability context about shipments.
 */
@Service
@Transactional(readOnly = true)
public class TraceabilityContextFacade {

    /**
     * A shipment that is on the road.
     *
     * @param logId            log of the shipment
     * @param deviceId         device that follows it
     * @param destinationName  destination
     * @param estimatedArrival estimated arrival
     */
    public record ActiveRoute(Long logId, Long deviceId, String destinationName, Instant estimatedArrival) {
    }

    private final TraceabilityLogQueryService logs;

    public TraceabilityContextFacade(TraceabilityLogQueryService logs) {
        this.logs = logs;
    }

    public Optional<String> destinationName(Long logId) {
        return logs.handle(new GetTraceabilityLogByIdQuery(logId)).map(log -> log.getDestinationName());
    }

    public List<ActiveRoute> inTransit() {
        var query = new GetAllTraceabilityLogsQuery(Map.of("currentStatus", List.of("IN_TRANSIT")), null, null, null);
        return logs.handle(query).stream()
                .map(log -> new ActiveRoute(log.getId(), log.getDeviceId(), log.getDestinationName(),
                        log.getEstimatedArrival()))
                .toList();
    }
}
