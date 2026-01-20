package it.edu.maxplanck.gpoProject_Server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Community;

@Repository
public interface CommunitiesRepo extends JpaRepository<Community, Integer> {

	Community createCommunity(int ownerId, String name, String description);

    Community getCommunityById(int communityId);

    Community getCommunityByInviteCode(String inviteCode);

    boolean isInviteCodeValid(String inviteCode);

    void invalidateInviteCode(int communityId);

    void deleteCommunity(int communityId);
}