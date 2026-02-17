package com.sleepkqq.sololeveling.telegram.model.repository.broadcast;

import static com.sleepkqq.sololeveling.telegram.model.entity.Tables.SCHEDULED_BROADCAST_TABLE;

import com.sleepkqq.sololeveling.telegram.model.entity.broadcast.ScheduledBroadcast;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.View;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScheduledBroadcastRepository {

  private final JSqlClient sql;

  public ScheduledBroadcast save(ScheduledBroadcast broadcast, SaveMode saveMode) {
    return sql.saveCommand(broadcast)
        .setMode(saveMode)
        .execute()
        .getModifiedEntity();
  }

  @Nullable
  public <V extends View<ScheduledBroadcast>> V findView(UUID id, Class<V> viewType) {
    var table = SCHEDULED_BROADCAST_TABLE;
    return sql.createQuery(table)
        .where(table.id().eq(id))
        .select(table.fetch(viewType))
        .fetchFirstOrNull();
  }
}
