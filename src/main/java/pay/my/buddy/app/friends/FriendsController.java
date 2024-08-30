package pay.my.buddy.app.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.util.List;

@RestController
@RequestMapping("/friends")
public class FriendsController {
    @Autowired
    PersonService personService;
    @Autowired
    FriendsService friendsService;
    @Autowired
    FriendsRepository friendsRepository;
    @Autowired
    PersonRepository personRepository;
    public FriendsController() {
    }

    @PostMapping("/add")
    public String addFriendController(@RequestParam String friend2Username){
        return friendsService.addFriend(friend2Username);
    }
    @GetMapping("/viewAllFriends")
    public List<Friends> viewAllFriends(){return friendsRepository.findAll();}
    @DeleteMapping("/deleteFriendship")
    public String deleteFriendController(@RequestParam Long id){
        return friendsService.deleteFriend(id);
    }
    @GetMapping("/userFriendslist")
    public List<String> friendsListcontroller(){
        return friendsService.friendsList();
    }
}
