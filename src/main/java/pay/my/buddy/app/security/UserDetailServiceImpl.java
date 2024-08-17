//package pay.my.buddy.app.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//import pay.my.buddy.app.person.Person;
//
//@Component
//public class UserDetailServiceImpl implements UserDetailsService {
//    @Autowired
//    JdbcTemplate jdbcTemplate;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String sql = "select * from Person where username = ?";
//        Person person = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Person.class), username);
//        if(person == null){
//            throw new RuntimeException("user does not exist");
//        }
//        UserDetails userDetails = User.withUsername(person.getUsername())
//                                                            .password(person
//                                                            .getPassword())
//                                                            .build();
//        return userDetails;
//    }
//}
package pay.my.buddy.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pay.my.buddy.app.person.Person;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT * FROM Person WHERE username = ?";
        Person person;

        try {
            person = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Person.class), username);
        } catch (Exception e) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        if (person == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return User.withUsername(person.getUsername())
                .password(person.getPassword())
                .authorities("USER")
                .build();
    }
}
