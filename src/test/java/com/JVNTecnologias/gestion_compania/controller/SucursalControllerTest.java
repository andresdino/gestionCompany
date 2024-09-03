package com.JVNTecnologias.gestion_compania.controller;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.dto.SucursalRequestDto;
import com.JVNTecnologias.gestion_compania.service.ISucursalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SucursalControllerTest {

    private String URL = "/v1/sucursal/";
    private MockMvc mockMvc;

    @Mock
    private ISucursalService sucursalService;

    @InjectMocks
    private SucursalController sucursalController;
    SucursalRequestDto sucursalRequestDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sucursalController).build();

        sucursalRequestDto = new SucursalRequestDto();
        sucursalRequestDto.setIdCompania(34L);
        sucursalRequestDto.setNombre("Almacen exito S.A");
        sucursalRequestDto.setResponsable("Carlos tovar");
        sucursalRequestDto.setDireccion("Calle 40 # 31-3A");
        sucursalRequestDto.setTelefono("0018002882");
        sucursalRequestDto.setEmail("Olimpica@gmail.com");
        sucursalRequestDto.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
    }

    @Test
    public void testGuardarOk() throws Exception {
        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setEstadoOperacion(EstadosEnum.SUCCESS);
        response.setMessage("La operacion se realizado de manera correcta");
        response.setData(sucursalRequestDto);

        when(sucursalService.guardar(sucursalRequestDto)).thenReturn(response);

        mockMvc.perform(post(URL + "guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idCompania\": 34, \"nombre\": \"Almacen exito S.A\", \"responsable\": \"Carlos tovar\", " +
                                "\"direccion\": \"Calle 40 # 31-3A\", \"telefono\": \"0018002882\", \"email\": \"Olimpica@gmail.com\", \"estadoRegistro\": \"ACTIVO\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"OK\", \"estadoOperacion\": \"SUCCESS\", \"message\": \"La operacion se realizado de manera correcta\", \"data\": { \"idCompania\": 34, \"nombre\": \"Almacen exito S.A\", \"responsable\": \"Carlos tovar\", \"direccion\": \"Calle 40 # 31-3A\", \"telefono\": \"0018002882\", \"email\": \"Olimpica@gmail.com\", \"estadoRegistro\": \"ACTIVO\" } }"));
    }

    @Test
    public void testListar() throws Exception {
        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setEstadoOperacion(EstadosEnum.SUCCESS);
        response.setMessage("La operacion se realizado de manera correcta");
        response.setData(Arrays.asList(sucursalRequestDto));

        when(sucursalService.listar()).thenReturn(response);

        mockMvc.perform(get(URL + "listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"OK\", \"estadoOperacion\": \"SUCCESS\", \"message\": \"La operacion se realizado de manera correcta\", \"data\": [{ \"idCompania\": 34, \"nombre\": \"Almacen exito S.A\", \"responsable\": \"Carlos tovar\", \"direccion\": \"Calle 40 # 31-3A\", \"telefono\": \"0018002882\", \"email\": \"Olimpica@gmail.com\", \"estadoRegistro\": \"ACTIVO\" }] }"));
    }

    @Test
    public void testBuscarPorId() throws Exception {
        Long id = 1L;

        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setEstadoOperacion(EstadosEnum.SUCCESS);
        response.setMessage("La operacion se realizado de manera correcta");
        response.setData(sucursalRequestDto);

        when(sucursalService.buscarPorId(id)).thenReturn(response);

        mockMvc.perform(get("/v1/sucursal/buscar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"OK\", \"estadoOperacion\": \"SUCCESS\", \"message\": \"La operacion se realizado de manera correcta\", \"data\": { \"idCompania\": 34, \"nombre\": \"Almacen exito S.A\", \"responsable\": \"Carlos tovar\", \"direccion\": \"Calle 40 # 31-3A\", \"telefono\": \"0018002882\", \"email\": \"Olimpica@gmail.com\", \"estadoRegistro\": \"ACTIVO\" } }"));
    }

    @Test
    public void testActualizar() throws Exception {
        Long id = 1L;
        sucursalRequestDto.setIdCompania(34L);
        sucursalRequestDto.setIdSucursal(id);
        sucursalRequestDto.setNombre("Taller Cali S.A");
        sucursalRequestDto.setResponsable("Pedro tovar");
        sucursalRequestDto.setDireccion("Calle 40 # 31-3A");
        sucursalRequestDto.setTelefono("0018002882");
        sucursalRequestDto.setEmail("taller@gmail.com");
        sucursalRequestDto.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setEstadoOperacion(EstadosEnum.SUCCESS);
        response.setMessage("La operacion se realizado de manera correcta");
        response.setData(sucursalRequestDto);

        when(sucursalService.actualizar(id, sucursalRequestDto)).thenReturn(response);

        mockMvc.perform(put(URL + "actualizar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"idCompania\": 34, \"idSucursal\": 1, \"nombre\": \"Taller Cali S.A\", \"responsable\": \"Pedro tovar\", " +
                                "\"direccion\": \"Calle 40 # 31-3A\", \"telefono\": \"0018002882\", \"email\": \"taller@gmail.com\", \"estadoRegistro\": \"ACTIVO\" }"))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"OK\", \"estadoOperacion\": \"SUCCESS\", \"message\": \"La operacion se realizado de manera correcta\", " +
                        "\"data\": { \"idCompania\": 34, \"idSucursal\": 1, \"nombre\": \"Taller Cali S.A\", \"responsable\": \"Pedro tovar\", \"direccion\": \"Calle 40 # 31-3A\"," +
                        " \"telefono\": \"0018002882\", \"email\": \"taller@gmail.com\", \"estadoRegistro\": \"ACTIVO\" } }"));
    }

    @Test
    public void testEliminar() throws Exception {
        Long id = 1L;

        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setEstadoOperacion(EstadosEnum.SUCCESS);
        response.setMessage("Sucursal eliminada correctamente");

        when(sucursalService.eliminar(id)).thenReturn(response);

        mockMvc.perform(delete(URL + "eliminar/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{ \"status\": \"OK\", \"estadoOperacion\": \"SUCCESS\", \"message\": \"Sucursal eliminada correctamente\" }"));
    }






}