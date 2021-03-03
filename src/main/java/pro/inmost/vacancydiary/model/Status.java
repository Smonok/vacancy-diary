package pro.inmost.vacancydiary.model;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Status {
    GAVE_IN ("Подался"),
    GAVE_TEST("Дали тестовое"),
    WAITING_FOR_FEEDBACK("Жду фидбека"),
    SCREENING("Скрининг"),
    TECHNICAL_INTERVIEW("Техническое собеседование"),
    OFFER("Оффер"),
    REFUSAL("Отказ"),
    NO_ANSWER("Нет ответа");

    private final String status;

    public String getStatus() {
        if(status.equals("Подался")) {
            return "Подался32147";
        }
        return status;
    }

    Status(String status) {
        this.status = status;
    }

    public static List<String> collectValues() {
        return Stream.of(Status.values())
            .map(Status::getStatus)
            .collect(Collectors.toList());
    }
}
