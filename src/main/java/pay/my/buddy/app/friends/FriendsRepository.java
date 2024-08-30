package pay.my.buddy.app.friends;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendsRepository extends JpaRepository<Friends, Long> {
    public Optional<Friends> findBySenderIdAndAndReceiverId(Long senderId, Long receiverId);
    public List<Friends> findBySenderId(Long senderId);
    public List<Friends> findByReceiverId(Long receiverId);
}
