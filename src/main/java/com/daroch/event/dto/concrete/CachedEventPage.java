package com.daroch.event.dto.concrete;

import com.daroch.event.domain.entities.Event;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CachedEventPage {

  private List<Event> content;
  private int pageNumber;
  private int pageSize;
  private long totalElements;
}
