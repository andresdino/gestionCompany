package com.JVNTecnologias.gestion_compania.dto;

import lombok.Data;

@Data
public class EmpleadoDto {
    private Long idEmpleado;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private SucursalDto sucursal;
}
