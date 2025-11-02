package com.sleepkqq.sololeveling.telegram.model.repository;

import static com.sleepkqq.sololeveling.telegram.model.entity.user.Tables.TELEGRAM_USER_SESSION_TABLE;

import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSession;
import com.sleepkqq.sololeveling.telegram.model.entity.user.TelegramUserSessionFetcher;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TelegramUserSessionRepository {

  private final JSqlClient sql;

  @Nullable
  public TelegramUserSession findNullable(long id, TelegramUserSessionFetcher fetcher) {
    var table = TELEGRAM_USER_SESSION_TABLE;
    return sql.createQuery(table)
        .where(table.id().eq(id))
        .select(table.fetch(fetcher))
        .fetchFirstOrNull();
  }

  public TelegramUserSession save(TelegramUserSession session, SaveMode saveMode) {
    return sql.saveCommand(session)
        .setMode(saveMode)
        .execute()
        .getModifiedEntity();
  }
}
