package com.JVNTecnologias.gestion_compania.dto;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SucursalDto {
    private CompaniaDto companiaDto;
    private Long idSucursal;
    private String nombre;
    private String responsable;
    private String direccion;
    private String telefono;
    private String email;
    private EstadoRegistroEnum estadoRegistro;
}
