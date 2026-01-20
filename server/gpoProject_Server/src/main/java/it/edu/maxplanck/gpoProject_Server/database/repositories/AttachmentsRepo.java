package it.edu.maxplanck.gpoProject_Server.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.edu.maxplanck.gpoProject_Server.database.model.Attached;

@Repository
public interface AttachmentsRepo extends JpaRepository<Attached, Integer> {

    Attached createAttachment(int messageId, String path);

    List<Attached> getAttachmentsByMessage(int messageId);
}