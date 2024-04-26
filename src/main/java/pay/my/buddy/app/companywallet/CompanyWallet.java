package pay.my.buddy.app.companywallet;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.math.BigDecimal;

@Entity
public class CompanyWallet {
    @Id
    @GeneratedValue
    private Long companyWalletId;
    public BigDecimal companyWallet;

    public CompanyWallet(Long companyWalletId, BigDecimal companyWallet) {
        this.companyWalletId = companyWalletId;
        this.companyWallet = companyWallet;
    }

    public CompanyWallet() {
    }

    @Override
    public String toString() {
        return "CompanyWallet{" +
                "companyWallet=" + companyWallet +
                '}';
    }

    public Long getCompanyWalletId() {
        return companyWalletId;
    }

    public void setCompanyWalletId(Long companyWalletId) {
        this.companyWalletId = companyWalletId;
    }

    public BigDecimal getCompanyWallet() {
        return companyWallet;
    }

    public void setCompanyWallet(BigDecimal companyWallet) {
        this.companyWallet = companyWallet;
    }
}
