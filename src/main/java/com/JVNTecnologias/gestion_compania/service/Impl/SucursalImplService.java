package com.JVNTecnologias.gestion_compania.service.Impl;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.*;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import com.JVNTecnologias.gestion_compania.mapper.SucursalMapper;
import com.JVNTecnologias.gestion_compania.repository.SucursalRepository;
import com.JVNTecnologias.gestion_compania.service.ICompaniaService;
import com.JVNTecnologias.gestion_compania.service.ISucursalService;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class SucursalImplService implements ISucursalService {

    private final GeneradorRespuesta generadorRespuesta;
    SucursalRepository sucursalRepository;
    ICompaniaService companiaService;
    private final SucursalMapper sucursalMapper = SucursalMapper.INSTANCE;

    @Override
    public ResponseGenerico guardar(SucursalRequestDto sucursalRequestDto) {
        CompaniaEntity companiaEntity = this.companiaService.buscarPorIdCompania(sucursalRequestDto.getIdCompania());
        if (companiaEntity == null) {
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.NO_EXISTE_COMPANIA,null);
        }
        if (companiaEntity.getEstadoRegistro() == EstadoRegistroEnum.INACTIVO){
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.INACTIVA_COMPANIA,null);
        }
        SucursalEntity sucursalMapper = this.sucursalMapper.toSucursalEntity(sucursalRequestDto);
        sucursalMapper.setCreatedAt(LocalDate.now());
        sucursalMapper.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);
        sucursalMapper.setCompania(companiaEntity);

        SucursalEntity sucursalEntity = this.sucursalRepository.save(sucursalMapper);

        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, sucursalEntity);

    }

    @Override
    public ResponseGenerico listar() {
        List<SucursalEntity> sucursalEntityList = this.sucursalRepository.findAll();
        if (sucursalEntityList.isEmpty()) {
            return this.generadorRespuesta.noExiste();
        }
        List<SucursalDto> sucursalDtoList = this.sucursalDtoList(sucursalEntityList);
       return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.OPERACION_EXITO, sucursalDtoList);
    }
    private List<SucursalDto> sucursalDtoList(List<SucursalEntity> sucursalEntityList) {
        return  sucursalEntityList.stream()
                .map(sucursalEntity -> SucursalDto.builder()
                        .companiaDto(CompaniaDto.builder()
                                .idCompania(sucursalEntity.getCompania().getIdCompania())
                                .nombre(sucursalEntity.getCompania().getNombre())
                                .build())
                        .idSucursal(sucursalEntity.getIdSucursal())
                        .nombre(sucursalEntity.getNombre())
                        .responsable(sucursalEntity.getResponsable())
                        .direccion(sucursalEntity.getDireccion())
                        .telefono(sucursalEntity.getTelefono())
                        .email(sucursalEntity.getEmail())
                        .estadoRegistro(sucursalEntity.getEstadoRegistro())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public ResponseGenerico buscarPorId(Long id) {
        SucursalEntity sucursalEntity = this.buscarPorIdEntity(id);
        if (sucursalEntity == null) {
            return this.generadorRespuesta.noExiste();
        }
        SucursalDto sucursalDto = this.sucursalMapper.toSucursalDto(sucursalEntity);
        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, sucursalDto);
    }

    @Override
    public ResponseGenerico actualizar(Long id, SucursalRequestDto sucursalRequestDto) {
        try {

            if (!isIdValid(id, sucursalRequestDto.getIdSucursal())) {
                return this.generadorRespuesta.noCoincide();
            }
            SucursalEntity sucursalEntity = this.buscarPorIdEntity(id);
            if (sucursalEntity == null){
                return this.generadorRespuesta.noExiste();
            }
            if (!Objects.equals(sucursalEntity.getCompania().getIdCompania(), sucursalRequestDto.getIdCompania())){
                return this.generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST,EstadosEnum.ERROR,Constant.Message.NO_CAMBIAR,null);
            }

            sucursalEntity.toBuilder()
                    .nombre(sucursalRequestDto.getNombre())
                    .responsable(sucursalRequestDto.getResponsable())
                    .direccion(sucursalRequestDto.getDireccion())
                    .telefono(sucursalRequestDto.getTelefono())
                    .email(sucursalRequestDto.getEmail())
                    .estadoRegistro(sucursalRequestDto.getEstadoRegistro())
                    .updatedAt(LocalDate.now())
                    .build();

            SucursalEntity sucursalActualizada = this.sucursalRepository.save(sucursalEntity);
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, this.sucursalMapper.toSucursalDto(sucursalActualizada));

        } catch (Exception e) {
            log.error("Error al actualizar la sucursal con el ID: {} con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "sucursal"),null);
        }
    }

    private boolean isIdValid(Long id, Long sucursalId) {
        return Objects.equals(id, sucursalId);
    }

    @Override
    public ResponseGenerico eliminar(Long id) {
        try{
            if (this.buscarPorIdEntity(id) != null) {
                this.sucursalRepository.marcarComoEliminado(id,LocalDate.now(),EstadoRegistroEnum.INACTIVO);
                return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.OPERACION_EXITO,null);
            }
            return this.generadorRespuesta.noExiste();
        } catch (Exception e) {
            log.error("Error al eliminar la sucursal con el ID: {} con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "sucursal"),null);
        }
    }

    @Override
    public SucursalEntity buscarPorIdEntity(Long id) {
        return this.sucursalRepository.findById(id).orElse(null);
    }
}
