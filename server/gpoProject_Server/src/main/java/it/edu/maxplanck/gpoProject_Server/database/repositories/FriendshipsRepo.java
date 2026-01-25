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
	
	@Query("SELECT u FROM Friendship f JOIN User u ON (u.id = f.fkUser1.id AND f.fkUser2.id = :userId) OR (u.id = f.fkUser2.id AND f.fkUser1.id = :userId)")
	List<User> findFriendsByUserId(@Param("userId") Integer userId);
	
	@Query("SELECT f FROM Friendship f WHERE (f.fkUser1.id = :userId AND f.fkUser2.username = :userUsername) OR (f.fkUser2.id = :userId AND f.fkUser1.username = :userUsername)")
	Friendship findFriendByUser1IdUser2Username(@Param("userId") Integer userId, @Param("userUsername") String username);
	
	boolean existsByFkUser1AndFkUser2(User user1, User user2);
}