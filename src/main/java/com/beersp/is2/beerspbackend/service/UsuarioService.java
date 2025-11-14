package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Usuario;
import com.beersp.is2.beerspbackend.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {
    @Autowired
    private final UsuarioRepository repository;

    public List<Usuario> obtenerUsuarios() {
        return repository.findAll();
    }

    public Usuario obtenerUsuario(Integer id) { return repository.findById(id).orElse(null);}

    public boolean existeUsuario(String nombreUsuario) { return repository.existsByNombreUsuario(nombreUsuario); }

    public boolean existeUsuario(Integer id) { return repository.existsById(id); }

    public Usuario crearUsuario(Usuario usuario) {
        return repository.save(usuario);
    }

    public void actualizarUsuario(Integer id, Usuario usuario) {
        Usuario usuarioActual = repository.findById(id).orElse(null);
        if (usuarioActual != null) {
            if (usuario.getNombreUsuario() != null) usuarioActual.setNombreUsuario(usuario.getNombreUsuario());
            if (usuario.getFechaNacimiento() != null) usuarioActual.setFechaNacimiento(usuario.getFechaNacimiento());
            if (usuario.getContrasena() != null) usuarioActual.setContrasena(usuario.getContrasena());
            if (usuario.getEmail() != null) usuarioActual.setEmail(usuario.getEmail());
            if (usuario.getNombre() != null) usuarioActual.setNombre(usuario.getNombre());
            if (usuario.getApellidos() != null) usuarioActual.setApellidos(usuario.getApellidos());
            if (usuario.getProcedencia() != null) usuarioActual.setProcedencia(usuario.getProcedencia());
            if (usuario.getIntroduccion() != null) usuarioActual.setIntroduccion(usuario.getIntroduccion());
            repository.save(usuarioActual);
        }
    }

    public void actualizarFotoUsuario(Integer id, MultipartFile foto) {
        Usuario usuarioActual = repository.findById(id).orElse(null);
        if (usuarioActual != null) {
            usuarioActual.añadirFoto(foto);
            repository.save(usuarioActual);
        }
    }

    public void eliminarUsuario(Integer id) {
        repository.deleteById(id);
    }


}
