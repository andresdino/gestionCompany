package com.JVNTecnologias.gestion_compania.controller;

import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import com.JVNTecnologias.gestion_compania.service.IEmpleadoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmpleadoControllerTest {

    private String URL = "/v1/empleado/";
    private MockMvc mockMvc;

    @Mock
    private IEmpleadoService iEmpleadoService;

    @InjectMocks
    private EmpleadoController empleadoController;

    private ObjectMapper objectMapper;

    EmpleadoRequestDto empleadoRequestDto;
    ResponseGenerico responseGenerico;

    EmpleadosEntity empleado;
    Long idEmpleado = 1L;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController).build();

        empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setNombre("Nuevo Carlos");
        empleadoRequestDto.setApellido("cali");
        empleadoRequestDto.setEmail("cali@gmail.com");
        empleadoRequestDto.setTelefono("3224568750");

        responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage("La operación se realizó de manera correcta");
        responseGenerico.setData(Map.of(
                "idEmpleado", 7,
                "nombre", "Nuevo Carlos",
                "apellido", "cali",
                "email", "cali@gmail.com",
                "telefono", "3224568750",
                "sucursal", Map.of(
                        "companiaDto", Map.of(
                                "idCompania", 1,
                                "nombre", "Empresas olimpica S.A"
                        ),
                        "idSucursal", 1,
                        "nombre", "Almacen exito S.A",
                        "responsable", "Carlos tovar",
                        "direccion", "Calle 40 # 31-3A",
                        "telefono", "0018002882",
                        "email", "Olimpica@gmail.com",
                        "estadoRegistro", "ACTIVO"
                )
        ));

        empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(7L);
        empleado.setNombre("Nuevo Carlos");
        empleado.setApellido("cali");
        empleado.setEmail("cali@gmail.com");
        empleado.setTelefono("3224568750");
    }

    @Test
    void guardarCrearEmpleadoExitoso() throws Exception {
       when(iEmpleadoService.guardar(empleadoRequestDto)).thenReturn(responseGenerico);

        mockMvc.perform(post(URL + "guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(empleadoRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("La operación se realizó de manera correcta"))
                .andExpect(jsonPath("$.data.idEmpleado").value(7))
                .andExpect(jsonPath("$.data.nombre").value("Nuevo Carlos"))
                .andExpect(jsonPath("$.data.sucursal.companiaDto.nombre").value("Empresas olimpica S.A"));
    }

    @Test
    void testBadRequestGuardarEmpleadoError() throws Exception {
        when(iEmpleadoService.guardar(empleadoRequestDto)).thenReturn(responseGenerico);

        mockMvc.perform(post(URL + "/guardar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void listarEmpleadosExitoso() throws Exception {
        responseGenerico.setData(Collections.singletonList(empleado));
        when(iEmpleadoService.listar()).thenReturn(responseGenerico);

        mockMvc.perform(get(URL + "listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("La operación se realizó de manera correcta"))
                .andExpect(jsonPath("$.data[0].idEmpleado").value(7))
                .andExpect(jsonPath("$.data[0].nombre").value("Nuevo Carlos"));
    }

    @Test
    void listarEmpleadosNotFound() throws Exception {
        responseGenerico.setData(Collections.singletonList(empleado));
        when(iEmpleadoService.listar()).thenReturn(responseGenerico);

        mockMvc.perform(get(URL + "listars")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarEmpleadoPorIdExitoso() throws Exception {

        responseGenerico.setData(empleado);

        when(iEmpleadoService.buscarPorId(idEmpleado)).thenReturn(responseGenerico);

        mockMvc.perform(get(URL + "buscar/{id}", idEmpleado)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("La operación se realizó de manera correcta"))
                .andExpect(jsonPath("$.data.idEmpleado").value(7))
                .andExpect(jsonPath("$.data.nombre").value("Nuevo Carlos"));
    }

    @Test
    void buscarEmpleadoPorIdNoEncontrado() throws Exception {
        Long idEmpleado = 12L;
        responseGenerico.setStatus(HttpStatus.NOT_FOUND);
        responseGenerico.setEstadoOperacion(EstadosEnum.ERROR);
        responseGenerico.setMessage("Empleado no encontrado");

        when(iEmpleadoService.buscarPorId(idEmpleado)).thenReturn(responseGenerico);

        mockMvc.perform(get(URL + "buscar/{id}", idEmpleado)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.estadoOperacion").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Empleado no encontrado"));
    }

    @Test
    void testEliminarEmpleadoExitoso() throws Exception {
        ResponseGenerico response = new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, "La operacion se realizado de manera correcta", null);

        when(iEmpleadoService.eliminar(idEmpleado)).thenReturn(response);

        mockMvc.perform(delete(URL + "/eliminar/{id}", idEmpleado)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("La operacion se realizado de manera correcta"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(iEmpleadoService, times(1)).eliminar(idEmpleado);
    }

    @Test
    void testActualizarEmpleadoExitoso() throws Exception {
        EmpleadoRequestDto empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setIdEmpleado(idEmpleado);
        empleadoRequestDto.setNombre("Mario");
        empleadoRequestDto.setApellido("Marcos");
        empleadoRequestDto.setEmail("carlos@gmail.com");
        empleadoRequestDto.setTelefono("3214568750");

        ResponseGenerico response = new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, "La operacion se realizado de manera correcta", empleadoRequestDto);

        when(iEmpleadoService.actualizar(eq(idEmpleado), any(EmpleadoRequestDto.class))).thenReturn(response);

        mockMvc.perform(put(URL + "/actualizar/{id}", idEmpleado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idEmpleado\":1,\"nombre\":\"Mario\",\"apellido\":\"Marcos\",\"email\":\"carlos@gmail.com\",\"telefono\":\"3214568750\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("La operacion se realizado de manera correcta"))
                .andExpect(jsonPath("$.data.idEmpleado").value(idEmpleado))
                .andExpect(jsonPath("$.data.nombre").value("Mario"))
                .andExpect(jsonPath("$.data.apellido").value("Marcos"))
                .andExpect(jsonPath("$.data.email").value("carlos@gmail.com"))
                .andExpect(jsonPath("$.data.telefono").value("3214568750"));

        verify(iEmpleadoService, times(1)).actualizar(eq(idEmpleado), any(EmpleadoRequestDto.class));
    }

    @Test
    void testActualizarEmpleadoIdNoCoincide() throws Exception {
        EmpleadoRequestDto empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setIdEmpleado(2L);

        ResponseGenerico response = new ResponseGenerico(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, "Error: la información suministrada no coincide.", null);

        when(iEmpleadoService.actualizar(eq(idEmpleado), any(EmpleadoRequestDto.class))).thenReturn(response);

        mockMvc.perform(put(URL + "/actualizar/{id}", idEmpleado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idEmpleado\":2,\"nombre\":\"Mario\",\"apellido\":\"Marcos\",\"email\":\"carlos@gmail.com\",\"telefono\":\"3214568750\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.estadoOperacion").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Error: la información suministrada no coincide."))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(iEmpleadoService, times(1)).actualizar(eq(idEmpleado), any(EmpleadoRequestDto.class));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}