package com.JVNTecnologias.gestion_compania.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.dto.SucursalRequestDto;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import com.JVNTecnologias.gestion_compania.repository.SucursalRepository;
import com.JVNTecnologias.gestion_compania.service.ICompaniaService;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SucursalImplService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class SucursalImplServiceTest {
    @MockBean
    private GeneradorRespuesta generadorRespuesta;

    @MockBean
    private ICompaniaService iCompaniaService;

    @Autowired
    private SucursalImplService sucursalImplService;

    @MockBean
    private SucursalRepository sucursalRepository;

    CompaniaEntity compania;
    SucursalEntity sucursalEntity;

    CompaniaEntity companiaEntity;
    SucursalRequestDto sucursalRequestDto;
    ResponseGenerico responseGenerico;

    @BeforeEach
    void setUp() {
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

        sucursalEntity = new SucursalEntity();
        sucursalEntity.setCompania(compania);
        sucursalEntity.setCreatedAt(LocalDate.of(1970, 1, 1));
        sucursalEntity.setDireccion("Direccion");
        sucursalEntity.setEmail("jane.doe@example.org");
        sucursalEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        sucursalEntity.setIdSucursal(1L);
        sucursalEntity.setNombre("Nombre");
        sucursalEntity.setResponsable("Responsable");
        sucursalEntity.setTelefono("Telefono");
        sucursalEntity.setUpdatedAt(LocalDate.of(1970, 1, 1));

        companiaEntity  = new CompaniaEntity();
        companiaEntity.setCreatedAt(LocalDate.of(1970, 1, 1));
        companiaEntity.setDireccion("Direccion");
        companiaEntity.setEmail("jane.doe@example.org");
        companiaEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        companiaEntity.setIdCompania(1L);
        companiaEntity.setNit("Nit");
        companiaEntity.setNombre("Nombre");
        companiaEntity.setPropietario("Propietario");
        companiaEntity.setTelefono("Telefono");
        companiaEntity.setUpdatedAt(LocalDate.of(1970, 1, 1));
        when(iCompaniaService.buscarPorIdCompania(Mockito.<Long>any())).thenReturn(companiaEntity);

         sucursalRequestDto = new SucursalRequestDto();
        sucursalRequestDto.setDireccion("Direccion");
        sucursalRequestDto.setEmail("jane.doe@example.org");
        sucursalRequestDto.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        sucursalRequestDto.setIdCompania(1L);
        sucursalRequestDto.setIdSucursal(1L);
        sucursalRequestDto.setNombre("Nombre");
        sucursalRequestDto.setResponsable("Responsable");
        sucursalRequestDto.setTelefono("Telefono");


         responseGenerico = new ResponseGenerico();
    }

    @Test
    void testGuardar() {
        when(sucursalRepository.save(Mockito.<SucursalEntity>any())).thenReturn(sucursalEntity);
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);


        ResponseGenerico actualGuardarResult = sucursalImplService.guardar(sucursalRequestDto);

        verify(iCompaniaService).buscarPorIdCompania(eq(1L));
        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isA(Object.class));
        verify(sucursalRepository).save(isA(SucursalEntity.class));
        assertSame(responseGenerico, actualGuardarResult);
    }

    @Test
    void testGuardarCompniaNull(){
        responseGenerico.setMessage(Constant.Message.NO_EXISTE_COMPANIA);
        when(iCompaniaService.buscarPorIdCompania(anyLong())).thenReturn(null);
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);

        ResponseGenerico actualGuardarResult = sucursalImplService.guardar(sucursalRequestDto);

        assertNotNull(actualGuardarResult);
        assertEquals(Constant.Message.NO_EXISTE_COMPANIA, actualGuardarResult.getMessage());
        verify(iCompaniaService).buscarPorIdCompania(eq(1L));
    }

    @Test
    void testListar() {
        when(generadorRespuesta.noExiste()).thenReturn(responseGenerico);
        when(sucursalRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseGenerico actualListarResult = sucursalImplService.listar();

        verify(generadorRespuesta).noExiste();
        verify(sucursalRepository).findAll();
        assertSame(responseGenerico, actualListarResult);
    }

    @Test
    void testListarOK() {
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);

        ArrayList<SucursalEntity> sucursalEntityList = new ArrayList<>();
        sucursalEntityList.add(sucursalEntity);
        when(sucursalRepository.findAll()).thenReturn(sucursalEntityList);

        ResponseGenerico actualListarResult = sucursalImplService.listar();

        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isA(Object.class));
        verify(sucursalRepository).findAll();
        assertSame(responseGenerico, actualListarResult);
    }

    @Test
    void testBuscarPorId() {
        ResponseGenerico responseGenerico = new ResponseGenerico();
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);

        Optional<SucursalEntity> ofResult = Optional.of(sucursalEntity);
        when(sucursalRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ResponseGenerico actualBuscarPorIdResult = sucursalImplService.buscarPorId(1L);

        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isA(Object.class));
        verify(sucursalRepository).findById(eq(1L));
        assertSame(responseGenerico, actualBuscarPorIdResult);
    }

    @Test
    void testBuscarPorIdOk() {

        when(generadorRespuesta.noExiste()).thenReturn(responseGenerico);
        Optional<SucursalEntity> emptyResult = Optional.empty();
        when(sucursalRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        ResponseGenerico actualBuscarPorIdResult = sucursalImplService.buscarPorId(1L);

        verify(generadorRespuesta).noExiste();
        verify(sucursalRepository).findById(eq(1L));
        assertSame(responseGenerico, actualBuscarPorIdResult);
    }

    @Test
    void testActualizar() {
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);
        Optional<SucursalEntity> ofResult = Optional.of(sucursalEntity);
        when(sucursalRepository.save(Mockito.<SucursalEntity>any())).thenReturn(sucursalEntity);
        when(sucursalRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ResponseGenerico actualActualizarResult = sucursalImplService.actualizar(1L, sucursalRequestDto);
        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isA(Object.class));
        verify(sucursalRepository).findById(eq(1L));
        verify(sucursalRepository).save(isA(SucursalEntity.class));
        assertSame(responseGenerico, actualActualizarResult);
    }

    @Test
    void testEliminar() {
        Optional<SucursalEntity> ofResult = Optional.of(sucursalEntity);

        ResponseGenerico responseGenerico = new ResponseGenerico();
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);
        doNothing().when(sucursalRepository)
                .marcarComoEliminado(Mockito.<Long>any(), Mockito.<LocalDate>any(), Mockito.<EstadoRegistroEnum>any());
        when(sucursalRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        ResponseGenerico actualEliminarResult = sucursalImplService.eliminar(1L);

        verify(sucursalRepository).marcarComoEliminado(eq(1L), isA(LocalDate.class), eq(EstadoRegistroEnum.INACTIVO));
        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.OK), eq(EstadosEnum.SUCCESS),
                eq("La operacion se realizado de manera correcta"), isNull());
        verify(sucursalRepository).findById(eq(1L));
        assertSame(responseGenerico, actualEliminarResult);
    }
    @Test
    void testBuscarPorIdEntity() {
        Optional<SucursalEntity> ofResult = Optional.of(sucursalEntity);
        when(sucursalRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        SucursalEntity actualBuscarPorIdEntityResult = sucursalImplService.buscarPorIdEntity(1L);

        verify(sucursalRepository).findById(eq(1L));
        assertSame(sucursalEntity, actualBuscarPorIdEntityResult);
    }

    @Test
    void testActualizarDatosNoCoincide(){
        responseGenerico.setStatus(HttpStatus.BAD_REQUEST);
        responseGenerico.setMessage(Constant.Message.ERROR_DATA);
        when(generadorRespuesta.noCoincide()).thenReturn(responseGenerico);

        ResponseGenerico sucursalService = sucursalImplService.actualizar(12L, sucursalRequestDto);

        verify(generadorRespuesta).noCoincide();
        assertEquals(Constant.Message.ERROR_DATA, sucursalService.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, sucursalService.getStatus());
    }

    @Test
    void testActualizaErrorCatch(){
        responseGenerico.setMessage(Constant.Message.ERROR_CONSULTADO);
        responseGenerico.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        when(generadorRespuesta.generarRespuesta(Mockito.<HttpStatus>any(), Mockito.<EstadosEnum>any(),
                Mockito.<String>any(), Mockito.<Object>any())).thenReturn(responseGenerico);
        when(sucursalImplService.buscarPorIdEntity(anyLong())).thenReturn(null);

        ResponseGenerico servicio = sucursalImplService.actualizar(1L, sucursalRequestDto);

        assertEquals(Constant.Message.ERROR_CONSULTADO, servicio.getMessage());
        assertNotNull(servicio);
        verify(generadorRespuesta).generarRespuesta(eq(HttpStatus.INTERNAL_SERVER_ERROR), eq(EstadosEnum.ERROR),
                eq("Se ha presentado un error al consultar sucursal."), isNull());
    }
}
