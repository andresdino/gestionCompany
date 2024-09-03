package com.JVNTecnologias.gestion_compania.dto;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Request solicitado para la creación de una compañía.")
public class CompaniaRequestDto {
    private Long idCompania;

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Schema(description = "Nombre de la compañía.", example = "Empresas olimpica S.A")
    private String nombre;

    @NotNull(message = "El NIT no puede ser nulo")
    @NotBlank(message = "El NIT no puede estar vacío")
    @Size(max = 100, message = "El NIT no puede tener más de 100 caracteres")
    @Schema(description = "NIT de la compañía.", example = "123456-7363")
    private String nit;

    @NotNull(message = "El propietario no puede ser nulo")
    @NotBlank(message = "El propietario no puede estar vacío")
    @Size(max = 100, message = "El propietario no puede tener más de 100 caracteres")
    @Schema(description = "Propietario de la compañía.", example = "Carlos mario Tovar")
    private String propietario;

    @NotNull(message = "El direccion no puede ser nulo")
    @NotBlank(message = "El direccion no puede estar vacío")
    @Size(max = 100, message = "El direccion no puede tener más de 100 caracteres")
    @Schema(description = "Direccion de la compañía.", example = "Calle 40 # 31-3A")
    private String direccion;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de teléfono debe contener solo números")
    @Size(min = 1, max = 15, message = "El número de teléfono debe tener entre 10 y 15 dígitos")
    @Schema(description = "Telefono de la compañía.", example = "0018002882")
    private String telefono;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser una dirección de correo electrónico válida")
    @Schema(description = "Email de la compañía.", example = "Olimpica@gmail.com")
    private String email;

    @Schema(description = "El campo estado en la creacion de la compañia por defecto se toma en ACTIVO", example = "ACTIVO")
    private EstadoRegistroEnum estadoRegistro;

    List<SucursalRequestDto>sucursales;

}
