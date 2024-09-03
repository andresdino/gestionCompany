package com.JVNTecnologias.gestion_compania.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompaniaDto {
    private Long idCompania;
    private String nombre;
}
