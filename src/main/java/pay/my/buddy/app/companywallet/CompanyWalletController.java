package pay.my.buddy.app.companywallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/CompanyWallet")
public class CompanyWalletController {
    @Autowired
    CompanyWalletRepository companyWalletRepository;
    @GetMapping("/wallet")
    public List<CompanyWallet> viewCompanyFunds() {return companyWalletRepository.findAll();}
}
