package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String clientPassword = newClient.getPassword();
        if (emailChecker.isEmpty() && usernameChecker.isEmpty()){
            newClient.setPassword(encoder.encode(clientPassword));
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
    public String changeAccountInformation(Optional<String> email,
                                           Optional<String> password,
                                           Optional<String> firstName,
                                           Optional<String> lastName,
                                           Optional<BigDecimal> wallet){
        Long currentUserId = getCurrentUserId();
        Person currentUser = personRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("person not found"));
        String responseIfUpdated = "you have successfully updated your:";
        String response = "";
        String inUseMessage = "";
        Boolean counter = Boolean.FALSE;
        if (email.isPresent() && personRepository.findByEmailAddress(email.get()).isEmpty()){
            currentUser.setEmailAddress(email.get());
            response = response + " email";
            counter = Boolean.TRUE;
        }if (password.isPresent()){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String clientPassword = password.get();
            currentUser.setPassword(encoder.encode(clientPassword));
            response = response + " password";
            counter = Boolean.TRUE;
        }if (firstName.isPresent()){
            currentUser.setFirstName(firstName.get());
            response = response + " firstname";
            counter = Boolean.TRUE;
        }if(lastName.isPresent()){
            currentUser.setLastName(lastName.get());
            response = response + " last name";
            counter = Boolean.TRUE;
        }if (wallet.isPresent()){
            currentUser.setWallet(wallet.get());
            response = response + " wallet";
            counter = Boolean.TRUE;
        }if(email.isPresent()){
            if (personRepository.findByEmailAddress(email.get()).isPresent()){
                inUseMessage = "email already in use";
            }
        }else if (!counter) {
            return "please fill at least one of the following you want to change: password, email, first name, last name, wallet.";
        }if (counter){
            response = "you have successfully updated your:" + response;
        }
        personRepository.save(currentUser);
        return (inUseMessage + response + ".");
    }
    public String checkWallet(){
        Long currentUserId = getCurrentUserId();
        Person currentUser = personRepository.findById(currentUserId).orElseThrow(() -> new RuntimeException("person not found"));
        return ("your wallet is " + currentUser.getWallet());
    }
}
