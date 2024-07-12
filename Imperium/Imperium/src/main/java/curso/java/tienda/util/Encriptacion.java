package curso.java.tienda.util;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class Encriptacion {
	public static String encriptarPassword(String nuevaPassword) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		String nuevaPasswordEncriptada = passwordEncryptor.encryptPassword(nuevaPassword);
		return nuevaPasswordEncriptada;
	}
	
	public static boolean validarPassword(String passwordIntroducida, String passwordEncriptadaExistente) {
		StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();
		
		if (passwordEncryptor.checkPassword(passwordIntroducida, passwordEncriptadaExistente)) {
			return true;
		} else {
			return false;
		}
	}
}
