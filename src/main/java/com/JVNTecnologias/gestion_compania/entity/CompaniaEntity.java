package com.JVNTecnologias.gestion_compania.entity;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name ="compania")
@Builder(toBuilder = true)
public class CompaniaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compania")
    private Long idCompania;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "nit")
    private String nit;

    @Column(name = "propietario")
    private String propietario;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "email")
    private String email;

    @Column(name = "fecha_creacion")
    private LocalDate createdAt;

    @Column(name = "fecha_actualizacion")
    private LocalDate updatedAt;

    @Column(name = "estado_registro")
    private EstadoRegistroEnum estadoRegistro;
}
