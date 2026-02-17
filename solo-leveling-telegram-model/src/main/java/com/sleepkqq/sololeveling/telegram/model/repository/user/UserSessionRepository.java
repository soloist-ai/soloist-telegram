package com.sleepkqq.sololeveling.telegram.model.repository.user;

import static com.sleepkqq.sololeveling.telegram.model.entity.Tables.USER_SESSION_TABLE;

import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.BotSessionState;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.Expression;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserSessionRepository {

  private final JSqlClient sql;

  @Nullable
  public UserSession findNullable(long userId, UserSessionFetcher fetcher) {
    var table = USER_SESSION_TABLE;
    return sql.createQuery(table)
        .where(table.userId().eq(userId))
        .select(table.fetch(fetcher))
        .fetchFirstOrNull();
  }

  public UserSession save(UserSession session, SaveMode saveMode) {
    return sql.saveCommand(session)
        .setMode(saveMode)
        .execute()
        .getModifiedEntity();
  }

  public void confirmInterruptState(long userId) {
    var table = USER_SESSION_TABLE;
    sql.createUpdate(table)
        .where(
            table.userId().eq(userId),
            table.pendingInterruptState().isNotNull()
        )
        .set(table.state(), table.pendingInterruptState())
        .set(table.pendingInterruptState(), Expression.nullValue(BotSessionState.class))
        .execute();
  }

  public void cancelInterruptState(long userId) {
    var table = USER_SESSION_TABLE;
    sql.createUpdate(table)
        .where(
            table.userId().eq(userId),
            table.pendingInterruptState().isNotNull()
        )
        .set(table.pendingInterruptState(), Expression.nullValue(BotSessionState.class))
        .execute();
  }

  public void updateState(long userId, BotSessionState state) {
    var table = USER_SESSION_TABLE;
    sql.createUpdate(table)
        .where(table.userId().eq(userId))
        .set(table.state(), state)
        .execute();
  }
}
