package ru.utmn.dyagunov.tax_subsidies.repository.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ru.utmn.dyagunov.tax_subsidies.security.Person;
import ru.utmn.dyagunov.tax_subsidies.security.PersonRepository;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void run(String... args) throws Exception {
        if (personRepository.count() == 0) {
            Person person1 = new Person(
                    "admin@gmail.com",
                    "admin",
                    LocalDate.of(2000, 1, 1),
                    "adminPass",
                    true,
                    "ADMIN"
            );
            Person person2 = new Person(
                    "user@gmail.com",
                    "user",
                    LocalDate.of(2005, 1, 1),
                    "userPass",
                    true,
                    "USER"
            );

            ArrayList<Person> persons = new ArrayList<>();
            persons.add(person1);
            persons.add(person2);

            personRepository.saveAll(persons);
        }
    }
}