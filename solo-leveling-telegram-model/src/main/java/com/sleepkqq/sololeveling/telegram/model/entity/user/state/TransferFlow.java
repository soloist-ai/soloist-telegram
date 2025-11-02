package com.sleepkqq.sololeveling.telegram.model.entity.user.state;

public sealed interface TransferFlow extends BotSessionState permits
    TransferFlow.WaitingAmount,
    TransferFlow.WaitingRecipient,
    TransferFlow.WaitingConfirmation {

  record WaitingAmount() implements TransferFlow {

    @Override
    public String message() {
      return "Введите сумму платежа";
    }
  }

  record WaitingRecipient(int amount) implements TransferFlow {

    @Override
    public String message() {
      return "Введите тэг получателя";
    }
  }

  record WaitingConfirmation(int amount, String recipientUsername) implements TransferFlow {

    @Override
    public String message() {
      return "Получатель: %s\nСумма: %d\nВcё правильно?";
    }
  }
}
