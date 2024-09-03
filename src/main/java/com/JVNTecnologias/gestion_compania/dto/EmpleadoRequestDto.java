package com.JVNTecnologias.gestion_compania.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "Request solicitado para la creacion de un empleado.")
public class EmpleadoRequestDto {
    private Long idEmpleado;
    @Schema(description = "Id de la sucursal asignada.", example = "1")
    private Long idSucursal;

    @NotNull(message = "El nombre no puede ser nulo")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 100, message = "El nombre no puede tener más de 100 caracteres")
    @Schema(description = "Nombre del empleado.", example = "Carlos")
    private String nombre;

    @NotNull(message = "El apellido no puede ser nulo")
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(max = 100, message = "El apellido no puede tener más de 100 caracteres")
    @Schema(description = "Apellido del empleado", example = "Marcos")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser una dirección de correo electrónico válida")
    @Schema(description = "Email del empleado.", example = "carlos@gmail.com")
    private String email;

    @NotBlank(message = "El número de teléfono no puede estar vacío")
    @Pattern(regexp = "^[0-9]+$", message = "El número de teléfono debe contener solo números")
    @Size(min = 1, max = 15, message = "El número de teléfono debe tener entre 10 y 15 dígitos")
    @Schema(description = "Telefono del empleado.", example = "3214568750")
    private String telefono;
}
