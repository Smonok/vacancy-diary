package pro.inmost.vacancydiary.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pro.inmost.vacancydiary.controller.UserController;
import pro.inmost.vacancydiary.controller.VacancyController;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;

@Component
public class DefaultDataLoader implements ApplicationRunner {
    private static final int USERS_NUMBER = 10;
    private static final int VACANCIES_NUMBER = 50;
    private final Random random = new Random();
    private final List<Vacancy> createdVacancies = new ArrayList<>();
    private final UserController userController;
    private final VacancyController vacancyController;
    private final String[] companies = {"Inmost", "Google", "Amazon", "ECM Center"};
    private final String[] positions = {"Dev", "HR", "PM", "QA"};
    private int emailCounter = 0;

    @Autowired
    public DefaultDataLoader(UserController userController, VacancyController vacancyController) {
        this.userController = userController;
        this.vacancyController = vacancyController;
    }

    @Override
    public void run(ApplicationArguments args) {
        createVacancies();
        registerUsers();
    }

    private void createVacancies() {
        for (int i = 0; i < VACANCIES_NUMBER; i++) {
            Vacancy vacancy = createRandomVacancy();

            vacancyController.create(vacancy);
            createdVacancies.add(vacancy);
        }
    }

    private Vacancy createRandomVacancy() {
        String companyName = findRandomElement(companies);
        String position = findRandomElement(positions);
        int expectedSalary = getRandomNumber(400, 3000);
        String link = "www.google.com";
        String recruiterContacts = "+380683569098";
        String status = findRandomElement(Status.collectValues());

        return new Vacancy(companyName, position, expectedSalary, link,
            recruiterContacts, status);
    }

    private String findRandomElement(String[] array) {
        int index = random.nextInt(array.length);
        return array[index];
    }

    private String findRandomElement(List<String> list) {
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    private void registerUsers() {
        for (int i = 0; i < USERS_NUMBER; i++) {
            User user = createRandomUser();

            userController.register(user);
        }
    }

    private User createRandomUser() {
        String email = emailCounter == 0 ? "nazariy_miami@ukr.net" :
            String.format("testmail%d@gmail.com", emailCounter);
        String password = generatePassword();
        int userVacanciesNumber = getRandomNumber(3, VACANCIES_NUMBER);
        List<Vacancy> vacancies = pickRandomVacancies(createdVacancies, userVacanciesNumber);

        emailCounter++;
        return new User(email, password, vacancies);
    }

    private String generatePassword() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;

        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    private int getRandomNumber(int min, int max) {
        if (min >= max) {
            return 0;
        }

        return random.nextInt(max - min) + min;
    }

    private List<Vacancy> pickRandomVacancies(List<Vacancy> vacancies, int number) {
        if (vacancies == null || number < 1) {
            return Collections.emptyList();
        }

        List<Vacancy> copy = new ArrayList<>(vacancies);
        Collections.shuffle(copy);

        return number > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, number);
    }
}
