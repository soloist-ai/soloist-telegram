package com.sleepkqq.sololeveling.telegram.model.repository.user;

import static com.sleepkqq.sololeveling.telegram.model.entity.user.Tables.USER_SESSION_TABLE;

import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSession;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserSessionFetcher;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
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
}
