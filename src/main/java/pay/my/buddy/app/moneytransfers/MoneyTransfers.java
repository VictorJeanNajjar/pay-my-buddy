package pay.my.buddy.app.moneytransfers;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class MoneyTransfers {
    @Id
    @GeneratedValue
    public Long transferId;
    public String senderFirstName;
    public String senderLastName;
    public Long senderId;
    public String receiverFirstName;
    public String receiverLastName;
    public Long receiverId;
    public String description;
    public LocalDate transferDate;
    public BigDecimal transferAmount;

    public MoneyTransfers(Long moneyTransfersId, String senderFirstName, String senderLastName, Long senderId, String receiverFirstName, String receiverLastName, Long receiverId, String description, LocalDate transferDate, BigDecimal transferAmount) {
        this.transferId = moneyTransfersId;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderId = senderId;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.receiverId = receiverId;
        this.description = description;
        this.transferDate = transferDate;
        this.transferAmount = transferAmount;
    }

    public MoneyTransfers() {
    }

    @Override
    public String toString() {
        return "MoneyTransfers{" +
                "MoneyTransfersId=" + transferId +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderId=" + senderId +
                ", receiverFirst='" + receiverFirstName + '\'' +
                ", receiverLastName='" + receiverLastName + '\'' +
                ", receiverId=" + receiverId +
                ", description='" + description + '\'' +
                ", transferDate=" + transferDate +
                ", transferAmount=" + transferAmount +
                '}';
    }
    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getReceiverFirst() {
        return receiverFirstName;
    }

    public void setReceiverFirstName(String receiverFirstName) {
        this.receiverFirstName = receiverFirstName;
    }

    public String getReceiverLastName() {
        return receiverLastName;
    }

    public void setReceiverLastName(String receiverLastName) {
        this.receiverLastName = receiverLastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getTransferDate() {
        return transferDate;
    }

    public void setTransferDate(LocalDate transferDate) {
        this.transferDate = transferDate;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public void setTransferId(Long moneyTransfersId) {
        transferId = moneyTransfersId;
    }

    public Long getTransferId() {
        return transferId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
