package pro.inmost.vacancydiary.util;

import java.sql.Timestamp;
import java.util.Date;

public final class DateUtil {
    private DateUtil() {
    }

    public static Timestamp subtractWeeks(Timestamp date, long weeks) {
        if (date == null) {
            throw new IllegalArgumentException("date should not be null");
        }

        if (weeks <= 0) {
            throw new IllegalArgumentException("weeks should be greater than 0");
        }

        return Timestamp.valueOf(date.toLocalDateTime().minusWeeks(weeks));
    }

    public static Timestamp subtractWeeksFromCurrentDate(long weeks) {
        return subtractWeeks(getCurrentDate(), weeks);
    }

    public static Timestamp getCurrentDate() {
        long currentTime = new Date().getTime();
        return new Timestamp(currentTime);
    }
}
