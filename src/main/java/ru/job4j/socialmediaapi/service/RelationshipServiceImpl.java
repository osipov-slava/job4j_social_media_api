package ru.job4j.socialmediaapi.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.socialmediaapi.model.Relationship;
import ru.job4j.socialmediaapi.model.Type;
import ru.job4j.socialmediaapi.model.User;
import ru.job4j.socialmediaapi.repository.RelationshipRepository;

@Service
@AllArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipRepository relationshipRepository;

    @Override
    public Relationship createInvitationToFriend(User initiator, User potentialFriend) {
        Relationship relationship = new Relationship();
        relationship.setUser(initiator);
        relationship.setPartner(potentialFriend);
        relationship.setType(Type.APPLICANT);
        return relationshipRepository.save(relationship);
    }

    @Transactional
    @Override
    public void acceptInvitation(User user, User initiator) {
        relationshipRepository.updateType(initiator, user, Type.FRIEND);
        if (relationshipRepository.findByUserAndPartner(user, initiator).isEmpty()) {
            relationshipRepository.save(new Relationship(null, user, initiator, Type.FRIEND));
        } else {
            relationshipRepository.updateType(user, initiator, Type.FRIEND);
        }
    }

    @Override
    public void rejectInvitation(User user, User initiator) {
        relationshipRepository.updateType(initiator, user, Type.SUBSCRIBER);
    }

    @Transactional
    @Override
    public void breakFriendship(User user, User exFriend) {
        relationshipRepository.updateType(user, exFriend, Type.NONE);
        relationshipRepository.updateType(exFriend, user, Type.SUBSCRIBER);
    }

}
