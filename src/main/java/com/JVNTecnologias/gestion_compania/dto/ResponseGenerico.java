package com.JVNTecnologias.gestion_compania.dto;

import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenerico {
    private HttpStatus status;
    private EstadosEnum estadoOperacion;
    private String message;
    private Object data;
}
