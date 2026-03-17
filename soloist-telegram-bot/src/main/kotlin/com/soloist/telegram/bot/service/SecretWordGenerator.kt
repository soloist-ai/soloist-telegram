package com.soloist.telegram.bot.service

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class SecretWordGenerator {

    fun generate(): String {
        val locale = LocaleContextHolder.getLocale().language
        val words = if (locale == "ru") WORDS_RU else WORDS_EN
        return words.random()
    }

    private companion object {
        val WORDS_EN = listOf(
            "apple", "river", "tiger", "cloud", "stone",
            "flame", "ocean", "eagle", "maple", "coral",
            "lemon", "storm", "pearl", "frost", "cedar",
            "melon", "globe", "amber", "crane", "bloom",
            "solar", "spark", "honey", "delta", "north",
            "lotus", "blaze", "ivory", "orbit", "arrow"
        )

        val WORDS_RU = listOf(
            "яблоко", "река", "тигр", "облако", "камень",
            "пламя", "океан", "орёл", "клён", "коралл",
            "лимон", "буря", "жемчуг", "мороз", "кедр",
            "дыня", "глобус", "янтарь", "журавль", "цветок",
            "солнце", "искра", "мёд", "дельта", "север",
            "лотос", "пламя", "кость", "орбита", "стрела"
        )
    }
}
