package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    @PutMapping("/updateAccountInformation")
    public String changeAccountInformationController(@RequestParam Optional<String> email,
                                                     @RequestParam Optional<String> password,
                                                     @RequestParam Optional<String> firstName,
                                                     @RequestParam Optional<String> lastName,
                                                     @RequestParam Optional<BigDecimal> wallet){
        return personService.changeAccountInformation(email, password, firstName, lastName, wallet);
    }
    @GetMapping("/currentWallet")
    public String checkWalletController(){
        return personService.checkWallet();
    }
}