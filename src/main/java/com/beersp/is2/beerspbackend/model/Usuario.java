package com.beersp.is2.beerspbackend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Entity
@Table(name = "usuarios")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario extends RepresentationModel<Usuario> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombreUsuario;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private Date fechaNacimiento;
    private String contrasena;
    private String email;
    private String nombre;
    private String apellidos;
    @Lob
    private Byte[] foto;
    private String procedencia;
    private String introduccion;

    public void añadirFoto(MultipartFile foto) {
        if (foto != null) {
            try {
                Byte[] byteObjects = new Byte[foto.getBytes().length];
                int i = 0;
                for (Byte b : foto.getBytes()) {
                    byteObjects[i++] = b;
                }
                this.setFoto(byteObjects);
            } catch (IOException e) {
                this.setFoto(null);
            }
        }
    }
}
