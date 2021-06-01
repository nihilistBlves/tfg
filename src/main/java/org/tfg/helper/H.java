package org.tfg.helper;

import javax.servlet.http.HttpSession;

import org.springframework.ui.ModelMap;
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
	
	public static void mPut(ModelMap m, HttpSession s) {
		m.put("infoModal", s.getAttribute("infoModal"));
		m.put("infoTitulo", s.getAttribute("infoTitulo"));
		m.put("infoTexto", s.getAttribute("infoTexto"));
		m.put("infoEstado", s.getAttribute("infoEstado"));
		s.removeAttribute("infoTitulo");
		s.removeAttribute("infoTexto");
		s.removeAttribute("infoEstado");
		s.removeAttribute("infoModal");
	}
	
	public static void setInfoModal(String cadenaSinSeparar, HttpSession s) {
		String[] cadenaSeparada = cadenaSinSeparar.split("\\|");
		s.setAttribute("infoModal", "true");
		s.setAttribute("infoTitulo", cadenaSeparada[0]);
		s.setAttribute("infoTexto", cadenaSeparada[1]);
		s.setAttribute("infoEstado", cadenaSeparada[2]);
	}
	

}
