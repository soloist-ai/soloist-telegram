package com.soloist.telegram.model.entity.broadcast;

import com.soloist.telegram.model.entity.Model;
import com.soloist.telegram.model.entity.broadcast.enums.BroadcastStatus;
import com.soloist.telegram.model.entity.localization.LocalizedMessage;
import java.time.Instant;
import java.util.UUID;
import org.babyfish.jimmer.sql.*;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "scheduled_broadcasts")
public interface ScheduledBroadcast extends Model {

  @Id
  @GeneratedValue(generatorType = UUIDIdGenerator.class)
  UUID id();

  @Key
  String name();

  @Nullable
  String fileId();

  Instant scheduledAt();

  BroadcastStatus status();

  @Nullable
  Integer total();

  @Nullable
  Integer totalSuccess();

  @Nullable
  Integer totalFailed();

  @OneToMany(mappedBy = "broadcast")
  List<LocalizedMessage> messages();
}
