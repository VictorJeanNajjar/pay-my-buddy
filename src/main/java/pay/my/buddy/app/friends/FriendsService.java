package pay.my.buddy.app.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;

import java.util.Optional;

@Service
public class FriendsService {
    @Autowired
    public FriendsRepository friendsRepository;
    @Autowired
    public PersonRepository personRepository;
    public String addFriend(Long senderId, Long receiverId){
        Optional<Person> optionalSender = personRepository.findById(senderId);
        Optional<Person> optionalReceiver = personRepository.findById(receiverId);
        Friends friends = new Friends();
        if (optionalSender.isPresent() && optionalReceiver.isPresent()) {
            Person sender = optionalSender.get();
            Person receiver = optionalReceiver.get();
            friends.setSenderFirstName(sender.getFirstName());
            friends.setSenderLastName(sender.getLastName());
            friends.setReceiverFirstName(receiver.getFirstName());
            friends.setReceiverLastName(receiver.getLastName());
            friendsRepository.save(friends);
            return ("friend added");
        }
        else {
            return "incorrect Id";
        }
    }
}
