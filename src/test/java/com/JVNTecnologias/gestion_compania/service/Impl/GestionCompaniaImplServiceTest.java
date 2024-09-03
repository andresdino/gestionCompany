package com.JVNTecnologias.gestion_compania.service.Impl;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.CompaniaRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.mapper.CompaniasMapper;
import com.JVNTecnologias.gestion_compania.repository.CompaniaRepository;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class GestionCompaniaImplServiceTest {

    @InjectMocks
    CompaniaImplService gestionCompaniaImplService;

    @Mock
    CompaniaRepository companiaRepository;

    @Mock
    GeneradorRespuesta generadorRespuesta;

    @Mock
    CompaniasMapper companiasMapper;

    CompaniaRequestDto companiaRequestDto;
    CompaniaEntity companiaEntity;

    ResponseGenerico responseGenerico;
    Long id = 1L;

    @BeforeEach
    void setUp() {
        companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setNombre("Compania");
        companiaRequestDto.setDireccion("Calle 50 test");
        companiaRequestDto.setNit("2423423");
        companiaRequestDto.setTelefono("1234567890");
        companiaRequestDto.setEmail("email@email.com");

        companiaEntity = new CompaniaEntity();
        companiaEntity.setIdCompania(12L);

        responseGenerico = new ResponseGenerico();
        responseGenerico.setMessage(Constant.Message.COMPANIA_GUARDADA);
    }

    @Test
    void testGuardarCompaniaOK() {
        when(companiaRepository.save(any())).thenReturn(companiaEntity);
        when(generadorRespuesta.generarRespuesta(any(),any(),any(),any())).thenReturn(responseGenerico);

        var servicio = this.gestionCompaniaImplService.guardar(companiaRequestDto);

        assertNotNull(servicio);
        assertEquals(responseGenerico, servicio);
    }

    @Test
    void listarCompaniasConDatos() {
        CompaniaEntity companiaEntity = new CompaniaEntity();

        List<CompaniaEntity> listaCompanias = Collections.singletonList(companiaEntity);
        List<CompaniaRequestDto> listaCompaniasMapper = Collections.singletonList(new CompaniaRequestDto());

        when(companiaRepository.findAllByAllEstadoRegistro()).thenReturn(listaCompanias);
        when(companiasMapper.toListDto(listaCompanias)).thenReturn(listaCompaniasMapper);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, listaCompaniasMapper))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, listaCompaniasMapper));


        ResponseGenerico respuesta = gestionCompaniaImplService.listar();


        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.CONSULTA_EXITOSA, respuesta.getMessage());
        assertEquals(listaCompaniasMapper, respuesta.getData());
        verify(companiaRepository).findAllByAllEstadoRegistro();
        verify(generadorRespuesta).generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, listaCompaniasMapper);
    }

    @Test
    void listarCompaniasSinDatos() {
        when(companiaRepository.findAllByAllEstadoRegistro()).thenReturn(Collections.emptyList());
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.NO_DATA, null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findAllByAllEstadoRegistro();
        verify(generadorRespuesta).generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.NO_DATA, null);
    }

    @Test
    void listarCompaniasConExcepcion() {
        when(companiaRepository.findAllByAllEstadoRegistro()).thenThrow(new RuntimeException("Error"));

        when(generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null))
                .thenReturn(new ResponseGenerico(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null));

        ResponseGenerico respuesta = gestionCompaniaImplService.listar();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findAllByAllEstadoRegistro();
        verify(generadorRespuesta).generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null);
    }

    @Test
    void buscarPorIdExitoso() {
        CompaniaEntity companiaEntity = new CompaniaEntity();
        companiaEntity.setIdCompania(id);
        companiaEntity.setNombre("Compania Test");

        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(id);
        companiaRequestDto.setNombre("Compania Test");

        when(companiaRepository.findById(id)).thenReturn(java.util.Optional.of(companiaEntity));
        when(companiasMapper.toDto(companiaEntity)).thenReturn(companiaRequestDto);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, companiaRequestDto))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, companiaRequestDto));

        ResponseGenerico respuesta = gestionCompaniaImplService.buscarPorId(id);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.CONSULTA_EXITOSA, respuesta.getMessage());
        assertEquals(companiaRequestDto, respuesta.getData());
        verify(companiaRepository).findById(id);
        verify(generadorRespuesta).generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.CONSULTA_EXITOSA, companiaRequestDto);
    }

    @Test
    void buscarPorIdNoExiste() {
        when(companiaRepository.findById(id)).thenReturn(java.util.Optional.empty());
        when(generadorRespuesta.noExiste())
                .thenReturn(new ResponseGenerico(HttpStatus.NOT_FOUND, EstadosEnum.ERROR, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.buscarPorId(id);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findById(id);
        verifyNoMoreInteractions(companiasMapper);
        verify(generadorRespuesta).noExiste();
    }

    @Test
    void buscarPorIdConExcepcion() {
        when(companiaRepository.findById(id)).thenThrow(new RuntimeException("Error"));

        when(generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null))
                .thenReturn(new ResponseGenerico(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null));

        ResponseGenerico respuesta = gestionCompaniaImplService.buscarPorId(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findById(id);
        verifyNoMoreInteractions(companiasMapper);
        verify(generadorRespuesta).generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null);
    }

    @Test
    void actualizarCompaniaCoincideYExiste() {
        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(id);
        companiaRequestDto.setNombre("Nueva Compania");
        companiaRequestDto.setNit("123456789");
        companiaRequestDto.setPropietario("Propietario");
        companiaRequestDto.setDireccion("Direccion");
        companiaRequestDto.setTelefono("123456789");
        companiaRequestDto.setEmail("email@dominio.com");
        companiaRequestDto.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

        CompaniaEntity companiaEntity = new CompaniaEntity();
        companiaEntity.setIdCompania(id);

        CompaniaEntity companiaActualizada = companiaEntity.toBuilder()
                .nombre(companiaRequestDto.getNombre())
                .nit(companiaRequestDto.getNit())
                .propietario(companiaRequestDto.getPropietario())
                .direccion(companiaRequestDto.getDireccion())
                .telefono(companiaRequestDto.getTelefono())
                .email(companiaRequestDto.getEmail())
                .estadoRegistro(companiaRequestDto.getEstadoRegistro())
                .build();

        when(companiaRepository.findById(id)).thenReturn(java.util.Optional.of(companiaEntity));
        when(companiaRepository.save(companiaActualizada)).thenReturn(companiaActualizada);
        when(companiasMapper.toDto(companiaActualizada)).thenReturn(companiaRequestDto);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.COMPANIA_ACTUALIZADA, companiaRequestDto))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.COMPANIA_ACTUALIZADA, companiaRequestDto));

        ResponseGenerico respuesta = gestionCompaniaImplService.actualizar(id, companiaRequestDto);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.COMPANIA_ACTUALIZADA, respuesta.getMessage());
        assertEquals(companiaRequestDto, respuesta.getData());
        verify(companiaRepository).findById(id);
        verify(companiaRepository).save(companiaActualizada);
        verify(generadorRespuesta).generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.COMPANIA_ACTUALIZADA, companiaRequestDto);
    }

    @Test
    void actualizarCompaniaNoCoincideId() {
        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(2L);

        when(generadorRespuesta.noCoincide())
                .thenReturn(new ResponseGenerico(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, Constant.Message.ERROR_DATA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.actualizar(id, companiaRequestDto);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verifyNoInteractions(companiaRepository, companiasMapper);
        verify(generadorRespuesta).noCoincide();
    }

    @Test
    void actualizarCompaniaNoExiste() {
        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(id);

        when(companiaRepository.findById(id)).thenReturn(java.util.Optional.empty());
        when(generadorRespuesta.noExiste())
                .thenReturn(new ResponseGenerico(HttpStatus.NOT_FOUND, EstadosEnum.ERROR, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.actualizar(id, companiaRequestDto);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findById(id);
        verifyNoMoreInteractions(companiasMapper);
        verify(generadorRespuesta).noExiste();
    }

    @Test
    void actualizarCompaniaConExcepcion() {
        CompaniaRequestDto companiaRequestDto = new CompaniaRequestDto();
        companiaRequestDto.setIdCompania(id);

        when(companiaRepository.findById(id)).thenThrow(new RuntimeException("Error"));

        when(generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null))
                .thenReturn(new ResponseGenerico(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null));

        ResponseGenerico respuesta = gestionCompaniaImplService.actualizar(id, companiaRequestDto);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).findById(id);
        verifyNoMoreInteractions(companiasMapper);
        verify(generadorRespuesta).generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null);
    }

    @Test
    void buscarPorIdCompaniaExiste() {
        CompaniaEntity companiaEntity = new CompaniaEntity();
        companiaEntity.setIdCompania(id);

        when(companiaRepository.findById(id)).thenReturn(Optional.of(companiaEntity));

        CompaniaEntity resultado = gestionCompaniaImplService.buscarPorIdCompania(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdCompania());
        assertEquals(companiaEntity, resultado);
        verify(companiaRepository).findById(id);
    }

    @Test
    void buscarPorIdCompaniaNoExiste() {
        when(companiaRepository.findById(id)).thenReturn(Optional.empty());

        CompaniaEntity resultado = gestionCompaniaImplService.buscarPorIdCompania(id);

        assertNull(resultado);
        verify(companiaRepository).findById(id);
    }

    @Test
    void eliminarCompaniaConSucursalActiva() {
        when(companiaRepository.isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO)).thenReturn(false);
        when(generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, Constant.Message.SUCURSAL_ASIGNADA, null))
                .thenReturn(new ResponseGenerico(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, Constant.Message.SUCURSAL_ASIGNADA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.eliminar(id);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.SUCURSAL_ASIGNADA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO);
    }

    @Test
    void eliminarCompaniaExisteSinSucursalActiva() {
        CompaniaEntity companiaEntity = new CompaniaEntity();
        companiaEntity.setIdCompania(id);

        when(companiaRepository.isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO)).thenReturn(true);
        when(companiaRepository.findById(id)).thenReturn(Optional.of(companiaEntity));
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.eliminar(id);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.OPERACION_EXITO, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO);
        verify(companiaRepository).findById(id);
        verify(companiaRepository).marcarComoEliminado(id, LocalDate.now(), EstadoRegistroEnum.INACTIVO);
        verify(generadorRespuesta).generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, null);
    }

    @Test
    void eliminarCompaniaNoExiste() {
        when(companiaRepository.isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO)).thenReturn(true);
        when(companiaRepository.findById(id)).thenReturn(Optional.empty());
        when(generadorRespuesta.noExiste())
                .thenReturn(new ResponseGenerico(HttpStatus.NOT_FOUND, EstadosEnum.ERROR, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = gestionCompaniaImplService.eliminar(id);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(companiaRepository).isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO);
        verify(companiaRepository).findById(id);
    }

    @Test
    void eliminarCompaniaConExcepcion() {
        when(companiaRepository.isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO)).thenThrow(new RuntimeException("Error"));

        when(generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null))
                .thenReturn(new ResponseGenerico(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), null));

        ResponseGenerico respuesta = gestionCompaniaImplService.eliminar(id);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"), respuesta.getMessage());
        assertNull(respuesta.getData());

        verify(companiaRepository).isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO);
    }
}