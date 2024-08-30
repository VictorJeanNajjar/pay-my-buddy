package pay.my.buddy.app.companywallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CompanyWalletService {
    @Autowired
    private CompanyWalletRepository companyWalletRepository;
    public void addFundsToWallet(Long id, BigDecimal amount) {
        CompanyWallet wallet = companyWalletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.addToWallet(amount);
        companyWalletRepository.save(wallet);
    }
}
