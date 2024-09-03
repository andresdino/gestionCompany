package com.JVNTecnologias.gestion_compania.controller;

import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.dto.SucursalRequestDto;
import com.JVNTecnologias.gestion_compania.service.ISucursalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/sucursal", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Sucursal", description = "Operaciones relacionadas con la gestion de sucursal")
@AllArgsConstructor
public class SucursalController {

    ISucursalService sucursalService;

    @PostMapping("/guardar")
    @Operation(summary = "Método que permite la creación de una nueva sucursal.", description = "Devuelve objecto generico con la sucursal creada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se crea de manera correcta la sucursal.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"La operacion se realizado de manera correcta\",\"data\":{\"idSucursal\":3," +
                                    "\"nombre\":\"Pinturas Cali S.A\",\"responsable\":\"Pedro tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"taller@gmail.com\",\"createdAt\":\"2024-08-27\",\"updatedAt\":null," +
                                    "\"estadoRegistro\":\"ACTIVO\",\"compania\":{\"idCompania\":1,\"nombre\":\"Empresas olimpica S.A\"," +
                                    "\"nit\":\"123456-7363\",\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"createdAt\":\"2024-08-27\"," +
                                    "\"updatedAt\":null,\"estadoRegistro\":\"ACTIVO\"}}}"
                            )))
    })
    public ResponseEntity<ResponseGenerico> guardar(@Valid @RequestBody SucursalRequestDto sucursalRequestDto) {
        ResponseGenerico servicio = this.sucursalService.guardar(sucursalRequestDto);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar sucursales existentes", description = "Método que se encarga de listar todas las sucursales registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de sucursales",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"La operacion se realizado de manera correcta\",\"data\":[{\"companiaDto\":{\"idCompania\":1," +
                                    "\"nombre\":\"Empresas olimpica S.A\"},\"nombre\":null,\"responsable\":null,\"direccion\":null," +
                                    "\"telefono\":null,\"email\":null,\"estadoRegistro\":\"ACTIVO\"}]}"
                            )))
    })
    public ResponseEntity<ResponseGenerico> listar() {
        ResponseGenerico servicio = this.sucursalService.listar();
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @GetMapping("buscar/{id}")
    @Operation(summary = "Buscar sucursal por ID", description = "Devuelve la sucursal correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscar la sucursal por ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"La operacion se realizado de manera correcta\"," +
                                    "\"data\":{\"companiaDto\":{\"idCompania\":1,\"nombre\":\"Empresas olimpica S.A\"}," +
                                    "\"idSucursal\":3,\"nombre\":\"Pinturas Cali S.A\",\"responsable\":\"Pedro tovar\"," +
                                    "\"direccion\":\"Calle 40 # 31-3A\",\"telefono\":\"0018002882\",\"email\":\"taller@gmail.com\"," +
                                    "\"estadoRegistro\":\"ACTIVO\"}}"
                            ))),
            @ApiResponse(responseCode = "404", description = "La sucursal con el ID especificado no fue encontrada.")
    })
    public ResponseEntity<ResponseGenerico> buscarPorId(@PathVariable Long id) {
        ResponseGenerico servicio = this.sucursalService.buscarPorId(id);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @PutMapping("actualizar/{id}")
    @Operation(summary = "Actualizar la sucursal", description = "Método que permite actualizar la sucursal")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"La operacion se realizado de manera correcta\"," +
                                    "\"data\":{\"companiaDto\":{\"idCompania\":1,\"nombre\":\"Empresas olimpica S.A\"}," +
                                    "\"idSucursal\":2,\"nombre\":\"Taller Cali S.A\",\"responsable\":\"Pedro tovar\"," +
                                    "\"direccion\":\"Calle 40 # 31-3A\",\"telefono\":\"0018002882\"," +
                                    "\"email\":\"taller@gmail.com\",\"estadoRegistro\":\"ACTIVO\"}}"
                            ))),
            @ApiResponse(responseCode = "400", description = "La sucursal con el ID especificado y el ID del body no coincide.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"BAD_REQUEST\",\"estadoOperacion\":\"ERROR\"," +
                                    "\"message\":\"Error: la información suministrada no coincide.\",\"data\":null}"
                            )
                    )

            )
    })
    public ResponseEntity<ResponseGenerico> actualizar(@PathVariable Long id, @Valid @RequestBody SucursalRequestDto sucursalRequestDto) {
        ResponseGenerico servicio = this.sucursalService.actualizar(id, sucursalRequestDto);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @DeleteMapping("eliminar/{id}")
    @Operation(summary = "Eliminar la sucursal", description = "Método que permite eliminar una sucursal en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucursal eliminada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"Se ha actualizado de manera correcta.\"," +
                                    "\"data\":{\"nombre\":\"Empresas olimpica S.A\",\"nit\":\"123456-7363\"," +
                                    "\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"estadoRegistro\":ACTIVO}}"
                            ))),
            @ApiResponse(responseCode = "404", description = "La sucursal con el ID especificado no se encuentra.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"BAD_REQUEST\",\"estadoOperacion\":\"ERROR\"," +
                                    "\"message\":\"Error: La sucursal con el ID especificado no se encuentra.\",\"data\":null}"
                            )
                    )

            )
    })
    public ResponseEntity<ResponseGenerico> eliminar(@PathVariable Long id) {
        ResponseGenerico servicio = this.sucursalService.eliminar(id);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }
}
