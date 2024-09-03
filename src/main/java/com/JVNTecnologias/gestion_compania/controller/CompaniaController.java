package com.JVNTecnologias.gestion_compania.controller;


import com.JVNTecnologias.gestion_compania.dto.CompaniaRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.service.ICompaniaService;
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
@RequestMapping(value = "v1/companias", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Compañia", description = "Operaciones relacionadas con las compañias")
@AllArgsConstructor
public class CompaniaController {

    ICompaniaService iCompaniaService;

    @PostMapping("/guardar")
    @Operation(summary = "Método que permite la creación de una nueva compañía.", description = "Devuelve objecto generico con la compañia creada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Se crea de manera correcta la compañía.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"message\":\"Se ha guardado el compania\"," +
                                    "\"data\":{\"idCompania\":52,\"nombre\":\"Empresas olimpica S.A\"," +
                                    "\"nit\":\"123456-7363\",\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"createdAt\":\"2024-08-24\",\"updatedAt\":null}}"
                            )))
    })
    public ResponseEntity<ResponseGenerico> guardar(@Valid @RequestBody CompaniaRequestDto companiaRequestDto) {
        ResponseGenerico servicio = this.iCompaniaService.guardar(companiaRequestDto);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @GetMapping("/listar")
    @Operation(summary = "Listar compañias existentes", description = "Método que se encarga de listar todas las compañias registradas en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de compañias",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"Se ha consultado de manera correcta.\",\"data\":[{\"idCompania\":1,\"nombre\":\"Empresas olimpica S.A\"," +
                                    "\"nit\":\"123456-7363\",\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"createdAt\":\"2024-08-26\",\"updatedAt\":null," +
                                    "\"estadoRegistro\":ACTIVO},{\"idCompania\":2,\"nombre\":\"Exito S.A\",\"nit\":\"123423456-7363\"," +
                                    "\"propietario\":\"Pedro mario Tovar\",\"direccion\":\"Calle 41 # 31-3A\",\"telefono\":\"0018002882\"," +
                                    "\"email\":\"exito@gmail.com\",\"createdAt\":\"2024-08-26\",\"updatedAt\":null,\"estadoRegistro\":ACTIVO}]}"
                            )))
    })
    public ResponseEntity<ResponseGenerico> listar() {
        ResponseGenerico servicio = this.iCompaniaService.listar();
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }
    @GetMapping("buscar/{id}")
    @Operation(summary = "Buscar compañía por ID", description = "Devuelve la compañía correspondiente al ID especificado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscar la compañia por ID",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"Se ha consultado de manera correcta.\"," +
                                    "\"data\":{\"nombre\":\"Empresas olimpica S.A\",\"nit\":\"123456-7363\"," +
                                    "\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"estadoRegistro\":ACTIVO}}"
                            ))),
            @ApiResponse(responseCode = "404", description = "La compañía con el ID especificado no fue encontrada.")
    })
    public ResponseEntity<ResponseGenerico> buscarPorId(@PathVariable Long id) {
        ResponseGenerico servicio = this.iCompaniaService.buscarPorId(id);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @PutMapping("actualizar/{id}")
    @Operation(summary = "Actualizar la compañia", description = "Método que permite actualizar la compañia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compañia actualizada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"Se ha actualizado de manera correcta.\"," +
                                    "\"data\":{\"nombre\":\"Empresas olimpica S.A\",\"nit\":\"123456-7363\"," +
                                    "\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"estadoRegistro\":ACTIVO}}"
                            ))),
            @ApiResponse(responseCode = "400", description = "La compañía con el ID especificado y el ID del body no coincide.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"BAD_REQUEST\",\"estadoOperacion\":\"ERROR\"," +
                                    "\"message\":\"Error: la información suministrada no coincide.\",\"data\":null}"
                            )
                    )

            )
    })
    public ResponseEntity<ResponseGenerico> actualizar(@PathVariable Long id, @Valid @RequestBody CompaniaRequestDto companiaRequestDto) {
        ResponseGenerico servicio = this.iCompaniaService.actualizar(id, companiaRequestDto);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }

    @DeleteMapping("eliminar/{id}")
    @Operation(summary = "Eliminar la compañia", description = "Método que permite eliminar una compañia en especifico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Compañia eliminada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"OK\",\"estadoOperacion\":\"SUCCESS\"," +
                                    "\"message\":\"Se ha actualizado de manera correcta.\"," +
                                    "\"data\":{\"nombre\":\"Empresas olimpica S.A\",\"nit\":\"123456-7363\"," +
                                    "\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                    "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"estadoRegistro\":ACTIVO}}"
                            ))),
            @ApiResponse(responseCode = "400", description = "La compañía con el ID especificado y el ID del body no coincide.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResponseGenerico.class),
                            examples = @ExampleObject(name = "example1", value = "{\"status\":\"BAD_REQUEST\",\"estadoOperacion\":\"ERROR\"," +
                                    "\"message\":\"Error: la información suministrada no coincide.\",\"data\":null}"
                            )
                    )

            )
    })
    public ResponseEntity<ResponseGenerico> eliminar(@PathVariable Long id) {
        ResponseGenerico servicio = this.iCompaniaService.eliminar(id);
        return new ResponseEntity<>(servicio, servicio.getStatus());
    }
}
