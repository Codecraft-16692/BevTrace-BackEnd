package com.codecraft.bevtrace.platform.inventory.domain.model.queries;

import java.util.List;
import java.util.Map;

/**
 * Lists waste records, optionally filtered by any field.
 *
 * @param filters field name to raw values
 * @param sort    field to sort by
 * @param order   asc or desc
 * @param limit   maximum rows
 */
public record GetAllWasteRecordsQuery(Map<String, List<String>> filters, String sort, String order, Integer limit) {
}
