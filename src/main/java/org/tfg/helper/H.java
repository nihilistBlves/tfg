package org.tfg.helper;

import javax.servlet.http.HttpSession;

import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;

public class H {
	/**
	 * 
	 * @param 	rolExigido 			Tres posibilidades "anon", "auth", "admin"
	 * @param 	s   				la sesión activa
	 * @throws 	DangerException		si el rol no coincide con el del usuario activo
	 */
	public static void isRolOK(String rolExigido, HttpSession s) throws DangerException {
		String rolActual = "anon";
		
		if (s.getAttribute("user") != null) {
			rolActual = ((Usuario)s.getAttribute("user")).isAdmin() ? "admin" : "auth";
		}
		System.err.println("ROL="+rolActual);

		if ((rolActual=="anon" || rolActual=="auth") && rolExigido=="admin") {
			throw new DangerException("Rol inadecuado");
		}
		
		if ((rolActual=="anon") && rolExigido=="auth") {
			throw new DangerException("Rol inadecuado");
		}
		
		if ((rolActual!="anon") && rolExigido=="anon") {
			throw new DangerException("Rol inadecuado");
		}
		
		
	}

}
