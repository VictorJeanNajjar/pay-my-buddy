package pay.my.buddy.app.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

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
}
