package it.edu.maxplanck.gpoProject_Server.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.tika.Tika;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

/**
 * Classe di utilita' Generica
 */
public class GenericUtil {

	public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	
	/**
	 * Crea una cartella nel path
	 * @param uploadPath
	 * @throws IOException
	 */
	public static void createDirectory(Path uploadPath) throws IOException {
	    Files.createDirectories(uploadPath);
	}
	
	/**
	 * Salva il file
	 * @param uploadPath
	 * @param imageBytes
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Path uploadPath, byte[] fileBytes, String fileName) throws IOException {
	    Path filePath = uploadPath.resolve(fileName);

	    Files.copy(new ByteArrayInputStream(fileBytes), filePath, StandardCopyOption.REPLACE_EXISTING);
	}
	
	/**
	 * Elimina il file se esiste
	 * @param uploadPath
	 * @param fileName
	 * @throws IOException
	 */
	public static void removeFile(Path uploadPath, String fileName) throws IOException {
	    Files.deleteIfExists(uploadPath.resolve(fileName));
	}
	
	/**
	 * Controlla se il file e' una immagine
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static boolean isFileValidImage(MultipartFile file) throws IOException {
	    Tika tika = new Tika();
	    String detectedType = tika.detect(file.getInputStream());
	    return detectedType.startsWith("image/");
	}
	
	/**
	 * Genera una stringa randomica con una misura definita e un pattern dato
	 * @param length
	 * @param pattern
	 * @return
	 */
	public static String generateString(int length, String pattern) {
	    SecureRandom random = new SecureRandom();
	    StringBuilder sb = new StringBuilder(length);

	    for (int i = 0; i < length; i++) {
	        sb.append(pattern.charAt(random.nextInt(pattern.length())));
	    }
	    return sb.toString();
	}
	
	
	/**
	 * Trasforma un inputstream in un array di byte trasformandolo in una immagine quadrata (una immagine squadrata in un quadrato)
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] makeSquare(InputStream inputStream) throws IOException {
	    // Carica l'immagine originale
	    BufferedImage original = ImageIO.read(inputStream);
	    if (original == null) throw new IOException("Immagine non valida o corrotta");

	    int w = original.getWidth();
	    int h = original.getHeight();

	    // Determina il lato più lungo per creare il quadrato perfetto
	    int targetSize = Math.max(w, h);

	    // Crea la tela quadrata
	    BufferedImage squareImage = new BufferedImage(targetSize, targetSize, BufferedImage.TYPE_INT_RGB);
	    Graphics2D g2d = squareImage.createGraphics();

	    // Migliora la qualità
	    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	    
	    // Sfondo bianco (opzionale se l'immagine copre tutto)
	    g2d.setColor(Color.WHITE);
	    g2d.fillRect(0, 0, targetSize, targetSize);

	    // Disegna l'immagine originale centrata
	    // In questo caso, essendo il quadrato grande quanto il lato corto, 
	    // l'immagine verrà "ritagliata" visivamente o centrata con bande
	    int x = (targetSize - w) / 2;
	    int y = (targetSize - h) / 2;
	    g2d.drawImage(original, x, y, w, h, null);
	    g2d.dispose();

	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(squareImage, "jpg", baos);
	    return baos.toByteArray();
	}
}
