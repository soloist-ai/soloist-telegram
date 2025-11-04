package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.WaitingAmountState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.WaitingConfirmationState;
import com.sleepkqq.sololeveling.telegram.model.entity.user.state.transfer.WaitingRecipientState;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@type",
    defaultImpl = IdleState.class
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = IdleState.class, name = "IdleState"),
    @JsonSubTypes.Type(value = WaitingAmountState.class, name = "WaitingAmountState"),
    @JsonSubTypes.Type(value = WaitingRecipientState.class, name = "WaitingRecipientState"),
    @JsonSubTypes.Type(value = WaitingConfirmationState.class, name = "WaitingConfirmationState")
})
public interface BotSessionState {

  String message();

  default BotSessionState nextState(String userInput) {
    return new IdleState();
  }
}
