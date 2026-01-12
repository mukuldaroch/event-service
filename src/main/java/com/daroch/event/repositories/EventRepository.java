package com.daroch.event.repositories;

import com.daroch.event.domain.entities.Event;
import com.daroch.event.domain.enums.EventStatusEnum;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID> {

  Page<Event> findByOrganizerId(UUID organizerId, Pageable pageable);

  Optional<Event> findByEventIdAndOrganizerId(UUID eventId, UUID organizerId);

  Page<Event> findByStatus(EventStatusEnum eventStatusEnum, Pageable pageable);

  // TODO: implement serch throught the published events
  // @Query(
  //     value =
  //         """
  //           SELECT *
  //           FROM events
  //           WHERE status = 'PUBLISHED'
  //             AND to_tsvector('english', COALESCE(name, '') || ' ' ||
  //             COALESCE(venue, ''))
  //                 @@ plainto_tsquery('english', :searchTerm)
  //         """,
  //     countQuery =
  //         """
  //           SELECT count(*)
  //           FROM events
  //           WHERE status = 'PUBLISHED'
  //             AND to_tsvector('english', COALESCE(name, '') || ' ' ||
  //             COALESCE(venue, ''))
  //                 @@ plainto_tsquery('english', :searchTerm)
  //         """,
  //     nativeQuery = true)
  // Page<Event> searchEvents(@Param("searchTerm") String searchTerm, Pageable
  // pageable);

  Optional<Event> findByEventIdAndStatus(UUID eventId, EventStatusEnum status);
}
