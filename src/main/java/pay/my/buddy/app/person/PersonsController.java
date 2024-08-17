package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonsController {

    @Autowired
    private PersonRepository personRepository;

    @GetMapping
    public List<Person> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        System.out.println("Number of persons retrieved: " + persons.size());
        return persons;
    }
}