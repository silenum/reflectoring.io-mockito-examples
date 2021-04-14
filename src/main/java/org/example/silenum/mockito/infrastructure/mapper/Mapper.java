package org.example.silenum.mockito.infrastructure.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.example.silenum.mockito.domain.entity.BaseDomain;
import org.example.silenum.mockito.infrastructure.database.entity.BaseEntity;
import org.example.silenum.mockito.infrastructure.database.repository.BaseEntityRepository;

/**
 * This Interface is to be implemented in order to map a subclass of {@link BaseDomain} to {@link BaseEntity}
 * and vice versa. It creates all the necessary methods by providing the type arguments.
 *
 * @param <D> Type of subclass {@link BaseDomain}
 * @param <E> Type of subclass {@link BaseEntity}
 */
public interface Mapper<D extends BaseDomain, E extends BaseEntity> {

    /**
     * Checks if the entity is present and forwards it to {@link Mapper#toDomain(BaseEntity)}
     *
     * @param entity {@link Optional} from {@link BaseEntityRepository}
     */
    default Optional<D> toDomain(Optional<E> entity) {
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return toDomain(entity.get());
    }

    /**
     * Maps a subclass of {@link BaseEntity} to a subclass of {@link BaseDomain}
     * Important: Do not map any relations! The business layer is responsible for the correct mapping
     * of relations according to the use case.
     *
     * @param entity Instance of {@link BaseEntity}
     * @return Optional of {@link BaseDomain}
     */
    Optional<D> toDomain(E entity);

    /**
     * Overloaded method for {@link Mapper#toDomain(Optional)} for convenient mappings of {@link Collection}s.
     *
     * @param entities {@link Collection} of {@link BaseEntity}
     * @return Set of {@link BaseDomain}
     */
    default Set<D> toDomain(Collection<E> entities) {
        return entities == null ? Collections.emptySet() : entities.stream()
                .map(this::toDomain)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    /**
     * Maps a subclass of {@link BaseDomain} to a subclass of {@link BaseEntity}
     * Important: Relations are fully mapped, so that JPA is able to persist the relations.
     *
     * @param domain Instance of {@link BaseDomain}
     * @return Instance of {@link BaseEntity}
     */
    E toEntity(D domain);

    /**
     * Overloaded method for {@link Mapper#toEntity(BaseDomain)} for convenient mappings of {@link Collection}s.
     *
     * @param domains {@link Collection} of {@link BaseDomain}
     * @return Set of {@link BaseEntity}
     */
    default Set<E> toEntity(Collection<D> domains) {
        return domains == null ? Collections.emptySet() : domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

}
