package pay.my.buddy.app.moneytransfers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pay.my.buddy.app.person.PersonService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/moneyTransferController")
public class MoneyTransfersController {
    @Autowired
    private final MoneyTransfersService moneyTransfersService;
    @Autowired
    private final PersonService personService;

    @Autowired
    public MoneyTransfersController(MoneyTransfersService moneyTransfersService, PersonService personService) {
        this.moneyTransfersService = moneyTransfersService;
        this.personService = personService;
    }

    @PostMapping("/transfer")
    public String transferMoneyController(@RequestParam Long receiverId, @RequestParam BigDecimal amount, @RequestParam String description) {
        Long senderId = personService.getCurrentUserId();
        return moneyTransfersService.transferMoney(senderId, receiverId, amount, description);
    }
}

