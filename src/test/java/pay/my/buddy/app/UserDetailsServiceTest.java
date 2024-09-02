package pay.my.buddy.app;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.security.UserDetailServiceImpl;

public class UserDetailsServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserDetailServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        // Arrange
        String username = "testUser";
        Person person = new Person();
        person.setUsername(username);
        person.setPassword("testPassword");

        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM Person WHERE username = ?"),
                any(BeanPropertyRowMapper.class),
                eq(username)))
                .thenReturn(person);


        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        assertEquals("USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentUser";

        when(jdbcTemplate.queryForObject(
                "SELECT * FROM Person WHERE username = ?",
                new BeanPropertyRowMapper<>(Person.class),
                username))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Act & Assert
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(username);
        });
        assertEquals("User not found with username: " + username, thrown.getMessage());
    }
}
