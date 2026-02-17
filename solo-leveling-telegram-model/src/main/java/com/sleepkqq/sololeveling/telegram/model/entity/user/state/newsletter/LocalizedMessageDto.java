package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.sleepkqq.sololeveling.telegram.model.entity.localziation.enums.MessageLocale;

public record LocalizedMessageDto(MessageLocale locale, String text) {

}
