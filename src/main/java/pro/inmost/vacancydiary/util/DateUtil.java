package pro.inmost.vacancydiary.util;

import java.sql.Timestamp;

public class DateUtil {

    public static Timestamp subtractWeeks(Timestamp date, long weeks) {
        return Timestamp.valueOf(date.toLocalDateTime().minusWeeks(weeks));
    }
}
