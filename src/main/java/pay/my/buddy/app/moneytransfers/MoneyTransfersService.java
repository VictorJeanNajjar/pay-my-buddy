package pay.my.buddy.app.moneytransfers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class MoneyTransfersService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MoneyTransfersRepository moneyTransfersRepository;

    @Transactional
    public String transferMoney(Long senderId, Long receiverId, BigDecimal transferAmount, String description) {
        Person sender = personRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        Person receiver = personRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        int suficientFundsTest = sender.getWallet().compareTo(transferAmount);
        LocalDate dayOfTransfer = LocalDate.now();

        if (suficientFundsTest < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        sender.setWallet(sender.getWallet().subtract(transferAmount));
        receiver.setWallet(receiver.getWallet().add(transferAmount));

        MoneyTransfers transfer = new MoneyTransfers();
        //transfer.setTransferId(System.currentTimeMillis());
        transfer.setSenderId(senderId);
        transfer.setSenderFirstName(sender.getFirstName());
        transfer.setSenderLastName(sender.getLastName());
        transfer.setReceiverId(receiverId);
        transfer.setReceiverFirstName(receiver.getFirstName());
        transfer.setReceiverLastName(receiver.getLastName());
        transfer.setDescription(description);
        transfer.setTransferAmount(transferAmount);
        transfer.setTransferDate(dayOfTransfer);

        moneyTransfersRepository.save(transfer);
        personRepository.save(sender);
        personRepository.save(receiver);
        return "money transfered successfully";
    }
}