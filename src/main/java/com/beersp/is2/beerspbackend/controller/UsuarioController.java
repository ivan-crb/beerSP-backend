package com.beersp.is2.beerspbackend.controller;

import com.beersp.is2.beerspbackend.model.Usuario;
import com.beersp.is2.beerspbackend.service.UsuarioService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @GetMapping("")
    public ResponseEntity<List<Usuario>> obtenerUsuarios() {
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerUsuario(@PathVariable Integer id) {
        if (!usuarioService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuarioService.obtenerUsuario(id));
    }

    @PostMapping("")
    public ResponseEntity<Void> crearUsuario(@RequestBody Usuario nuevoUsuario) {
        if (usuarioService.existeUsuario(nuevoUsuario.getNombreUsuario())) {
            return ResponseEntity.badRequest().build();
        }
        Usuario usuario = usuarioService.crearUsuario(nuevoUsuario);
        return ResponseEntity.created(linkTo(UsuarioController.class).slash(usuario.getId()).toUri()).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        if (!usuarioService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }
        if (usuario.getNombreUsuario() != null) {
            if (usuarioService.existeUsuario(usuario.getNombreUsuario())
            && !usuario.getNombreUsuario().equals(usuarioService.obtenerUsuario(id).getNombreUsuario())) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        usuarioService.actualizarUsuario(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/foto")
    public ResponseEntity<Void> actualizarFotoUsuario(@PathVariable Integer id,
                                                  @RequestParam(name = "foto") MultipartFile foto) {
        if (!usuarioService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.actualizarFotoUsuario(id, foto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (!usuarioService.existeUsuario(id)) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

}
