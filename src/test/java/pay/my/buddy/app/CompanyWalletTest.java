package pay.my.buddy.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pay.my.buddy.app.companywallet.CompanyWallet;
import pay.my.buddy.app.companywallet.CompanyWalletRepository;
import pay.my.buddy.app.companywallet.CompanyWalletService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

class CompanyWalletTest {

    @Mock
    private CompanyWalletRepository companyWalletRepository;

    @InjectMocks
    private CompanyWalletService companyWalletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addFundsToWalletTest() {
        // Arrange
        BigDecimal initialAmount = BigDecimal.ZERO;
        CompanyWallet existingWallet = new CompanyWallet();
        BigDecimal amountToAdd = new BigDecimal("100.00");
        existingWallet.setCompanyWalletId(2L);

        // Mock repository to return the single wallet entry
        when(companyWalletRepository.findById(2L)).thenReturn(Optional.of(existingWallet));

        // Act

        companyWalletService.addFundsToWallet(2L, amountToAdd);

        // Assert
        verify(companyWalletRepository, times(1)).findById(2L);
        verify(companyWalletRepository, times(1)).save(existingWallet);

        // Verify the amount is added correctly
        BigDecimal expectedAmount = initialAmount.add(amountToAdd);
        assertEquals(expectedAmount, existingWallet.getCompanyWallet());
    }
}
