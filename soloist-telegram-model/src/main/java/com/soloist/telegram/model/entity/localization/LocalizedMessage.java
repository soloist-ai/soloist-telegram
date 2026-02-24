package com.soloist.telegram.model.entity.localization;

import com.soloist.telegram.model.entity.broadcast.ScheduledBroadcast;
import com.soloist.telegram.model.entity.localization.enums.MessageLocale;
import java.util.UUID;
import org.babyfish.jimmer.sql.*;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "localized_messages")
public interface LocalizedMessage {

  @Id
  @GeneratedValue(generatorType = UUIDIdGenerator.class)
  UUID id();

  MessageLocale locale();

  String text();

  @Nullable
  @ManyToOne
  @JoinColumn(name = "broadcast_id")
  ScheduledBroadcast broadcast();
}
