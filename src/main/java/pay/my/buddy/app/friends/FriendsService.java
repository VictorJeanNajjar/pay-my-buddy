package pay.my.buddy.app.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendsService {
    @Autowired
    public FriendsRepository friendsRepository;
    @Autowired
    public PersonRepository personRepository;
    @Autowired
    PersonService personService;
    public String addFriend(String person2Username){
        Long senderId = personService.getCurrentUserId();
        Person receiver = personRepository.findByUsername(person2Username).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Long receiverId = receiver.getPersonId();
        Optional<Person> optionalSender = personRepository.findById(senderId);
        Friends friends = new Friends();
        Optional<Friends> existingFriendship = friendsRepository.findBySenderIdAndAndReceiverId(senderId, receiverId);
        Optional<Friends> existingFriendship2 = friendsRepository.findBySenderIdAndAndReceiverId(receiverId, senderId);
        if (existingFriendship.isEmpty() && existingFriendship2.isEmpty() && optionalSender.isPresent()) {
            Person sender = optionalSender.get();
            friends.setSenderFirstName(sender.getFirstName());
            friends.setSenderLastName(sender.getLastName());
            friends.setReceiverFirstName(receiver.getFirstName());
            friends.setReceiverLastName(receiver.getLastName());
            friends.setReceiverId(receiverId);
            friends.setReceiverUsername(receiver.getUsername());
            friends.setSenderId(senderId);
            friends.setSenderUsername(sender.getUsername());
            friendsRepository.save(friends);
            return ("friend added");
        }
        else {
            return ("friends already");
        }
    }
    public String deleteFriend(Long id){
        Friends deleteFriendship = friendsRepository.findById(id).orElseThrow(() -> new RuntimeException("friendship not found"));
        friendsRepository.delete(deleteFriendship);
        return "friendship deleted";
    }
    public List <String> friendsList(){
        Person currentUser = personRepository.findById(personService.getCurrentUserId()).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Long currentUserId = currentUser.getPersonId();
        List <String> friendsListOfUser = new ArrayList<>();
        List <Friends> peopleUserAdded = friendsRepository.findBySenderId(currentUserId);
        List<Friends> peopleThatAddedUser = friendsRepository.findByReceiverId(currentUserId);
        for (Friends receiverFriendsObject : peopleUserAdded){
            String receiverFriendFirstName = receiverFriendsObject.getReceiverFirstName();
            String receiverFriendLastName = receiverFriendsObject.getReceiverLastName();
            String receiverUsername = receiverFriendsObject.getReceiverUsername();
            String receiverFriendFirstAndLastNameAndUsername = receiverFriendFirstName + " " + receiverFriendLastName + " username: " + receiverUsername;
            friendsListOfUser.add(receiverFriendFirstAndLastNameAndUsername);

        }
        for (Friends senderFriendObject : peopleThatAddedUser){
            String senderFriendFirstName = senderFriendObject.getSenderFirstName();
            String senderFriendLastName = senderFriendObject.getSenderLastName();
            String senderFriendUsername = senderFriendObject.getSenderUsername();
            String senderFriendFirstAndLastNameAndUsername = senderFriendFirstName + " " + senderFriendLastName + " username: " + senderFriendUsername;
            friendsListOfUser.add(senderFriendFirstAndLastNameAndUsername);
        }
        Collections.sort(friendsListOfUser);
        return friendsListOfUser;
    }
}
