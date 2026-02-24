package com.soloist.telegram.model.entity.user.state.newsletter;

import com.soloist.telegram.model.entity.localization.enums.MessageLocale;

public record LocalizedMessageDto(MessageLocale locale, String text) {

}
