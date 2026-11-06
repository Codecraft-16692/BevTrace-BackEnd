package com.codecraft.bevtrace.platform.telemetry.domain.model.queries;

import java.util.List;
import java.util.Map;

/**
 * Lists device models, optionally filtered by any field.
 *
 * @param filters field name to raw values
 * @param sort    field to sort by
 * @param order   asc or desc
 * @param limit   maximum rows
 */
public record GetAllDeviceModelsQuery(Map<String, List<String>> filters, String sort, String order, Integer limit) {
}
