package pay.my.buddy.app.companywallet;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CompanyWallet {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal wallet;

    public CompanyWallet() {

    }

    public void addToWallet(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount to add must be non-negative");
        }
        this.wallet = this.wallet.add(amount);
    }

    @Override
    public String toString() {
        return "CompanyWallet{" +
                "Wallet=" + wallet +
                '}';
    }

    public Long getCompanyWalletId() {
        return id;
    }

    public void setCompanyWalletId(Long companyWalletId) {
        this.id = id;
    }

    public BigDecimal getCompanyWallet() {
        return wallet;
    }

    public void setCompanyWallet(BigDecimal companyWallet) {
        this.wallet = wallet;
    }
}
