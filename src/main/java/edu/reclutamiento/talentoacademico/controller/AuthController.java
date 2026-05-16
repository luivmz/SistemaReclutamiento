package edu.reclutamiento.talentoacademico.controller;

import edu.reclutamiento.talentoacademico.dto.UsuarioDTO;
import edu.reclutamiento.talentoacademico.model.RolUsuario;
import edu.reclutamiento.talentoacademico.model.Usuario;
import edu.reclutamiento.talentoacademico.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String mensaje, Model model) {
        model.addAttribute("mensaje", mensaje);
        return "publico/login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Usuario usuario = usuarioService.login(email, password).orElse(null);
        if (usuario == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "publico/login";
        }
        guardarSesion(session, usuario);
        if (usuario.getRol() == RolUsuario.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/postulante/dashboard";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "publico/registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute UsuarioDTO usuario, HttpSession session) {
        usuario.setRol("POSTULANTE");
        usuario.setActivo(true);
        UsuarioDTO creado = usuarioService.guardar(usuario);
        Usuario entidad = usuarioService.buscarPorId(creado.getId()).orElseThrow();
        guardarSesion(session, entidad);
        return "redirect:/postulante/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "publico/acceso-denegado";
    }

    private void guardarSesion(HttpSession session, Usuario usuario) {
        session.setAttribute("usuarioId", usuario.getId());
        session.setAttribute("nombre", usuario.getNombre());
        session.setAttribute("email", usuario.getEmail());
        session.setAttribute("telefono", usuario.getTelefono());
        session.setAttribute("rol", usuario.getRol().name());
    }
}
