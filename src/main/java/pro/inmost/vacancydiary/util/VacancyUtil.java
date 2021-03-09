package pro.inmost.vacancydiary.util;

import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.Vacancy;

public class VacancyUtil {

    public static void partialUpdate(Vacancy destination, Vacancy source) {
        if (destination != null && source != null) {
            if (!source.getCompanyName().isEmpty()) {
                destination.setCompanyName(source.getCompanyName());
            }
            if (!StringUtils.isBlank(source.getStatus()) && Status.collectValues().contains(source.getStatus())) {
                Date date = new Date();
                Timestamp current = new Timestamp(date.getTime());

                destination.setStatus(source.getStatus());
                destination.setLastStatusChange(current);
            }
            if (source.getExpectedSalary() != 0) {
                destination.setExpectedSalary(source.getExpectedSalary());
            }
            if (!StringUtils.isBlank(source.getLink())) {
                destination.setLink(source.getLink());
            }
            if (!StringUtils.isBlank(source.getPosition())) {
                destination.setPosition(source.getPosition());
            }
            if (!StringUtils.isBlank(source.getRecruiterContacts())) {
                destination.setRecruiterContacts(source.getRecruiterContacts());
            }
            if (source.getUsers() != null && !source.getUsers().isEmpty()) {
                destination.setUsers(source.getUsers());
            }
        }
    }
}
