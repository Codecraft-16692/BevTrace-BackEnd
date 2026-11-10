package com.codecraft.bevtrace.platform.dispatch.domain.model.queries;

import java.util.List;
import java.util.Map;

/**
 * Lists transport vehicles, optionally filtered by any field.
 *
 * @param filters field name to raw values
 * @param sort    field to sort by
 * @param order   asc or desc
 * @param limit   maximum rows
 */
public record GetAllTransportVehiclesQuery(Map<String, List<String>> filters, String sort, String order, Integer limit) {
}
