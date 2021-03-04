package pro.inmost.vacancydiary.util;

import org.apache.commons.lang3.StringUtils;
import pro.inmost.vacancydiary.model.User;

public class UserUtil {

    public static void partialUpdate(User destination, User source) {
        if (destination != null && source != null) {
            if (!StringUtils.isEmpty(source.getName())) {
                destination.setName(source.getName());
            }
            if (!StringUtils.isEmpty(source.getEmail())) {
                destination.setEmail(source.getEmail());
            }
            if (!StringUtils.isEmpty(source.getPassword())) {
                destination.setPassword(source.getPassword());
            }
            if (source.getVacancies() != null && !source.getVacancies().isEmpty()) {
                destination.setVacancies(source.getVacancies());
            }
        }
    }
}
