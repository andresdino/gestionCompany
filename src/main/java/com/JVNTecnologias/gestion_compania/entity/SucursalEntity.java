package com.JVNTecnologias.gestion_compania.entity;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sucursal")
@Builder(toBuilder = true)
public class SucursalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Long idSucursal;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "responsable")
    private String responsable;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compania_id", nullable = false)
    private CompaniaEntity compania;
}
