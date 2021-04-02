package pro.inmost.vacancydiary.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Status {
    GAVE_IN("Подался"),
    GAVE_TEST("Дали тестовое"),
    WAITING_FOR_FEEDBACK("Жду фидбека"),
    SCREENING("Скрининг"),
    TECHNICAL_INTERVIEW("Техническое собеседование"),
    OFFER("Оффер"),
    REFUSAL("Отказ"),
    NO_ANSWER("Нет ответа");

    private final String label;

    public String getLabel() {
        return label;
    }

    Status(String label) {
        this.label = label;
    }

    public static boolean isValueExists(String value) {
        return collectValues().contains(value);
    }

    public static List<String> collectValues() {
        return Stream.of(Status.values())
            .map(Status::getLabel)
            .collect(Collectors.toList());
    }
}
