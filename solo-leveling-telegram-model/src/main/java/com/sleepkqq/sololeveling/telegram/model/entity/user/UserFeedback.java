package com.sleepkqq.sololeveling.telegram.model.entity.user;

import java.time.Instant;
import java.util.UUID;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.JoinColumn;
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.Table;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;

@Entity
@Table(name = "user_feedbacks")
public interface UserFeedback {

  @Id
  @GeneratedValue(generatorType = UUIDIdGenerator.class)
  UUID id();

  String message();

  Instant createdAt();

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user();
}
