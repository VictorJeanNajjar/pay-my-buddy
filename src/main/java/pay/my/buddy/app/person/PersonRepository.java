package pay.my.buddy.app.person;

import org.springframework.data.jpa.repository.JpaRepository;
import pay.my.buddy.app.friends.Friends;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    public Optional<Person> findByUsername (String username);
}
