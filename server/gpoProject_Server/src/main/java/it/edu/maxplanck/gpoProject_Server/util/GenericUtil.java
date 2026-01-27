package it.edu.maxplanck.gpoProject_Server.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenericUtil {

	public static final String standardPathImages = "http://localhost:8080/images/";

	public static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	public static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	
	public static String generateString(int length, String pattern) {
	    SecureRandom random = new SecureRandom();
	    StringBuilder sb = new StringBuilder(length);

	    for (int i = 0; i < length; i++) {
	        sb.append(pattern.charAt(random.nextInt(pattern.length())));
	    }
	    return sb.toString();
	}
	
	public static byte[] makeSquare(InputStream inputStream) throws IOException {
	    // Carica l'immagine originale
	    BufferedImage original = ImageIO.read(inputStream);
	    if (original == null) throw new IOException("Immagine non valida o corrotta");

	    int w = original.getWidth();
	    int h = original.getHeight();

	    // Determina il lato più corto per creare il quadrato perfetto
	    int targetSize = Math.min(w, h);

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
