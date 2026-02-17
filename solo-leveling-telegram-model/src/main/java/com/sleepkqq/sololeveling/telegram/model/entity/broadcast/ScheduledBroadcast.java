package com.sleepkqq.sololeveling.telegram.model.entity.broadcast;

import com.sleepkqq.sololeveling.telegram.model.entity.Model;
import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.enums.BroadcastStatus;
import com.sleepkqq.sololeveling.telegram.model.entity.localziation.LocalizedMessage;
import java.time.Instant;
import java.util.UUID;
import org.babyfish.jimmer.sql.*;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "scheduled_broadcasts")
public interface ScheduledBroadcast extends Model {

  @Id
  @GeneratedValue(generatorType = UUIDIdGenerator.class)
  UUID id();

  @NotNull
  Instant scheduledAt();

  @NotNull
  BroadcastStatus status();

  @Nullable
  Integer totalUsers();

  @Nullable
  Integer successfulSends();

  @Nullable
  Integer failedSends();

  @OneToMany(mappedBy = "broadcast")
  List<LocalizedMessage> messages();
}
