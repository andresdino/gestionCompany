package com.JVNTecnologias.gestion_compania.entity;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
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
@Table(name = "empleado")
@Builder(toBuilder = true)
public class EmpleadosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Long idEmpleado;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "email")
    private String email;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "fecha_creacion")
    private LocalDate createdAp;

    @Column(name = "fecha_actualizacion")
    private LocalDate updatedAt;

    @Column(name = "estado_registro")
    private EstadoRegistroEnum estadoRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sucursal_id", nullable = false)
    private SucursalEntity sucursal;
}
