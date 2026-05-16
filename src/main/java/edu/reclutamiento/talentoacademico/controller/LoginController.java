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
public class LoginController {
    private final UsuarioService usuarioService;

    public LoginController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/login")
    public String login() {
        return "publico/login";
    }

    @PostMapping("/login")
    public String autenticar(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Usuario usuario = usuarioService.login(email, password).orElse(null);
        if (usuario == null) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "publico/login";
        }
        session.setAttribute("usuario", usuario);
        if (usuario.getRol() == RolUsuario.ADMIN) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/postulante/mis-postulaciones";
    }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new UsuarioDTO());
        return "publico/registro";
    }

    @PostMapping("/registro")
    public String registrar(@ModelAttribute UsuarioDTO usuario) {
        usuario.setRol("POSTULANTE");
        usuario.setActivo(true);
        usuarioService.guardar(usuario);
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
