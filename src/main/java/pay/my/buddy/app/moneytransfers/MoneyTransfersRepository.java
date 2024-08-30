package pay.my.buddy.app.moneytransfers;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoneyTransfersRepository extends JpaRepository<MoneyTransfers, Long> {
    public List<MoneyTransfers> findBySenderId(Long senderId);
    public List<MoneyTransfers> findByReceiverId(Long receiverId);
}
