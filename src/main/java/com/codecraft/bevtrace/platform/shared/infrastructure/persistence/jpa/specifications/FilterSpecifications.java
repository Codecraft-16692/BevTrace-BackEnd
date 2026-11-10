package com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.specifications;

import jakarta.persistence.criteria.Predicate;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Builds Spring Data specifications from the {@code ?field=value} filters of the read endpoints
 * (json-server conventions the Angular frontend was built against): any persistent field as an
 * equality filter (repeat the parameter for an {@code IN}), {@code _sort}, {@code _order} and {@code _limit}.
 *
 * <p>Addition of BevTrace: generic infrastructure that lets every persistence adapter answer filtered
 * reads without one hand-written finder per filter. It does not exist in Qualitrack's shared package.</p>
 */
@NullMarked
public final class FilterSpecifications {

    private FilterSpecifications() {
    }

    /**
     * Builds the where clause of a read.
     *
     * @param filters field name to raw values; keys starting with {@code _} are ignored
     * @param <E>     the persistence entity type
     * @return specification matching every filter
     */
    public static <E> Specification<E> of(Map<String, List<String>> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (var entry : filters.entrySet()) {
                String field = entry.getKey();
                if (field.startsWith("_")) {
                    continue;
                }
                var path = root.get(field);
                var values = entry.getValue().stream().map(raw -> coerce(raw, path.getJavaType(), field)).toList();
                predicates.add(values.size() == 1 ? cb.equal(path, values.get(0)) : path.in(values));
            }
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    /**
     * Builds the paging/sorting of a read.
     *
     * @param sort  field to sort by, null for the id
     * @param order {@code asc} or {@code desc}, null for ascending
     * @param limit maximum rows, null for all
     * @return pageable carrying sort and limit
     */
    public static Pageable page(String sort, String order, Integer limit) {
        var direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
        var by = Sort.by(direction, sort == null || sort.isBlank() ? "id" : sort);
        return PageRequest.of(0, limit == null || limit <= 0 ? Integer.MAX_VALUE : limit, by);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object coerce(String raw, Class<?> type, String field) {
        try {
            if (type == String.class) return raw;
            if (type == Long.class || type == long.class) return Long.valueOf(raw);
            if (type == Integer.class || type == int.class) return Integer.valueOf(raw);
            if (type == Double.class || type == double.class) return Double.valueOf(raw);
            if (type == BigDecimal.class) return new BigDecimal(raw);
            if (type == Boolean.class || type == boolean.class) return Boolean.valueOf(raw);
            if (type == LocalDate.class) return LocalDate.parse(raw);
            if (type == Instant.class) return Instant.parse(raw);
            if (type.isEnum()) return Enum.valueOf((Class<? extends Enum>) type, raw);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Invalid value '%s' for field '%s'".formatted(raw, field), ex);
        }
        throw new IllegalArgumentException("Field '%s' cannot be used as a filter".formatted(field));
    }
}
