package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.User;

/**
 * Interfaccia che rappresenta le query da fare nel database delle amicizie
 */
@Repository
public interface FriendshipsRepo extends JpaRepository<Friendship, Integer> {
	
	@Query("SELECT u FROM Friendship f JOIN User u ON (u.id = f.fkUser1.id AND f.fkUser2.id = :userId) OR (u.id = f.fkUser2.id AND f.fkUser1.id = :userId) WHERE f.accepted = true")
	List<User> findFriendsByUserId(@Param("userId") Integer userId);


    @Query("SELECT u FROM Friendship f JOIN User u ON (u.id = f.fkUser1.id AND f.fkUser2.id = :userId) OR (u.id = f.fkUser2.id AND f.fkUser1.id = :userId) WHERE f.accepted = false")
    List<User> findFriendsRequestsByUserId(@Param("userId") Integer userId);
	
	@Query("SELECT f FROM Friendship f WHERE (f.fkUser1.id = :user1Id AND f.fkUser2.id = :user2Id) OR (f.fkUser2.id = :user1Id AND f.fkUser1.id = :user2Id)")
	Friendship findFriendByUser1IdUser2Id(@Param("user1Id") Integer user1Id, @Param("user2Id") Integer user2Id);
	
	boolean existsByFkUser1AndFkUser2(User user1, User user2);
}