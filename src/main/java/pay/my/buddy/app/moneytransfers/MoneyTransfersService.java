package pay.my.buddy.app.moneytransfers;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay.my.buddy.app.companywallet.CompanyWalletService;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MoneyTransfersService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MoneyTransfersRepository moneyTransfersRepository;
    @Autowired
    private CompanyWalletService companyWalletService;
    @Autowired
    private PersonService personService;

    @Transactional
    public String transferMoney(String receiverUsername, BigDecimal transferAmount, String description, Long companyWalletId) {
        Long senderId = personService.getCurrentUserId();
        Person sender = personRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        Person receiver = personRepository.findByUsername(receiverUsername).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Long receiverId = receiver.getPersonId();
        int sufficientFundsTest = sender.getWallet().compareTo(transferAmount);
        LocalDate dayOfTransfer = LocalDate.now();

        if (sufficientFundsTest < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        BigDecimal companyDecimalValue = new BigDecimal ("0.005");
        BigDecimal clientDecimalValue = new BigDecimal("0.995");
        sender.setWallet(sender.getWallet().subtract(transferAmount));
        receiver.setWallet(receiver.getWallet().add(transferAmount.multiply(clientDecimalValue)));
        companyWalletService.addFundsToWallet(companyWalletId, transferAmount.multiply(companyDecimalValue));

        MoneyTransfers transfer = new MoneyTransfers();
        transfer.setSenderId(senderId);
        transfer.setSenderFirstName(sender.getFirstName());
        transfer.setSenderLastName(sender.getLastName());
        transfer.setReceiverId(receiverId);
        transfer.setReceiverFirstName(receiver.getFirstName());
        transfer.setReceiverLastName(receiver.getLastName());
        transfer.setDescription(description);
        transfer.setTransferAmount(transferAmount);
        transfer.setTransferDate(dayOfTransfer);
        transfer.setReceiverUsername(receiverUsername);
        transfer.setSenderUsername(sender.getUsername());

        moneyTransfersRepository.save(transfer);
        personRepository.save(sender);
        personRepository.save(receiver);
        return "money transferred successfully";
    }
    public List<String> userTransfers() {
        Long personId = personService.getCurrentUserId();
        List<MoneyTransfers> receivedTransfers = moneyTransfersRepository.findByReceiverId(personId);
        List<MoneyTransfers> senderTransfers = moneyTransfersRepository.findBySenderId(personId);
        Map<String, LocalDate> transfersMap = new HashMap<>();

        for (MoneyTransfers moneyTransfersReceived : receivedTransfers) {
            BigDecimal receivedAmount = moneyTransfersReceived.getTransferAmount();
            String senderFirstName = moneyTransfersReceived.getSenderFirstName();
            String senderLastName = moneyTransfersReceived.getSenderLastName();
            String senderUsername = moneyTransfersReceived.getSenderUsername();
            LocalDate receivedTransferDate = moneyTransfersReceived.getTransferDate();
            String transferString = receivedTransferDate + " " + senderFirstName + " " + senderLastName + " username: " + senderUsername + " amount: +" + receivedAmount;
            transfersMap.put(transferString, receivedTransferDate);
        }

        for (MoneyTransfers moneyTransfersSent : senderTransfers) {
            BigDecimal sentAmount = moneyTransfersSent.getTransferAmount();
            String receiverFirstName = moneyTransfersSent.getReceiverFirstName();
            String receiverLastName = moneyTransfersSent.getReceiverLastName();
            String receiverUsername = moneyTransfersSent.getReceiverUsername();
            LocalDate sentTransferDate = moneyTransfersSent.getTransferDate();
            String transferString = sentTransferDate + " " + receiverFirstName + " " + receiverLastName + " username: " + receiverUsername + " amount: -" + sentAmount;
            transfersMap.put(transferString, sentTransferDate);
        }

        return transfersMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, LocalDate>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}