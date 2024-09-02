package pay.my.buddy.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PersonService personService;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testGetCurrentUserId_WithUserDetailsPrincipal() {
        // Mocking SecurityContextHolder and JdbcTemplate behavior
        UserDetails userDetails = mock(UserDetails.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test_user");

        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(1L);

        Long result = personService.getCurrentUserId();

        assertEquals(1L, result);
    }

    @Test
    public void testGetCurrentUserId_WithStringPrincipal() {
        // Mocking SecurityContextHolder and JdbcTemplate behavior
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");

        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(2L);

        Long result = personService.getCurrentUserId();

        assertEquals(2L, result);
    }

    @Test
    public void testAddNewPerson_Success() {
        Person newPerson = new Person();
        newPerson.setEmailAddress("test@example.com");
        newPerson.setUsername("new_user");
        newPerson.setPassword("example");

        when(personRepository.findByEmailAddress(newPerson.getEmailAddress())).thenReturn(Optional.empty());
        when(personRepository.findByUsername(newPerson.getUsername())).thenReturn(Optional.empty());

        String result = personService.addNewPerson(newPerson);

        assertEquals("account created successfully", result);
        verify(personRepository, times(1)).save(newPerson);
    }
    @Test
    public void testAddNewPerson_noValuesAdded() {
        Long currentUserId = 1L;
        Person currentUser = new Person();
        currentUser.setPersonId(currentUserId);
        currentUser.setEmailAddress("old@example.com");
        currentUser.setUsername("old_user");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));

        String result = personService.changeAccountInformation(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertTrue(result.contains("please fill at least one of the following you want to change: password, email, first name, last name, wallet."));
        verify(personRepository, never()).save(currentUser);
    }
    @Test
    public void testAddNewPerson_EmailAlreadyExists() {
        Person newPerson = new Person();
        newPerson.setEmailAddress("test@example.com");
        newPerson.setUsername("new_user");

        when(personRepository.findByEmailAddress(newPerson.getEmailAddress())).thenReturn(Optional.of(newPerson));
        when(personRepository.findByUsername(newPerson.getUsername())).thenReturn(Optional.empty());

        String result = personService.addNewPerson(newPerson);

        assertEquals("email already exists", result);
        verify(personRepository, never()).save(newPerson);
    }

    @Test
    public void testAddNewPerson_UsernameAlreadyExists() {
        Person newPerson = new Person();
        newPerson.setEmailAddress("test@example.com");
        newPerson.setUsername("existing_user");

        when(personRepository.findByEmailAddress(newPerson.getEmailAddress())).thenReturn(Optional.empty());
        when(personRepository.findByUsername(newPerson.getUsername())).thenReturn(Optional.of(newPerson));

        String result = personService.addNewPerson(newPerson);

        assertEquals("username already exists", result);
        verify(personRepository, never()).save(newPerson);
    }
    @Test
    public void testAccountDeletion_Success() {
        Long currentUserId = 1L;

        Person person = new Person();
        person.setPersonId(currentUserId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(person));

        String result = personService.accountDeletion();

        assertEquals("account deleted", result);
        verify(personRepository, times(1)).delete(person);
    }

    @Test
    public void testAccountDeletion_PersonNotFound() {
        Long currentUserId = 1L;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            personService.accountDeletion();
        });

        assertEquals("person not found", exception.getMessage());
        verify(personRepository, never()).delete(any(Person.class));
    }

    @Test
    public void testChangeAccountInformation_SuccessfulUpdate() {
        Long currentUserId = 1L;
        Person currentUser = new Person();
        currentUser.setPersonId(currentUserId);
        currentUser.setEmailAddress("old@example.com");
        currentUser.setUsername("old_user");

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(personRepository.findByEmailAddress("new@example.com")).thenReturn(Optional.empty());

        String result = personService.changeAccountInformation(Optional.of("new@example.com"), Optional.of("newPassword"),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertTrue(result.contains("you have successfully updated your:"));
        assertTrue(result.contains("email"));
        assertTrue(result.contains("password"));
        verify(personRepository, times(1)).save(currentUser);
    }

    @Test
    public void testChangeAccountInformation_EmailInUse() {
        Long currentUserId = 1L;
        Person currentUser = new Person();
        currentUser.setPersonId(currentUserId);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(personRepository.findByEmailAddress("new@example.com")).thenReturn(Optional.of(new Person()));

        String result = personService.changeAccountInformation(Optional.of("new@example.com"), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        assertEquals("email already in use.", result);
    }

    @Test
    public void testCheckWallet() {
        Long currentUserId = 1L;
        Person currentUser = new Person();
        currentUser.setPersonId(currentUserId);
        currentUser.setWallet(BigDecimal.valueOf(150.75));

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));

        String result = personService.checkWallet();

        assertEquals("your wallet is 150.75", result);
    }

    @Test
    public void testCheckWallet_PersonNotFound() {
        Long currentUserId = 1L;

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn("test_user");
        when(jdbcTemplate.queryForObject(anyString(), eq(Long.class), eq("test_user"))).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            personService.checkWallet();
        });

        assertEquals("person not found", exception.getMessage());
    }
}
