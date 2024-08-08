package ru.job4j.socialmediaapi.service;

import ru.job4j.socialmediaapi.model.Relationship;
import ru.job4j.socialmediaapi.model.User;

public interface RelationshipService {

    Relationship createInvitationToFriend(User initiator, User potentialFriend);

    void acceptInvitation(User user, User initiator);

    void rejectInvitation(User user, User initiator);

    void breakFriendship(User user, User exFriend);

}
