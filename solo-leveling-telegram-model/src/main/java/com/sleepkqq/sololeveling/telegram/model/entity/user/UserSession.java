package com.sleepkqq.sololeveling.telegram.model.entity.user;

import com.sleepkqq.sololeveling.telegram.model.entity.Model;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import java.util.UUID;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.GeneratedValue;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.JoinColumn;
import org.babyfish.jimmer.sql.OneToOne;
import org.babyfish.jimmer.sql.Serialized;
import org.babyfish.jimmer.sql.Table;
import org.babyfish.jimmer.sql.meta.UUIDIdGenerator;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "user_sessions")
public interface UserSession extends Model {

  @Id
  @GeneratedValue(generatorType = UUIDIdGenerator.class)
  UUID id();

  @Serialized
  BotSessionState state();

  @Nullable
  @Serialized
  BotSessionState pendingInterruptState();

  @OneToOne
  @JoinColumn(name = "user_id")
  User user();
}
