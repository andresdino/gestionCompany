package com.JVNTecnologias.gestion_compania.service.Impl;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoDto;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import com.JVNTecnologias.gestion_compania.mapper.EmpleadoMapper;
import com.JVNTecnologias.gestion_compania.repository.EmpleadoRepository;
import com.JVNTecnologias.gestion_compania.service.IEmpleadoService;
import com.JVNTecnologias.gestion_compania.service.ISucursalService;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EmpleadoService implements IEmpleadoService {

    private final GeneradorRespuesta generadorRespuesta;
    EmpleadoRepository empleadoRepository;
    ISucursalService iSucursalService;
    private final EmpleadoMapper empleadoMapper = EmpleadoMapper.INSTANCE;

    @Override
    public ResponseGenerico guardar(EmpleadoRequestDto empleadoRequestDto) {
        SucursalEntity sucursalEntity = this.iSucursalService.buscarPorIdEntity(empleadoRequestDto.getIdSucursal());
        if (sucursalEntity == null) {
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.WARNING,"La sucursal para crear el empleado no existe", null);
        }
        if (sucursalEntity.getEstadoRegistro().equals(EstadoRegistroEnum.INACTIVO)){
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.WARNING,"La sucursal se encuentra en estado inactiva", null);
        }
        EmpleadosEntity empleadosEntity = this.empleadoMapper.toEmpleadosEntity(empleadoRequestDto);
        empleadosEntity.setCreatedAp(LocalDate.now());
        empleadosEntity.setSucursal(sucursalEntity);
        empleadosEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

       EmpleadosEntity empleadoRegistrado = this.empleadoRepository.save(empleadosEntity);

        EmpleadoDto empleadoDto = this.empleadoMapper.toEmpleadoDto(empleadoRegistrado);

        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDto);
    }

    @Override
    public ResponseGenerico listar() {
        List<EmpleadosEntity> empleadosEntities = this.empleadoRepository.findAll();
        if (empleadosEntities.isEmpty()) {
            return this.generadorRespuesta.noExiste();
        }
        List<EmpleadoDto> empleadoDtoList = this.empleadoMapper.toEmpleadoDtos(empleadosEntities);
        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDtoList);

    }

    @Override
    public ResponseGenerico buscarPorId(Long id) {
        EmpleadosEntity empleadosEntity = this.buscarPorIdEntity(id);
        if (empleadosEntity == null) {
            return this.generadorRespuesta.noExiste();
        }
        EmpleadoDto empleadoDto = this.empleadoMapper.toEmpleadoDto(empleadosEntity);
        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDto);
    }

    @Override
    public ResponseGenerico actualizar(Long id, EmpleadoRequestDto empleadoRequestDto) {
        try {
            if (!isIdValid(id, empleadoRequestDto.getIdEmpleado())){
                return this.generadorRespuesta.noCoincide();
            }
            EmpleadosEntity empleadosEntity = this.buscarPorIdEntity(id);
            if (empleadosEntity == null) {
                return this.generadorRespuesta.noExiste();
            }

            SucursalEntity sucursalEntity = this.iSucursalService.buscarPorIdEntity(empleadoRequestDto.getIdSucursal());
            if (sucursalEntity == null) {
                return this.generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, "La nueva sucursal a asignar no existe", null);
            }
            if (!sucursalEntity.getEstadoRegistro().equals(EstadoRegistroEnum.ACTIVO)){
                return this.generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST, EstadosEnum.ERROR, "La sucursal asiganda no esta activa", null);
            }

            EmpleadosEntity empleadoBuilder = empleadosEntity.toBuilder()
                    .nombre(empleadoRequestDto.getNombre())
                    .apellido(empleadoRequestDto.getApellido())
                    .email(empleadoRequestDto.getEmail())
                    .telefono(empleadoRequestDto.getTelefono())
                    .estadoRegistro(EstadoRegistroEnum.ACTIVO)
                    .updatedAt(LocalDate.now())
                    .build();
            EmpleadosEntity empledoActualizar = this.empleadoRepository.save(empleadoBuilder);
            EmpleadoDto empleadoDto = this.empleadoMapper.toEmpleadoDto(empledoActualizar);
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK, EstadosEnum.SUCCESS, Constant.Message.OPERACION_EXITO, empleadoDto);
        } catch (Exception e) {
            log.error("Error al actualizar el empleado con el ID: {} con error: {}",id, e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR, EstadosEnum.ERROR, Constant.Message.ERROR_CONSULTADO.replace("%s","empleado"), null);

        }
    }
    private boolean isIdValid(Long id, Long idEmpleado) {
        return id.equals(idEmpleado);
    }

    @Override
    public ResponseGenerico eliminar(Long id) {
        try{
            if (this.buscarPorIdEntity(id) != null) {
                this.empleadoRepository.marcarComoEliminado(id,LocalDate.now(),EstadoRegistroEnum.INACTIVO);
                return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.OPERACION_EXITO,null);
            }
            return this.generadorRespuesta.noExiste();
        } catch (Exception e) {
            log.error("Error al eliminar el empleado con el ID: {} con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "empleado"),null);
        }
    }

    @Override
    public EmpleadosEntity buscarPorIdEntity(Long id) {
        return this.empleadoRepository.findById(id).orElse(null);
    }
}
