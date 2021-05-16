package org.tfg.controller;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.tfg.domain.Usuario;
import org.tfg.exception.DangerException;
import org.tfg.helper.H;
import org.tfg.helper.PRG;
import org.tfg.repositories.UsuarioRepository;
import org.tfg.services.MailService;

@Controller
public class AnonymousController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private MailService ms;

	@GetMapping("/")
	public String index(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		
		return "/home/home";
	}

	@GetMapping("/login")
	public String loginGet() {
		return "/home/login";
	}
	
	@PostMapping("/login")
	public String loginPost(ModelMap m, HttpSession s, @RequestParam("loginName") String loginName, @RequestParam("password") String pass) throws DangerException {
		
		
		
		if(usuarioRepository.getByLoginName(loginName) !=null ) {

			m.put("nombre","ja");
			
			return "/usuario/buscar";
		}else {
		PRG.error("Ya existe este nombre de usuario", "/login");
		}
		
		ArrayList <Usuario> allUsuarios = (ArrayList<Usuario>) usuarioRepository.findAll();
		
		for(Usuario usuario: allUsuarios) {
			
			if(usuario.getLoginName()==loginName && usuario.getPass()==pass) {
				
				//Caambiar para sacar auth de la base de datos
				H.isRolOK("auth", s);
				return "/home/home";
			}
			
		}
		
		PRG.error("Nombre de usuario o contraseña erroneos", "/");
		return "/home/registro";
	}


	@GetMapping("/registro")
	public String registroGet() {
		return "/home/registro";
	}

	@PostMapping("/registro")
	public String registroPost(ModelMap m, @RequestParam("loginName") String loginName,
			@RequestParam("password") String pass, @RequestParam("repass") String passConfirm,
			@RequestParam("email") String email, /*@RequestParam("nombre") String nombre,*/
			/*@RequestParam("apellido") String apellidos,*/ @RequestParam("fechaNacimiento") String fNacimiento) throws DangerException {
		
		
		
		if(pass != passConfirm) {
			
		}
		if (usuarioRepository.getByLoginName(loginName) != null) {
			PRG.error("Ya existe este nombre de usuario", "/login");
		}
		if (usuarioRepository.getByEmail(email) != null) {
			PRG.error("Ya existe una cuenta con este correo electrónico", "/login");
		}
		
		
		Usuario usuario = new Usuario();
		
		usuario.setLoginName(loginName);
		usuario.setPass(pass);
		usuario.setEmail(email);
		
		//Añadir el rol a los usuarios/admin etc
		//usuario.setRol();
		
		LocalDate fecha= LocalDate.parse(fNacimiento);
		
		usuario.setFechaNacimiento(fecha);
		
		usuarioRepository.save(usuario);
		
		//Cargar el servicio del email
		//MailService e= new MailService();
		
		//Cabecera del email
		String cabecera="Email de prueba";
		//cuerpo del email
		//editar para crear plantilla
		String cuerpo="<h1>Hola Dani</h1>"+"<br>"+"Te hasregistrado ypienso comerme a tu perrito<b>=D</b>";
		
		//EnviarEmail
		ms.enviarEmail(email, cabecera, cuerpo);
		
		File directorio = new File("/ruta/directorio_nuevo");
        if (!directorio.exists()) {
            if (directorio.mkdirs()) {
                System.out.println("Directorio creado");
            } else {
                System.out.println("Error al crear directorio");
            }
        }
		
		return "redirect:/";
	}
	
	
}
