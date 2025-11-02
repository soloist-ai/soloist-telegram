package com.sleepkqq.sololeveling.telegram.model.entity.user;

import com.sleepkqq.sololeveling.telegram.model.entity.Model;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.Serialized;
import org.babyfish.jimmer.sql.Table;

@Entity
@Table(name = "telegram_user_sessions")
public interface TelegramUserSession extends Model {

  @Id
  long id();

  @Serialized
  BotSessionState state();

  @Serialized
  BotSessionState pendingInterruptState();
}
