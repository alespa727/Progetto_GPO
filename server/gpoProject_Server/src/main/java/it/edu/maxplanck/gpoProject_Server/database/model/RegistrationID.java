package it.edu.maxplanck.gpoProject_Server.database.model;

import java.io.Serializable;
import java.util.Objects;

// Deve implementare Serializable
public class RegistrationID implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idCommunity; // Il nome deve coincidere con l'attributo nell'entità Registration
    private Integer idUser;      // Il tipo deve coincidere con la PK di User/Community

    public RegistrationID() {}

    public RegistrationID(Integer fkCommunity, Integer fkUser) {
        this.idCommunity = fkCommunity;
        this.idUser = fkUser;
    }

    // Fondamentale implementare equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegistrationID that = (RegistrationID) o;
        return Objects.equals(idCommunity, that.idCommunity) && Objects.equals(idUser, that.idUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCommunity, idUser);
    }
}