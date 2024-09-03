package com.JVNTecnologias.gestion_compania.service.Impl;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoDto;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import com.JVNTecnologias.gestion_compania.mapper.EmpleadoMapper;
import com.JVNTecnologias.gestion_compania.repository.EmpleadoRepository;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmpleadoServiceTest {

    @InjectMocks
   EmpleadoService empleadoService;

    @Mock
    EmpleadoRepository empleadoRepository;

    @Mock
    EmpleadoMapper empleadoMapper;

    @Mock
    GeneradorRespuesta generadorRespuesta;

    @Mock
    SucursalImplService iSucursalService;

    EmpleadoRequestDto empleadoRequestDto;
    SucursalEntity sucursalEntity;

    EmpleadosEntity empleadosEntity;

    List<EmpleadosEntity> empleadosEntityList;
    List<EmpleadoDto> empleadoDtoList;
    EmpleadoDto empleadoDto;
    CompaniaEntity compania;

    Long idEmpleado = 1L;


    @BeforeEach
    void setUp() {
        empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setIdSucursal(1L);
        empleadoRequestDto.setNombre("Nombre test");
        empleadoRequestDto.setApellido("Apellido test");

        sucursalEntity = new SucursalEntity();
        sucursalEntity.setEstadoRegistro(EstadoRegistroEnum.INACTIVO);

        empleadosEntity = new EmpleadosEntity();
        empleadosEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        empleadosEntity.setSucursal(sucursalEntity);
        empleadosEntity.setNombre("Nombre test");
        empleadosEntity.setApellido("Apellido test");

        empleadosEntityList = new ArrayList<>();
        empleadosEntityList.add(empleadosEntity);

        empleadoDtoList = new ArrayList<>();
        empleadoDto = new EmpleadoDto();
        empleadoDto.setIdEmpleado(1L);
        empleadoDto.setNombre("Juan");
        empleadoDto.setApellido("Pérez");
        empleadoDtoList.add(empleadoDto);

         compania = new CompaniaEntity();
        compania.setCreatedAt(LocalDate.of(1970, 1, 1));
        compania.setDireccion("Direccion");
        compania.setEmail("jane.doe@example.org");
        compania.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        compania.setIdCompania(1L);
        compania.setNit("Nit");
        compania.setNombre("Nombre");
        compania.setPropietario("Propietario");
        compania.setTelefono("Telefono");
        compania.setUpdatedAt(LocalDate.of(1970, 1, 1));
    }


    @Test
    void guardarSucursalNoExiste() {
        when(iSucursalService.buscarPorIdEntity(empleadoRequestDto.getIdSucursal())).thenReturn(null);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal para crear el empleado no existe", null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal para crear el empleado no existe", null));

        ResponseGenerico respuesta = empleadoService.guardar(empleadoRequestDto);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.WARNING, respuesta.getEstadoOperacion());
        assertEquals("La sucursal para crear el empleado no existe", respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(iSucursalService).buscarPorIdEntity(empleadoRequestDto.getIdSucursal());
    }

    @Test
    void guardarSucursalInactiva() {
        Long idSucursal = 1L;
        EmpleadoRequestDto empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setIdSucursal(idSucursal);

        when(iSucursalService.buscarPorIdEntity(idSucursal)).thenReturn(sucursalEntity);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal se encuentra en estado inactiva", null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal se encuentra en estado inactiva", null));

        ResponseGenerico respuesta = empleadoService.guardar(empleadoRequestDto);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.WARNING, respuesta.getEstadoOperacion());
        assertEquals("La sucursal se encuentra en estado inactiva", respuesta.getMessage());
        assertNull(respuesta.getData());
        verify(iSucursalService).buscarPorIdEntity(idSucursal);
    }

    @Test
    void testGuardarSucursalNull() {
        when(iSucursalService.buscarPorIdEntity(empleadoRequestDto.getIdSucursal())).thenReturn(null);
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal para crear el empleado no existe", null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.WARNING, "La sucursal para crear el empleado no existe", null));

        ResponseGenerico respuesta = empleadoService.guardar(empleadoRequestDto);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.WARNING, respuesta.getEstadoOperacion());
        assertNull(respuesta.getData());
    }

    @Test
    void testGuardarSucursalOK() {
        empleadosEntity.setIdEmpleado(23L);
        sucursalEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        when(iSucursalService.buscarPorIdEntity(empleadoRequestDto.getIdSucursal())).thenReturn(sucursalEntity);
        when(empleadoMapper.toEmpleadosEntity(empleadoRequestDto)).thenReturn(empleadosEntity);
        when(empleadoRepository.save(any(EmpleadosEntity.class))).thenReturn(empleadosEntity);
        when(empleadoMapper.toEmpleadoDto(empleadosEntity)).thenReturn(new EmpleadoDto());
        when(generadorRespuesta.generarRespuesta(
                eq(HttpStatus.OK),
                eq(EstadosEnum.SUCCESS),
                eq(Constant.Message.OPERACION_EXITO),
                any(EmpleadoDto.class)
        )).thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, new EmpleadoDto()));


        ResponseGenerico respuesta = empleadoService.guardar(empleadoRequestDto);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertNotNull(respuesta.getData());
    }

    @Test
    void testListarEmpleadosNoExiste() {
        when(empleadoRepository.findAll()).thenReturn(Collections.emptyList());
        when(generadorRespuesta.noExiste())
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.NO_DATA,null));

        ResponseGenerico respuesta = empleadoService.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA,respuesta.getMessage());

    }

    @Test
    void testListarEmpleadosOk() {
        List<EmpleadosEntity> empleadosEntityList = new ArrayList<>();
        EmpleadosEntity empleado = new EmpleadosEntity();
        empleado.setIdEmpleado(1L);
        empleado.setNombre("Juan");
        empleado.setApellido("Pérez");
        empleadosEntityList.add(empleado);

        when(empleadoRepository.findAll()).thenReturn(empleadosEntityList);
        when(empleadoMapper.toEmpleadoDtos(empleadosEntityList)).thenReturn(empleadoDtoList);
        when(generadorRespuesta.generarRespuesta(
                eq(HttpStatus.OK),
                eq(EstadosEnum.SUCCESS),
                eq(Constant.Message.OPERACION_EXITO),
                eq(empleadoDtoList)
        )).thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDtoList));

        ResponseGenerico respuesta = empleadoService.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.OPERACION_EXITO, respuesta.getMessage());
        assertNotNull(respuesta.getData());
        assertFalse(((List<?>) respuesta.getData()).isEmpty());
        assertEquals(1, ((List<?>) respuesta.getData()).size());
    }

    @Test
    void testBuscarPorIdEmpleadoExiste() {
        EmpleadosEntity empleadosEntity = new EmpleadosEntity();
        empleadosEntity.setIdEmpleado(idEmpleado);
        empleadosEntity.setNombre("Juan");
        empleadosEntity.setApellido("Pérez");

        when(empleadoRepository.findById(idEmpleado)).thenReturn(Optional.of(empleadosEntity));
        when(empleadoMapper.toEmpleadoDto(empleadosEntity)).thenReturn(empleadoDto);
        when(generadorRespuesta.generarRespuesta(
                eq(HttpStatus.OK),
                eq(EstadosEnum.SUCCESS),
                eq(Constant.Message.OPERACION_EXITO),
                eq(empleadoDto)
        )).thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDto));

        ResponseGenerico respuesta = empleadoService.buscarPorId(idEmpleado);

        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.OPERACION_EXITO, respuesta.getMessage());
        assertNotNull(respuesta.getData());
        assertEquals(empleadoDto, respuesta.getData());
    }


    @Test
    void testBuscarPorIdEmpleadoNoExiste() {
        when(empleadoRepository.findById(idEmpleado)).thenReturn(Optional.empty());

        when(generadorRespuesta.noExiste()).thenReturn(new ResponseGenerico(HttpStatus.NOT_FOUND, EstadosEnum.ERROR, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = empleadoService.buscarPorId(idEmpleado);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
        assertNull(respuesta.getData());
    }

    @Test
    void testEliminarEmpleadoExistenteDebeMarcarComoEliminado() {
        LocalDate fechaActualizacion = LocalDate.now();
        EmpleadosEntity empleadoExistente = new EmpleadosEntity();
        empleadoExistente.setIdEmpleado(idEmpleado);

        when(empleadoRepository.findById(idEmpleado)).thenReturn(Optional.of(empleadoExistente));
        when(generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, null))
                .thenReturn(new ResponseGenerico(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, null));

        ResponseGenerico respuesta = empleadoService.eliminar(idEmpleado);

        verify(empleadoRepository, times(1)).marcarComoEliminado(eq(idEmpleado), eq(fechaActualizacion), eq(EstadoRegistroEnum.INACTIVO));
        assertEquals(HttpStatus.OK, respuesta.getStatus());
        assertEquals(EstadosEnum.SUCCESS, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.OPERACION_EXITO, respuesta.getMessage());
    }

    @Test
    void tetsEliminarEmpleadoNoExistenteDebeRetornarError() {
        when(empleadoRepository.findById(idEmpleado)).thenReturn(Optional.empty());
        when(generadorRespuesta.noExiste()).thenReturn(new ResponseGenerico(HttpStatus.NOT_FOUND, EstadosEnum.ERROR, Constant.Message.NO_DATA, null));

        ResponseGenerico respuesta = empleadoService.eliminar(idEmpleado);

        verify(empleadoRepository, never()).marcarComoEliminado(anyLong(), any(LocalDate.class), any(EstadoRegistroEnum.class));
        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.NO_DATA, respuesta.getMessage());
    }

    @Test
    void testEliminarCuandoOcurreExcepcionDebeRetornarError() {
        LocalDate fechaActualizacion = LocalDate.now();
        EmpleadosEntity empleadoExistente = new EmpleadosEntity();
        empleadoExistente.setIdEmpleado(idEmpleado);

        when(empleadoRepository.findById(idEmpleado)).thenReturn(Optional.of(empleadoExistente));
        doThrow(new RuntimeException("Error al actualizar el estado del empleado"))
                .when(empleadoRepository).marcarComoEliminado(eq(idEmpleado), eq(fechaActualizacion), eq(EstadoRegistroEnum.INACTIVO));
        when(generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "empleado"), null))
                .thenReturn(new ResponseGenerico(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s", "empleado"), null));

        ResponseGenerico respuesta = empleadoService.eliminar(idEmpleado);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatus());
        assertEquals(EstadosEnum.ERROR, respuesta.getEstadoOperacion());
        assertEquals(Constant.Message.ERROR_CONSULTADO.replace("%s", "empleado"), respuesta.getMessage());
    }

    @Test
    void testActualizar() {
        ResponseGenerico responseGenerico = new ResponseGenerico();
        Optional<EmpleadosEntity> ofResult = Optional.of(empleadosEntity);

        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);



        CompaniaEntity compania2 = new CompaniaEntity();
        compania2.setCreatedAt(LocalDate.of(1970, 1, 1));
        compania2.setDireccion("Direccion");
        compania2.setEmail("jane.doe@example.org");
        compania2.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        compania2.setIdCompania(1L);
        compania2.setNit("Nit");
        compania2.setNombre("Nombre");
        compania2.setPropietario("Propietario");
        compania2.setTelefono("Telefono");
        compania2.setUpdatedAt(LocalDate.of(1970, 1, 1));

        SucursalEntity sucursal2 = new SucursalEntity();
        sucursal2.setCompania(compania2);
        sucursal2.setCreatedAt(LocalDate.of(1970, 1, 1));
        sucursal2.setDireccion("Direccion");
        sucursal2.setEmail("jane.doe@example.org");
        sucursal2.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        sucursal2.setIdSucursal(1L);
        sucursal2.setNombre("Nombre");
        sucursal2.setResponsable("Responsable");
        sucursal2.setTelefono("Telefono");
        sucursal2.setUpdatedAt(LocalDate.of(1970, 1, 1));

        EmpleadosEntity empleadosEntity2 = new EmpleadosEntity();
        empleadosEntity2.setApellido("Apellido");
        empleadosEntity2.setCreatedAp(LocalDate.of(1970, 1, 1));
        empleadosEntity2.setEmail("jane.doe@example.org");
        empleadosEntity2.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        empleadosEntity2.setIdEmpleado(1L);
        empleadosEntity2.setNombre("Nombre");
        empleadosEntity2.setSucursal(sucursal2);
        empleadosEntity2.setTelefono("Telefono");
        empleadosEntity2.setUpdatedAt(LocalDate.of(1970, 1, 1));
        when(empleadoRepository.save(Mockito.<EmpleadosEntity>any())).thenReturn(empleadosEntity2);
        when(empleadoRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        CompaniaEntity compania3 = new CompaniaEntity();
        compania3.setCreatedAt(LocalDate.of(1970, 1, 1));
        compania3.setDireccion("Direccion");
        compania3.setEmail("jane.doe@example.org");
        compania3.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        compania3.setIdCompania(1L);
        compania3.setNit("Nit");
        compania3.setNombre("Nombre");
        compania3.setPropietario("Propietario");
        compania3.setTelefono("Telefono");
        compania3.setUpdatedAt(LocalDate.of(1970, 1, 1));

        SucursalEntity sucursalEntity = new SucursalEntity();
        sucursalEntity.setCompania(compania3);
        sucursalEntity.setCreatedAt(LocalDate.of(1970, 1, 1));
        sucursalEntity.setDireccion("Direccion");
        sucursalEntity.setEmail("jane.doe@example.org");
        sucursalEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        sucursalEntity.setIdSucursal(1L);
        sucursalEntity.setNombre("Nombre");
        sucursalEntity.setResponsable("Responsable");
        sucursalEntity.setTelefono("Telefono");
        sucursalEntity.setUpdatedAt(LocalDate.of(1970, 1, 1));
        when(iSucursalService.buscarPorIdEntity(Mockito.<Long>any())).thenReturn(sucursalEntity);

        EmpleadoRequestDto empleadoRequestDto = new EmpleadoRequestDto();
        empleadoRequestDto.setApellido("Apellido");
        empleadoRequestDto.setEmail("jane.doe@example.org");
        empleadoRequestDto.setIdEmpleado(1L);
        empleadoRequestDto.setIdSucursal(1L);
        empleadoRequestDto.setNombre("Nombre");
        empleadoRequestDto.setTelefono("Telefono");

        // Act
        ResponseGenerico actualActualizarResult = empleadoService.actualizar(1L, empleadoRequestDto);

        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isA(Object.class));
        verify(empleadoRepository).findById(eq(1L));
        verify(empleadoRepository).save(isA(EmpleadosEntity.class));
        assertSame(responseGenerico, actualActualizarResult);
    }


}