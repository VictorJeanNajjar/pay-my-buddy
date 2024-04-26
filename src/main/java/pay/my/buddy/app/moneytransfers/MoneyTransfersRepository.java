package pay.my.buddy.app.moneytransfers;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransfersRepository extends JpaRepository<MoneyTransfers, Long> {
}
