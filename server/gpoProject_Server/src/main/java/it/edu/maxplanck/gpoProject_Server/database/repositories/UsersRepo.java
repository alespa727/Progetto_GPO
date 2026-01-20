package it.edu.maxplanck.gpoProject_Server.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.User;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {
	
    boolean existsUserByUsername(String username);

    User createUser(String username, String encodedPassword);

    User findUserByUsername(String username);

    User findUserById(int userId);

    boolean checkPassword(int userId, String rawPassword);

    void updateUser(int userId, User updatedUser);

    void updateLastAccess(int userId);

    void deleteUser(int userId);
}