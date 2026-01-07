package it.edu.maxplanck.gpoProject_Server.database.modelDB;

import java.time.Instant;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @Column(length = 20)
    private String username;

    @Column(length = 256)
    private String password;

    @Column(name = "is_admin")
    private Boolean isAdmin;

	@Column(name = "ultimo_accesso")
    private LocalDateTime ultimoAccesso;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "image_path", length = 100)
    private String imagePath;
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public LocalDateTime getUltimoAccesso() {
		return ultimoAccesso;
	}

	public void setUltimoAccesso(LocalDateTime ultimoAccesso) {
		this.ultimoAccesso = ultimoAccesso;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
}
