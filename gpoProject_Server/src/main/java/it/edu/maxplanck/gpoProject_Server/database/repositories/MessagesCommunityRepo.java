package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.MessageCommunity;

/**
 * Interfaccia che rappresenta le query da fare nel database dei messaggi delle community
 */
@Repository
public interface MessagesCommunityRepo extends JpaRepository<MessageCommunity, Integer> {

    List<MessageCommunity> getMessagesByFkChannel(int channelId);

    @Query(value = "SELECT * FROM messagesCommunity WHERE (fkChannel = :channelId AND id > :lastMessageId) ORDER BY id ASC LIMIT :limit", nativeQuery = true)
	List<MessageCommunity> findByFkChannelIDAndIDGreaterThanOrderByPkIDAsc(@Param("channelId") Integer channelId, @Param("lastMessageId") Integer lastMessageId, @Param("limit") Integer maxMessagesRead);
}