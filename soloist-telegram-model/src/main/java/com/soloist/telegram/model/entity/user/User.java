package com.soloist.telegram.model.entity.user;

import com.soloist.telegram.model.entity.Model;
import java.util.List;
import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Id;
import org.babyfish.jimmer.sql.OneToMany;
import org.babyfish.jimmer.sql.OneToOne;
import org.babyfish.jimmer.sql.Table;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "users")
public interface User extends Model {

  @Id
  long id();

  @Nullable
  @OneToOne(mappedBy = "user")
  UserSession session();

  @OneToMany(mappedBy = "user")
  List<UserFeedback> feedbacks();
}
