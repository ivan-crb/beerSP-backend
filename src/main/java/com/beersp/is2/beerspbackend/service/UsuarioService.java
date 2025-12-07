package com.beersp.is2.beerspbackend.service;

import com.beersp.is2.beerspbackend.model.Usuario;
import com.beersp.is2.beerspbackend.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

@Service
@AllArgsConstructor
public class UsuarioService {
    @Autowired
    private final UsuarioRepository repository;

    @Transactional
    public List<Usuario> obtenerUsuarios() {
        return repository.findAll();
    }

    @Transactional
    public Usuario obtenerUsuario(String nombreUsuario) { return repository.findByNombreUsuario(nombreUsuario).orElse(null);}

    @Transactional
    public Usuario obtenerUsuario(Integer id) { return repository.findById(id).orElse(null);}

    public boolean existeUsuario(String nombreUsuario) { return repository.existsByNombreUsuario(nombreUsuario); }

    public boolean existeUsuario(Integer id) { return repository.existsById(id); }

    public Usuario crearUsuario(Usuario usuario) {
        String to = usuario.getEmail();
        String from = "no-reply@beersp.es";

        final String username = "api";
        final String password = "99b5b7d1ccf431f22d66ac9a508477a9";

        String host = "live.smtp.mailtrap.io";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "2525");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject("Cuenta verificada");
            message.setText("Tu cuenta de email se ha verificado correctamente. ¡Bienvenido a BeerSP!");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
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
            if (usuario.getFoto() != null) usuarioActual.setFoto(usuario.getFoto());
            if (usuario.getProcedencia() != null) usuarioActual.setProcedencia(usuario.getProcedencia());
            if (usuario.getIntroduccion() != null) usuarioActual.setIntroduccion(usuario.getIntroduccion());
            repository.save(usuarioActual);
        }
    }

    public void eliminarUsuario(Integer id) {
        repository.deleteById(id);
    }
}