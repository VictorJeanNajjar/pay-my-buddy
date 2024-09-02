package pay.my.buddy.app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pay.my.buddy.app.friends.Friends;
import pay.my.buddy.app.friends.FriendsRepository;
import pay.my.buddy.app.friends.FriendsService;
import pay.my.buddy.app.person.Person;
import pay.my.buddy.app.person.PersonRepository;
import pay.my.buddy.app.person.PersonService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FriendTest {

    @Mock
    private FriendsRepository friendsRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonService personService;

    @InjectMocks
    private FriendsService friendsService;
    private Friends friendship;
    private Long currentUserId;
    private Long friendId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize a common state for each test
        currentUserId = 1L;
        friendId = 2L;

        friendship = new Friends();
        friendship.setFriendshipId(1L);
        friendship.setSenderId(currentUserId);
        friendship.setReceiverId(friendId);
    }

    @Test
    void addFriend_ShouldAddFriend_WhenValidRequest() {
        // Arrange
        String person2Username = "john_doe";
        Long senderId = 1L;
        Long receiverId = 2L;
        Person sender = new Person();
        sender.setPersonId(senderId);
        Person receiver = new Person();
        receiver.setPersonId(receiverId);
        receiver.setUsername(person2Username);

        when(personService.getCurrentUserId()).thenReturn(senderId);
        when(personRepository.findByUsername(person2Username)).thenReturn(Optional.of(receiver));
        when(personRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(friendsRepository.findBySenderIdAndAndReceiverId(senderId, receiverId)).thenReturn(Optional.empty());
        when(friendsRepository.findBySenderIdAndAndReceiverId(receiverId, senderId)).thenReturn(Optional.empty());

        // Act
        String result = friendsService.addFriend(person2Username);

        // Assert
        assertEquals("friend added", result);
        verify(friendsRepository, times(1)).save(any(Friends.class));
    }

    @Test
    void addFriend_ShouldNotAddFriend_WhenFriendshipAlreadyExists() {
        // Arrange
        String person2Username = "john_doe";
        Long senderId = 1L;
        Long receiverId = 2L;
        Person receiver = new Person();
        receiver.setPersonId(receiverId);
        receiver.setUsername(person2Username);

        when(personService.getCurrentUserId()).thenReturn(senderId);
        when(personRepository.findByUsername(person2Username)).thenReturn(Optional.of(receiver));
        when(friendsRepository.findBySenderIdAndAndReceiverId(senderId, receiverId)).thenReturn(Optional.of(new Friends()));

        // Act
        String result = friendsService.addFriend(person2Username);

        // Assert
        assertEquals("friends already", result);
        verify(friendsRepository, never()).save(any(Friends.class));
    }

    @Test
    public void testDeleteFriend_Success_Sender() {
        // Arrange
        when(friendsRepository.findById(1L)).thenReturn(Optional.of(friendship));
        when(personService.getCurrentUserId()).thenReturn(currentUserId);

        // Act
        String result = friendsService.deleteFriend(1L);

        // Assert
        assertEquals("friendship deleted", result);
        verify(friendsRepository, times(1)).delete(friendship);
    }

    // Test for successful deletion where current user is the receiver
    @Test
    public void testDeleteFriend_Success_Receiver() {
        // Arrange
        friendship.setSenderId(friendId);
        friendship.setReceiverId(currentUserId);

        when(friendsRepository.findById(1L)).thenReturn(Optional.of(friendship));
        when(personService.getCurrentUserId()).thenReturn(currentUserId);

        // Act
        String result = friendsService.deleteFriend(1L);

        // Assert
        assertEquals("friendship deleted", result);
        verify(friendsRepository, times(1)).delete(friendship);
    }

    // Test for unsuccessful deletion where current user is neither the sender nor the receiver
    @Test
    public void testDeleteFriend_InvalidUserForOperation() {
        // Arrange
        when(friendsRepository.findById(1L)).thenReturn(Optional.of(friendship));
        when(personService.getCurrentUserId()).thenReturn(3L); // Current user is neither sender nor receiver

        // Act
        String result = friendsService.deleteFriend(1L);

        // Assert
        assertEquals("invalid user for operation", result);
        verify(friendsRepository, never()).delete(friendship);
    }

    // Test for friendship not found
    @Test
    public void testDeleteFriend_FriendshipNotFound() {
        // Arrange
        when(friendsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            friendsService.deleteFriend(1L);
        });

        assertEquals("friendship not found", exception.getMessage());
        verify(friendsRepository, never()).delete(any(Friends.class));
    }

    @Test
    void friendsList_ShouldReturnSortedFriendList() {
        // Arrange
        Long currentUserId = 1L;
        Person currentUser = new Person();
        currentUser.setPersonId(currentUserId);

        Friends friend1 = new Friends();
        friend1.setReceiverFirstName("Alice");
        friend1.setReceiverLastName("Smith");
        friend1.setReceiverUsername("alice_smith");

        Friends friend2 = new Friends();
        friend2.setSenderFirstName("Bob");
        friend2.setSenderLastName("Brown");
        friend2.setSenderUsername("bob_brown");

        List<Friends> sentFriends = new ArrayList<>();
        sentFriends.add(friend1);
        List<Friends> receivedFriends = new ArrayList<>();
        receivedFriends.add(friend2);

        when(personService.getCurrentUserId()).thenReturn(currentUserId);
        when(personRepository.findById(currentUserId)).thenReturn(Optional.of(currentUser));
        when(friendsRepository.findBySenderId(currentUserId)).thenReturn(sentFriends);
        when(friendsRepository.findByReceiverId(currentUserId)).thenReturn(receivedFriends);

        // Act
        List<String> result = friendsService.friendsList();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Alice Smith username: alice_smith", result.get(0));
        assertEquals("Bob Brown username: bob_brown", result.get(1));
    }
}

