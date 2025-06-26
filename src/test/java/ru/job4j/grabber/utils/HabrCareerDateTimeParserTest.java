package ru.job4j.grabber.utils;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class HabrCareerDateTimeParserTest {

    @Test
    void parseDate1() {
        String parse = "2025-06-26T17:47:25+03:00";
        HabrCareerDateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        LocalDateTime result = dateTimeParser.parse(parse);
        assertThat(result).isEqualTo("2025-06-26T17:47:25");
    }

    @Test
    void parseDate2() {
        String parse = "2025-06-26T17:47:25+05:00";
        HabrCareerDateTimeParser dateTimeParser = new HabrCareerDateTimeParser();
        LocalDateTime result = dateTimeParser.parse(parse);
        assertThat(result).isEqualTo("2025-06-26T17:47:25");
    }
}