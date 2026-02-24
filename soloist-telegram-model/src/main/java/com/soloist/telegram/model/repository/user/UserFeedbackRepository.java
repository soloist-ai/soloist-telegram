package com.soloist.telegram.model.repository.user;

import static com.soloist.telegram.model.entity.Tables.USER_FEEDBACK_TABLE;

import com.soloist.telegram.model.entity.user.UserFeedback;
import com.soloist.telegram.model.entity.user.UserFeedbackCount;
import com.soloist.telegram.model.entity.user.UserFeedbackCountMapper;
import java.util.List;
import java.util.Optional;
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
    var f = USER_FEEDBACK_TABLE;

    var result = sql.createQuery(f)
        .select(UserFeedbackCountMapper
            .userCount(f.userId().count(true))
            .feedbackCount(f.count())
        )
        .limit(1)
        .fetchOneOrNull();

    return Optional.ofNullable(result)
        .orElseGet(UserFeedbackCount::empty);
  }
}
