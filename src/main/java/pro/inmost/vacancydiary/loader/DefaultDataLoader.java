package pro.inmost.vacancydiary.loader;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pro.inmost.vacancydiary.model.Status;
import pro.inmost.vacancydiary.model.User;
import pro.inmost.vacancydiary.model.Vacancy;
import pro.inmost.vacancydiary.service.UserService;
import pro.inmost.vacancydiary.service.VacancyService;

@Component
public class DefaultDataLoader implements ApplicationRunner {
    private static final int USERS_NUMBER = 10;
    private static final int VACANCIES_NUMBER = 50;
    private final Random random = new Random();
    private final UserService userService;
    private final VacancyService vacancyService;
    private final String[] companies = {"Inmost", "Google", "Amazon", "ECM Center"};
    private final String[] positions = {"Dev", "HR", "PM", "QA"};
    private final String[] names = {"Nazarii", "Pavel", "Alex", "Bob", "Josh"};
    private int emailCounter = 0;

    @Autowired
    public DefaultDataLoader(UserService userService, VacancyService vacancyService) {
        this.userService = userService;
        this.vacancyService = vacancyService;
    }

    @Override
    public void run(ApplicationArguments args) {
        createVacancies();
        createUsers();
    }

    private void createVacancies() {
        for (int i = 0; i < VACANCIES_NUMBER; i++) {
            Vacancy vacancy = createRandomVacancy();

            vacancyService.create(vacancy);
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

    private void createUsers() {
        List<Vacancy> createdVacancies = vacancyService.findAll();
        createDefaultUser();

        for (int i = 0; i < USERS_NUMBER; i++) {
            User user = createRandomUser(Sets.newHashSet(createdVacancies));

            userService.create(user);
        }
    }

    private void createDefaultUser() {
        String name = "name";
        String email = "mail@gmail.com";
        String password = "111";
        Set<Vacancy> vacancies = Collections.emptySet();

        userService.create(new User(name, email, password, vacancies));
    }

    private User createRandomUser(HashSet<Vacancy> createdVacancies) {
        String name = findRandomElement(names);
        String email = String.format("testmail%d@gmail.com", (++emailCounter));
        String password = generatePassword();
        int userVacanciesNumber = getRandomNumber(3, VACANCIES_NUMBER);
        Set<Vacancy> vacancies = pickRandomVacancies(createdVacancies, userVacanciesNumber);

        return new User(name, email, password, vacancies);
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

    private Set<Vacancy> pickRandomVacancies(Set<Vacancy> vacancies, int number) {
        if (vacancies == null || number < 1) {
            return Collections.emptySet();
        }

        List<Vacancy> copy = new ArrayList<>(vacancies);
        Collections.shuffle(copy);

        copy = number > copy.size() ? copy.subList(0, copy.size()) : copy.subList(0, number);
        return Sets.newHashSet(copy);
    }
}
