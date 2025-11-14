package com.beersp.is2.beerspbackend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Table(name = "cervezas")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cerveza extends RepresentationModel<Cerveza> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nombre;
    @Lob
    private Byte[] foto;
    private String descripcion;
    private String estilo;
    private String procedencia;
    private String tamaño;
    private String formato;
    private Integer porcentajeAlcohol;
    private Integer amargor;
    private String color;

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
