package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonRepository personRepository;

    public Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        String sql = "SELECT user_Id FROM Person WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }
    public String addNewPerson(Person newClient){
        Optional<Person> emailChecker = personRepository.findByEmailAddress(newClient.getEmailAddress());
        Optional<Person> usernameChecker = personRepository.findByUsername(newClient.getUsername());
        if (emailChecker.isEmpty() && usernameChecker.isEmpty()){
            personRepository.save(newClient);
            return "account created successfully";
        }
        else if (emailChecker.isPresent()){
            return "email already exists";
        } else {
            return "username already exists";
        }
    }
    public String accountDeletion() {
        Long deletedAccountId = getCurrentUserId();
        Person deletedAccount =  personRepository.findById(deletedAccountId).orElseThrow(() -> new RuntimeException("person not found"));
        personRepository.delete(deletedAccount);
        return "account deleted";
    }
}
