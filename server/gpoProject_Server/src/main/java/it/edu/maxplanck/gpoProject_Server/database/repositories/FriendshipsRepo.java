package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Friendship;
import it.edu.maxplanck.gpoProject_Server.database.model.User;

@Repository
public interface FriendshipsRepo extends JpaRepository<Friendship, Integer> {

	List<User> getFriends(int userId);

    Friendship createFriendship(int userId1, int userId2);

    boolean friendshipExists(int userId1, int userId2);
	
	@Query("SELECT CASE WHEN f.fkUser1.id = :userId THEN f.fkUser2 ELSE f.fkUser1 END FROM Friendship f WHERE f.fkUser1.id = :userId OR f.fkUser2.id = :userId")
	List<User> findFriendsByUserId(@Param("userId") Integer userId);
}