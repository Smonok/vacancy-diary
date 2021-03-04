package pro.inmost.vacancydiary.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.inmost.vacancydiary.config.EmailConfiguration;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.repository.UserRepository;
import pro.inmost.vacancydiary.repository.VacancyRepository;
import pro.inmost.vacancydiary.response.status.EmailStatus;
import pro.inmost.vacancydiary.response.status.ResponseStatus;
import pro.inmost.vacancydiary.util.DateUtil;
import pro.inmost.vacancydiary.util.JsonReaderUtil;

@Slf4j
@RestController
@RequestMapping("/diary")
public class EmailController {
    private static final String EMAIL_TEMPLATE_PATH = "templates/email_template.json";
    private static final String RESPONSE = "response";
    private final EmailConfiguration emailConfig;
    private final UserRepository userRepository;
    private final VacancyRepository vacancyRepository;

    @Autowired
    public EmailController(EmailConfiguration emailConfig, VacancyRepository vacancyRepository,
        UserRepository userRepository) {
        this.emailConfig = emailConfig;
        this.vacancyRepository = vacancyRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("vacancies/users/{id}/email")
    public Map<String, ResponseStatus> send(@PathVariable Long id) throws IOException {
        Map<String, ResponseStatus> response = new HashMap<>();
        User userById = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("User not found by id = " + id));
        Set<Vacancy> vacancies = vacancyRepository
            .findAllByUserAndStatus(userById, Status.WAITING_FOR_FEEDBACK.getLabel());

        if (vacancies.isEmpty()) {
            response.put(RESPONSE, EmailStatus.NO_SUCH_VACANCIES_FOR_USER);
            return response;
        }

        String mailFrom = JsonReaderUtil.getJsonFieldValue(EMAIL_TEMPLATE_PATH, "from");
        String mailTo = userById.getEmail();
        String messageValue = JsonReaderUtil.getJsonFieldValue(EMAIL_TEMPLATE_PATH, "message");
        JavaMailSenderImpl mailSender = createMailSender();
        Timestamp current = new Timestamp(new Date().getTime());
        boolean isEmailSent = false;

        for (Vacancy vacancy : vacancies) {
            String message = String.format(messageValue, vacancy.getId());
            SimpleMailMessage mailMessage = createMessage(mailFrom, mailTo, message);
            Timestamp lastStatusChange = vacancy.getLastStatusChange();
            Timestamp weekBeforeNow = DateUtil.subtractWeeks(current, 1);

            if (weekBeforeNow.getTime() >= lastStatusChange.getTime()) {
                mailSender.send(mailMessage);
                isEmailSent = true;
            }
        }

        if (isEmailSent) {
            response.put(RESPONSE, EmailStatus.SUCCESS);
        } else {
            response.put(RESPONSE, EmailStatus.NO_MATCHING_STATUS_CHANGE_DATES);
        }

        return response;
    }

    private JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(emailConfig.getHost());
        mailSender.setPort(emailConfig.getPort());
        mailSender.setUsername(emailConfig.getUsername());
        mailSender.setPassword(emailConfig.getPassword());

        return mailSender;
    }

    private SimpleMailMessage createMessage(String mailFrom, String mailTo, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailTo);
        mailMessage.setText(message);

        return mailMessage;
    }
}
