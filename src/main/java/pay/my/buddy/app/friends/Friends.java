package pay.my.buddy.app.friends;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
@Entity
public class Friends {
    @Id
    @GeneratedValue
    public long friendshipId;
    public String senderFirstName;
    public String senderLastName;
    public Long senderId;
    public String receiverFirstName;
    public String receiverLastName;
    public Long receiverId;

    public Friends(long friendshipId, String senderFirstName, String senderLastName, Long senderId, String receiverFirstName, String receiverLastName, Long receiverId) {
        this.friendshipId = friendshipId;
        this.senderFirstName = senderFirstName;
        this.senderLastName = senderLastName;
        this.senderId = senderId;
        this.receiverFirstName = receiverFirstName;
        this.receiverLastName = receiverLastName;
        this.receiverId = receiverId;
    }

    public Friends() {
    }

    @Override
    public String toString() {
        return "Friends{" +
                "friendshipId=" + friendshipId +
                ", senderFirstName='" + senderFirstName + '\'' +
                ", senderLastName='" + senderLastName + '\'' +
                ", senderId=" + senderId +
                ", receiverFirstName='" + receiverFirstName + '\'' +
                ", receiverLastName='" + receiverLastName + '\'' +
                ", receiverId=" + receiverId +
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

    public String getReceiverFirstName() {
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
}
