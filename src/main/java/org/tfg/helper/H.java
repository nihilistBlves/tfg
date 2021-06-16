package org.tfg.helper;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.repositories.SeguimientoRepository;

public class H {
	
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
	
	public static void actualizarPeticiones (HttpSession s, SeguimientoRepository sr) {
		s.setAttribute("numPeticiones", sr.findSeguidoresNoAceptadosByIdUsuario(((Usuario) s.getAttribute("userLogged")).getId()).size());
	}
	
	public static SerialBlob convertidorBlob(MultipartFile file) throws IOException, SerialException, SQLException{
		byte[] bytes = file.getBytes();
		SerialBlob newBlob = new SerialBlob(bytes);
		return newBlob;
	}
}
