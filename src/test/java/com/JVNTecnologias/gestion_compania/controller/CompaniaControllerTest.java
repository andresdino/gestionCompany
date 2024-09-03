package com.JVNTecnologias.gestion_compania.controller;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.CompaniaRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.service.ICompaniaService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class CompaniaControllerTest {

    private String URL = "/v1/companias/";
    private MockMvc mockMvc;

    @Mock
    private ICompaniaService iCompaniaService;

    @InjectMocks
    private CompaniaController companiaController;

    LocalDate fechaCreacion = LocalDate.now();

    CompaniaEntity compania1;
    CompaniaEntity compania2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(companiaController).build();

        compania1  = new CompaniaEntity(1L, "Empresas olimpica S.A", "123456-7363", "Carlos mario Tovar",
                "Calle 40 # 31-3A", "0018002882", "Olimpica@gmail.com",fechaCreacion, null, EstadoRegistroEnum.ACTIVO);

        compania2 = new CompaniaEntity(2L, "Exito S.A", "123423456-7363", "Pedro mario Tovar",
                "Calle 41 # 31-3A", "0018002882", "exito@gmail.com", fechaCreacion, null, EstadoRegistroEnum.ACTIVO);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testGuardarCompaniaOK() throws Exception {
        CompaniaRequestDto requestDto = new CompaniaRequestDto();
        requestDto.setNombre("Empresas olimpica S.A");
        requestDto.setNit("123456-7363");
        requestDto.setPropietario("Carlos mario Tovar");
        requestDto.setDireccion("Calle 40 # 31-3A");
        requestDto.setTelefono("0018002882");
        requestDto.setEmail("Olimpica@gmail.com");


        ResponseGenerico response = new ResponseGenerico();
        response.setStatus(HttpStatus.OK);
        response.setMessage(Constant.Message.COMPANIA_GUARDADA);
        response.setData(requestDto);


        when(iCompaniaService.guardar(any(CompaniaRequestDto.class))).thenReturn(response);


        mockMvc.perform(post(URL+ "guardar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.data.nombre").value("Empresas olimpica S.A"))
                .andExpect(jsonPath("$.data.nit").value("123456-7363"))
                .andExpect(jsonPath("$.data.propietario").value("Carlos mario Tovar"))
                .andExpect(jsonPath("$.data.direccion").value("Calle 40 # 31-3A"))
                .andExpect(jsonPath("$.data.telefono").value("0018002882"))
                .andExpect(jsonPath("$.data.email").value("Olimpica@gmail.com"));


        verify(iCompaniaService, times(1)).guardar(any(CompaniaRequestDto.class));
    }

    @Test
    void listarTest() throws Exception {
        List<CompaniaEntity> companias = Arrays.asList(compania1, compania2);

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage(Constant.Message.OPERACION_EXITO);
        responseGenerico.setData(companias);


        when(iCompaniaService.listar()).thenReturn(responseGenerico);

        mockMvc.perform(get(URL+ "/listar")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value(Constant.Message.OPERACION_EXITO))
                .andExpect(jsonPath("$.data[0].idCompania").value(1))
                .andExpect(jsonPath("$.data[0].nombre").value("Empresas olimpica S.A"))
                .andExpect(jsonPath("$.data[1].idCompania").value(2))
                .andExpect(jsonPath("$.data[1].nombre").value("Exito S.A"));
    }
    @Test
    void listarNoFountTest() throws Exception {
        List<CompaniaEntity> companias = Arrays.asList(compania1, compania2);

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage(Constant.Message.OPERACION_EXITO);
        responseGenerico.setData(companias);


        when(iCompaniaService.listar()).thenReturn(responseGenerico);

        mockMvc.perform(get(URL+ "/lista")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorIdCompaniaExistente() throws Exception {
        Long idCompania = 1L;

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage("Se ha consultado de manera correcta.");
        responseGenerico.setData(compania1);

        when(iCompaniaService.buscarPorId(idCompania)).thenReturn(responseGenerico);


        mockMvc.perform(get(URL + "/buscar/{id}", idCompania)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Se ha consultado de manera correcta."))
                .andExpect(jsonPath("$.data.nombre").value("Empresas olimpica S.A"))
                .andExpect(jsonPath("$.data.nit").value("123456-7363"))
                .andExpect(jsonPath("$.data.propietario").value("Carlos mario Tovar"))
                .andExpect(jsonPath("$.data.direccion").value("Calle 40 # 31-3A"))
                .andExpect(jsonPath("$.data.telefono").value("0018002882"))
                .andExpect(jsonPath("$.data.email").value("Olimpica@gmail.com"))
                .andExpect(jsonPath("$.data.estadoRegistro").value("ACTIVO"));
    }

    @Test
    void buscarPorIdCompaniaNoExistente() throws Exception {
        Long idCompania = 99L;

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.NOT_FOUND);
        responseGenerico.setEstadoOperacion(EstadosEnum.ERROR);
        responseGenerico.setMessage("La compañía con el ID especificado no fue encontrada.");

        when(iCompaniaService.buscarPorId(idCompania)).thenReturn(responseGenerico);


        mockMvc.perform(get(URL+ "/buscar/{id}", idCompania)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.estadoOperacion").value("ERROR"))
                .andExpect(jsonPath("$.message").value("La compañía con el ID especificado no fue encontrada."));
    }

    @Test
    void actualizarCompaniaExistente() throws Exception {
        Long idCompania = 1L;
        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setNombre("Empresas olimpica S.A");
        companiaRequestDto.setNit("123456-7363");
        companiaRequestDto.setPropietario("Carlos mario Tovar");
        companiaRequestDto.setDireccion("Calle 40 # 31-3A");
        companiaRequestDto.setTelefono("0018002882");
        companiaRequestDto.setEmail("Olimpica@gmail.com");
        companiaRequestDto.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

        CompaniaEntity companiaActualizada = new CompaniaEntity(idCompania, "Empresas olimpica S.A", "123456-7363", "Carlos mario Tovar",
                "Calle 40 # 31-3A", "0018002882", "Olimpica@gmail.com", fechaCreacion, null, EstadoRegistroEnum.ACTIVO);

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage("Se ha actualizado de manera correcta.");
        responseGenerico.setData(companiaActualizada);


        when(iCompaniaService.actualizar(idCompania, companiaRequestDto)).thenReturn(responseGenerico);

        mockMvc.perform(put(URL + "actualizar/{id}", idCompania)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Empresas olimpica S.A\",\"nit\":\"123456-7363\"," +
                                "\"propietario\":\"Carlos mario Tovar\",\"direccion\":\"Calle 40 # 31-3A\"," +
                                "\"telefono\":\"0018002882\",\"email\":\"Olimpica@gmail.com\",\"estadoRegistro\":\"ACTIVO\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Se ha actualizado de manera correcta."))
                .andExpect(jsonPath("$.data.nombre").value("Empresas olimpica S.A"))
                .andExpect(jsonPath("$.data.nit").value("123456-7363"))
                .andExpect(jsonPath("$.data.propietario").value("Carlos mario Tovar"))
                .andExpect(jsonPath("$.data.direccion").value("Calle 40 # 31-3A"))
                .andExpect(jsonPath("$.data.telefono").value("0018002882"))
                .andExpect(jsonPath("$.data.email").value("Olimpica@gmail.com"))
                .andExpect(jsonPath("$.data.estadoRegistro").value("ACTIVO"));
    }

    @Test
    void actualizarCompaniaIdNoCoincide() throws Exception {
        Long idCompaniaUrl = 1L;
        Long idCompaniaBody = 2L;

        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(idCompaniaBody);
        companiaRequestDto.setNombre(compania1.getNombre());

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.BAD_REQUEST);
        responseGenerico.setEstadoOperacion(EstadosEnum.ERROR);
        responseGenerico.setMessage("Error: la información suministrada no coincide.");


        when(iCompaniaService.actualizar(idCompaniaUrl, companiaRequestDto)).thenReturn(responseGenerico);

        mockMvc.perform(put(URL+ "actualizar/{id}", idCompaniaUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\": 2,\"nombre\":\"Empresas olimpica S.A\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarCompaniaExistente() throws Exception {
        Long idCompania = 1L;

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.OK);
        responseGenerico.setEstadoOperacion(EstadosEnum.SUCCESS);
        responseGenerico.setMessage("Compañía eliminada correctamente.");


       when(iCompaniaService.eliminar(idCompania)).thenReturn(responseGenerico);


        mockMvc.perform(delete(URL + "/eliminar/{id}", idCompania)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.estadoOperacion").value("SUCCESS"))
                .andExpect(jsonPath("$.message").value("Compañía eliminada correctamente."));
    }

    @Test
    void eliminarCompaniaNoExistente() throws Exception {
        Long idCompania = 999L;

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setStatus(HttpStatus.NOT_FOUND);
        responseGenerico.setEstadoOperacion(EstadosEnum.ERROR);
        responseGenerico.setMessage("La compañía con el ID especificado no fue encontrada.");


        when(iCompaniaService.eliminar(idCompania)).thenReturn(responseGenerico);


        mockMvc.perform(delete(URL + "/eliminar/{id}", idCompania)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.estadoOperacion").value("ERROR"))
                .andExpect(jsonPath("$.message").value("La compañía con el ID especificado no fue encontrada."));
    }
}