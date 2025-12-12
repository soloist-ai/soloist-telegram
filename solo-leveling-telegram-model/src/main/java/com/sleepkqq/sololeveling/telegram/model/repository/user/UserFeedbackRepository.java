package com.sleepkqq.sololeveling.telegram.model.repository.user;

import static com.sleepkqq.sololeveling.telegram.model.entity.user.Tables.USER_FEEDBACK_TABLE;
import static com.sleepkqq.sololeveling.telegram.model.entity.user.Tables.USER_TABLE;

import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedback;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCount;
import com.sleepkqq.sololeveling.telegram.model.entity.user.UserFeedbackCountMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.babyfish.jimmer.sql.JSqlClient;
import org.babyfish.jimmer.sql.ast.mutation.SaveMode;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserFeedbackRepository {

  private final JSqlClient sql;

  public List<UserFeedback> find(long userId) {
    var table = USER_FEEDBACK_TABLE;
    return sql.createQuery(table)
        .where(table.userId().eq(userId))
        .select(table)
        .execute();
  }

  public UserFeedback save(UserFeedback feedback, SaveMode saveMode) {
    return sql.saveCommand(feedback)
        .setMode(saveMode)
        .execute()
        .getModifiedEntity();
  }

  public UserFeedbackCount getUserFeedbackCount() {
    var u = USER_TABLE;
    var f = USER_FEEDBACK_TABLE;

    var userCount = sql.createSubQuery(u)
        .where(u.asTableEx().feedbacks().count().gt(0L))
        .selectCount();

    return sql.createQuery(f)
        .select(UserFeedbackCountMapper
            .userCount(userCount)
            .feedbackCount(f.count())
        )
        .fetchOne();
  }
}
