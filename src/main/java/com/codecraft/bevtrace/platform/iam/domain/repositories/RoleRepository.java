package com.codecraft.bevtrace.platform.iam.domain.repositories;

import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Port to the stored roles.
 */
public interface RoleRepository {

    Optional<Role> findById(Long id);

    boolean existsById(Long id);

    Role save(Role role);

    /**
     * Filtered read behind the collection endpoint.
     *
     * @param filters field name to raw values (repeated values mean IN)
     * @param sort    field to sort by, null for the id
     * @param order   asc or desc
     * @param limit   maximum rows, null for all
     * @return matching rows
     */
    List<Role> search(Map<String, List<String>> filters, String sort, String order, Integer limit);

    Optional<Role> findByName(String name);
}
