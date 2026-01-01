package com.daroch.event.domain.entities;

import com.daroch.event.domain.enums.EventStatusEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
public class Event {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "organizer_id", nullable = false)
  private UUID organizerId;

  @Column(name = "name", nullable = false, length = 255)
  private String name;

  @Column(name = "start_time", nullable = false)
  private LocalDateTime start;

  @Column(name = "end_time", nullable = false)
  private LocalDateTime end;

  @Column(name = "venue", nullable = false, length = 255)
  private String venue;

  @Column(name = "sales_start_date")
  private LocalDateTime salesStartDate;

  @Column(name = "sales_end_date")
  private LocalDateTime salesEndDate;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  // for string representations to be stored in the database
  private EventStatusEnum status;

  @CreatedDate
  @Column(name = "created_at", updatable = false, nullable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Event)) return false;
    return id != null && id.equals(((Event) o).id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
