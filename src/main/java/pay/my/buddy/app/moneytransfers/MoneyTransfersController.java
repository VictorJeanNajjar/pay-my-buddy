package pay.my.buddy.app.moneytransfers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import pay.my.buddy.app.person.PersonService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/moneyTransferController")
public class MoneyTransfersController {
    @Autowired
    MoneyTransfersService moneyTransfersService;
    @Autowired
    PersonService personService;
    @Autowired
    MoneyTransfersRepository moneyTransfersRepository;

    @Autowired
    public MoneyTransfersController(MoneyTransfersService moneyTransfersService, PersonService personService) {
        this.moneyTransfersService = moneyTransfersService;
        this.personService = personService;
    }

    @PostMapping("/transfer")
    public String transferMoneyController(@RequestParam String receiverUsername, @RequestParam BigDecimal amount, @RequestParam String description, @RequestParam Long companyWalletId) {
        return moneyTransfersService.transferMoney(receiverUsername, amount, description, companyWalletId);
    }
    @GetMapping("/viewAllTransfers")
    public List<MoneyTransfers> viewAllTransfers(){return moneyTransfersRepository.findAll();}
    @GetMapping("/userTransfers")
    public List<String> userTransferscontroller(){
        return moneyTransfersService.userTransfers();
    }
}


