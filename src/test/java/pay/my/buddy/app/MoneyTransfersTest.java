package pay.my.buddy.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pay.my.buddy.app.companywallet.CompanyWalletService;
import pay.my.buddy.app.moneytransfers.MoneyTransfers;
import pay.my.buddy.app.moneytransfers.MoneyTransfersRepository;
import pay.my.buddy.app.moneytransfers.MoneyTransfersService;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MoneyTransfersTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private MoneyTransfersRepository moneyTransfersRepository;

    @Mock
    private CompanyWalletService companyWalletService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private MoneyTransfersService moneyTransfersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void transferMoney_ShouldTransferMoneySuccessfully() {
        String receiverUsername = "john_doe";
        BigDecimal transferAmount = new BigDecimal("100");
        String description = "Test Transfer";
        Long companyWalletId = 1L;
        Long senderId = 2L;
        Long receiverId = 3L;

        Person sender = new Person();
        sender.setPersonId(senderId);
        sender.setWallet(new BigDecimal("200"));

        Person receiver = new Person();
        receiver.setPersonId(receiverId);
        receiver.setUsername(receiverUsername);
        receiver.setWallet(new BigDecimal("50"));

        when(personService.getCurrentUserId()).thenReturn(senderId);
        when(personRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(personRepository.findByUsername(receiverUsername)).thenReturn(Optional.of(receiver));

        String result = moneyTransfersService.transferMoney(receiverUsername, transferAmount, description, companyWalletId);

        // Corrected assertions to match the expected outcomes
        assertEquals("money transferred successfully", result);
        assertEquals(new BigDecimal("149.500"), receiver.getWallet()); // 50 + 99.50
        assertEquals(new BigDecimal("100"), sender.getWallet()); // 200 - 100
        verify(companyWalletService, times(1)).addFundsToWallet(companyWalletId, new BigDecimal("0.500")); // 100 * 0.005
        verify(moneyTransfersRepository, times(1)).save(any(MoneyTransfers.class));
        verify(personRepository, times(1)).save(sender);
        verify(personRepository, times(1)).save(receiver);
    }


    @Test
    void transferMoney_ShouldThrowException_WhenSenderHasInsufficientFunds() {
        String receiverUsername = "john_doe";
        BigDecimal transferAmount = new BigDecimal("300");
        String description = "Test Transfer";
        Long companyWalletId = 1L;
        Long senderId = 2L;

        Person sender = new Person();
        sender.setPersonId(senderId);
        sender.setWallet(new BigDecimal("200"));

        when(personService.getCurrentUserId()).thenReturn(senderId);
        when(personRepository.findById(senderId)).thenReturn(Optional.of(sender));

        assertThrows(RuntimeException.class, () -> moneyTransfersService.transferMoney(receiverUsername, transferAmount, description, companyWalletId));
        verify(moneyTransfersRepository, never()).save(any(MoneyTransfers.class));
        verify(personRepository, never()).save(sender);
    }

    @Test
    void userTransfers_ShouldReturnSortedTransferList() {
        // Arrange
        Long personId = 1L;
        LocalDate today = LocalDate.now();
        LocalDate yesterday = LocalDate.now().minusDays(1);

        MoneyTransfers receivedTransfer = new MoneyTransfers();
        receivedTransfer.setTransferAmount(new BigDecimal("100"));
        receivedTransfer.setSenderFirstName("John");
        receivedTransfer.setSenderLastName("Doe");
        receivedTransfer.setSenderUsername("john_doe");
        receivedTransfer.setTransferDate(today);

        MoneyTransfers sentTransfer = new MoneyTransfers();
        sentTransfer.setTransferAmount(new BigDecimal("50"));
        sentTransfer.setReceiverFirstName("Jane");
        sentTransfer.setReceiverLastName("Doe");
        sentTransfer.setReceiverUsername("jane_doe");
        sentTransfer.setTransferDate(yesterday);

        List<MoneyTransfers> receivedTransfers = new ArrayList<>();
        receivedTransfers.add(receivedTransfer);

        List<MoneyTransfers> sentTransfers = new ArrayList<>();
        sentTransfers.add(sentTransfer);

        // Mock the service methods
        when(personService.getCurrentUserId()).thenReturn(personId);
        when(moneyTransfersRepository.findByReceiverId(personId)).thenReturn(receivedTransfers);
        when(moneyTransfersRepository.findBySenderId(personId)).thenReturn(sentTransfers);

        // Act
        List<String> result = moneyTransfersService.userTransfers();

        // Assert
        assertEquals(2, result.size());

        // Verify that the transfers are ordered by date in descending order (today first, then yesterday)
        assertEquals(today + " John Doe username: john_doe amount: +100", result.get(0));
        assertEquals(yesterday + " Jane Doe username: jane_doe amount: -50", result.get(1));
    }

}
