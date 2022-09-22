/*
 * OBIMP4J - Java OBIMP Lib
 * Copyright (C) 2013—2022 Alexander Krysin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.obimp.presence

/**
 * @author Alexander Krysin
 */
enum class Language(val code: Short) {
    AFRIKAANS(1),
    ALBANIAN(2),
    ARABIC(3),
    ARMENIAN(4),
    AZERBAIJANI(5),
    BELORUSSIAN(6),
    BHOJPURI(7),
    BOSNIAN(8),
    BULGARIAN(9),
    BURMESE(10),
    CANTONESE(11),
    CATALAN(12),
    CHAMORRO(13),
    CHINESE(14),
    CROATIAN(15),
    CZECH(16),
    DANISH(17),
    DUTCH(18),
    ENGLISH(19),
    ESPERANTO(20),
    ESTONIAN(21),
    FARSI(22),
    FINNISH(23),
    FRENCH(24),
    GAELIC(25),
    GERMAN(26),
    GREEK(27),
    GUJARATI(28),
    HEBREW(29),
    HINDI(30),
    HUNGARIAN(31),
    ICELANDIC(32),
    INDONESIAN(33),
    ITALIAN(34),
    JAPANESE(35),
    KHMER(36),
    KOREAN(37),
    KURDISH(38),
    LAO(39),
    LATVIAN(40),
    LITHUANIAN(41),
    MACEDONIAN(42),
    MALAY(43),
    MANDARIN(44),
    MONGOLIAN(45),
    NORWEGIAN(46),
    PERSIAN(47),
    POLISH(48),
    PORTUGUESE(49),
    PUNJABI(50),
    ROMANIAN(51),
    RUSSIAN(52),
    SERBIAN(53),
    SINDHI(54),
    SLOVAK(55),
    SLOVENIAN(56),
    SOMALI(57),
    SPANISH(58),
    SWAHILI(59),
    SWEDISH(60),
    TAGALOG(61),
    TAIWANESE(62),
    TAMIL(63),
    TATAR(64),
    THAI(65),
    TURKISH(66),
    UKRAINIAN(67),
    URDU(68),
    VIETNAMESE(69),
    WELSH(70),
    YIDDISH(71),
    YORUBA(72),
    KAZAKH(73),
    KYRGYZ(74),
    TAJIK(75),
    TURKMEN(76),
    UZBEK(77),
    GEORGIAN(78);

    companion object {
        @JvmStatic
        fun byCode(code: Short) = values().first { it.code == code }
    }
}