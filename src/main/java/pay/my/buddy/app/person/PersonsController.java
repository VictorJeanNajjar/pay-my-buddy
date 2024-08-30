package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
public class PersonsController {

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    public PersonService personService;

    @GetMapping("/AllClients")
    public List<Person> getAllPersons() {return personRepository.findAll();}
    @PostMapping("/newAccount")
    public String addNewPersonController(@RequestBody Person newClient){
        return personService.addNewPerson(newClient);
    }
    @DeleteMapping("/deleteAccount")
    public String accountDeletionController(){
        return personService.accountDeletion();
    }
}