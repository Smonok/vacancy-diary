package pro.inmost.vacancydiary.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.factory.EmailFactory;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.response.status.EmailStatus;
import pro.inmost.vacancydiary.response.status.ResponseStatus;
import pro.inmost.vacancydiary.service.UserService;
import pro.inmost.vacancydiary.service.VacancyService;
import pro.inmost.vacancydiary.util.DateUtil;
import pro.inmost.vacancydiary.util.JsonReaderUtil;

@Slf4j
@RestController
@RequestMapping("/diary")
public class EmailController {
    private static final String EMAIL_TEMPLATE_PATH = "templates/email_template.json";
    private static final String RESPONSE = "response";
    private static final String FROM_JSON_KEY = "from";
    private static final String MESSAGE_JSON_KEY = "message";
    private final String mailFrom;
    private final String mailMessage;
    private final JavaMailSenderImpl mailSender;
    private final EmailFactory emailFactory;
    private final UserService userService;
    private final VacancyService vacancyService;

    @Autowired
    public EmailController(VacancyService vacancyService, UserService userService,
        EmailFactory emailFactory) throws IOException {
        this.vacancyService = vacancyService;
        this.userService = userService;
        this.emailFactory = emailFactory;

        mailSender = emailFactory.createMailSender();
        mailFrom = JsonReaderUtil.getJsonFieldValue(EMAIL_TEMPLATE_PATH, FROM_JSON_KEY);
        mailMessage = JsonReaderUtil.getJsonFieldValue(EMAIL_TEMPLATE_PATH, MESSAGE_JSON_KEY);
    }

    @PostMapping("vacancies/users/{id}/email")
    public Map<String, ResponseStatus> send(@PathVariable Long id) {
        User userById = userService.findById(id);
        Set<Vacancy> vacancies = findVacanciesByUserAndStatus(userById);
        String mailTo = userById.getEmail();

        for (Vacancy vacancy : vacancies) {
            if (isWeekPassedFromLastStatusChange(vacancy)) {
                sendEmail(mailTo, vacancy.getId());
                log.info("Email sent to {}", mailTo);
                return createSuccessResponse();
            }
        }

        return createBadResponse(vacancies);
    }

    private void sendEmail(String mailTo, long vacancyId) {
        String messageToSend = String.format(mailMessage, vacancyId);
        SimpleMailMessage message = emailFactory.createMessage(mailFrom, mailTo, messageToSend);

        mailSender.send(message);
    }

    private Set<Vacancy> findVacanciesByUserAndStatus(User user) {
        String waitingForFeedbackStatus = Status.WAITING_FOR_FEEDBACK.getLabel();

        return vacancyService.findAllByUserAndStatus(user, waitingForFeedbackStatus);
    }

    private boolean isWeekPassedFromLastStatusChange(Vacancy vacancy) {
        int weeksNumber = 1;
        Timestamp lastStatusChange = vacancy.getLastStatusChange();
        Timestamp weekBeforeNow = DateUtil.subtractWeeksFromCurrentDate(weeksNumber);

        log.info("Last status change date: {}, for vacancy with id = {}", lastStatusChange.toString(), vacancy.getId());
        log.info("Week before now date: {}", weekBeforeNow.toString());
        return weekBeforeNow.getTime() >= lastStatusChange.getTime();
    }

    private Map<String, ResponseStatus> createSuccessResponse() {
        Map<String, ResponseStatus> response = new HashMap<>();
        response.put(RESPONSE, EmailStatus.SUCCESS);

        return response;
    }

    private Map<String, ResponseStatus> createBadResponse(Set<Vacancy> vacanciesByUserAndStatus) {
        Map<String, ResponseStatus> response = new HashMap<>();

        if (vacanciesByUserAndStatus.isEmpty()) {
            response.put(RESPONSE, EmailStatus.NO_SUCH_VACANCIES_FOR_USER);
        } else {
            response.put(RESPONSE, EmailStatus.NO_MATCHING_STATUS_CHANGE_DATES);
        }

        return response;
    }
}
