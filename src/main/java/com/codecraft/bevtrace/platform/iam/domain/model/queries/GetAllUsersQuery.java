package com.codecraft.bevtrace.platform.iam.domain.model.queries;

import java.util.List;
import java.util.Map;

/**
 * Lists users, optionally filtered by any field.
 *
 * @param filters field name to raw values
 * @param sort    field to sort by
 * @param order   asc or desc
 * @param limit   maximum rows
 */
public record GetAllUsersQuery(Map<String, List<String>> filters, String sort, String order, Integer limit) {
}
