package com.soloist.telegram.model.entity;

import java.time.Instant;
import org.babyfish.jimmer.sql.MappedSuperclass;

@MappedSuperclass
public interface Auditable {

  Instant createdAt();
}
