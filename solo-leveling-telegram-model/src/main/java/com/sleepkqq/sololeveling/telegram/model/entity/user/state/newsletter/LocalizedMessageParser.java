package com.sleepkqq.sololeveling.telegram.model.entity.user.state.newsletter;

import com.sleepkqq.sololeveling.telegram.model.entity.localziation.enums.MessageLocale;
import one.util.streamex.StreamEx;

import java.util.*;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

/**
 * Парсер локализованных сообщений для рассылки.
 *
 * <p>Формат:
 * <pre>
 * [RU]
 * Привет! Это сообщение на русском.
 * Можно писать несколько строк.
 *
 * [EN]
 * Hello! This is a message in English.
 * Multiple lines supported.
 * </pre>
 *
 * <p>Автоматически поддерживает все локали из {@link MessageLocale} enum.
 */
public class LocalizedMessageParser {

  private static Pattern createLocalePattern() {
    var locales = StreamEx.of(MessageLocale.values())
        .map(Enum::name)
        .joining("|");

    return Pattern.compile(
        "\\[(" + locales + ")]\\s*([\\s\\S]*?)(?=\\[(?:" + locales + ")]|$)",
        Pattern.CASE_INSENSITIVE
    );
  }

  public static List<LocalizedMessageDto> parse(String input) {
    if (!StringUtils.hasText(input)) {
      throw new IllegalArgumentException("Empty input");
    }

    var pattern = createLocalePattern();
    var matcher = pattern.matcher(input.trim());

    var messages = new ArrayList<LocalizedMessageDto>();
    var usedLocales = new HashSet<MessageLocale>();

    while (matcher.find()) {
      var localeStr = matcher.group(1).toUpperCase();
      var text = matcher.group(2).trim();

      if (text.isEmpty()) {
        throw new IllegalArgumentException("Empty text for locale");
      }

      var locale = MessageLocale.valueOf(localeStr);

      if (!usedLocales.add(locale)) {
        throw new IllegalArgumentException("Duplicate locale");
      }

      messages.add(new LocalizedMessageDto(locale, text));
    }

    if (messages.isEmpty()) {
      throw new IllegalArgumentException("No valid locales found");
    }

    return messages;
  }
}
