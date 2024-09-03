package com.JVNTecnologias.gestion_compania.service.Impl;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.Enum.EstadosEnum;
import com.JVNTecnologias.gestion_compania.constant.Constant;
import com.JVNTecnologias.gestion_compania.dto.CompaniaRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import com.JVNTecnologias.gestion_compania.mapper.CompaniasMapper;
import com.JVNTecnologias.gestion_compania.repository.CompaniaRepository;
import com.JVNTecnologias.gestion_compania.service.ICompaniaService;
import com.JVNTecnologias.gestion_compania.utils.GeneradorRespuesta;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class CompaniaImplService implements ICompaniaService {



    private final GeneradorRespuesta generadorRespuesta;
    CompaniaRepository companiaRepository;
    private final CompaniasMapper companiaMapper = CompaniasMapper.INSTANCE;

    @Override
    @Transactional
    public ResponseGenerico guardar(CompaniaRequestDto companiaRequestDto) {

        CompaniaEntity companiaMapperEntity = companiaMapper.toEntity(companiaRequestDto);
        companiaMapperEntity.setCreatedAt(LocalDate.now());
        companiaMapperEntity.setEstadoRegistro(EstadoRegistroEnum.ACTIVO);

        CompaniaEntity saveCompania = this.companiaRepository.save(companiaMapperEntity);

        if (saveCompania.getIdCompania() == null) {
            return this.generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST,EstadosEnum.ERROR,Constant.Message.ERROR_CREANDO,Constant.Message.ERROR_CREANDO);
        }
        return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.COMPANIA_GUARDADA,saveCompania);
    }

    @Override
    public ResponseGenerico listar() {
        try{
            List<CompaniaEntity> listaCompanias = this.companiaRepository.findAllByAllEstadoRegistro();
            if (listaCompanias.isEmpty()){
                return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.NO_DATA,null);
            }
            List<CompaniaRequestDto> listaCompaniasMapper = this.companiaMapper.toListDto(listaCompanias);
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.CONSULTA_EXITOSA,listaCompaniasMapper);
        } catch (Exception e) {
            log.error("Error al listar las compañías: {}", e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"),null);
        }
    }

    @Override
    public ResponseGenerico buscarPorId(Long id) {
        try{
            CompaniaEntity compania = this.buscarPorIdCompania(id);
            if (compania == null){
                return this.generadorRespuesta.noExiste();
            }
            CompaniaRequestDto companiaRequestDto = this.companiaMapper.toDto(compania);
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.CONSULTA_EXITOSA,companiaRequestDto);

        } catch (Exception e) {
            log.error("Error al buscar la compañia con el ID: {}con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"),null);
        }
    }

    @Override
    @Transactional
    public ResponseGenerico actualizar(Long id,CompaniaRequestDto companiaRequestDto) {
        try{
            if (!Objects.equals(id, companiaRequestDto.getIdCompania())){
                return this.generadorRespuesta.noCoincide();
            }
            CompaniaEntity compania = this.buscarPorIdCompania(id);
            if (compania == null){
                return this.generadorRespuesta.noExiste();
            }
            CompaniaEntity actualizarCompania = compania.toBuilder()
                    .nombre(companiaRequestDto.getNombre())
                    .nit(companiaRequestDto.getNit())
                    .propietario(companiaRequestDto.getPropietario())
                    .direccion(companiaRequestDto.getDireccion())
                    .telefono(companiaRequestDto.getTelefono())
                    .email(companiaRequestDto.getEmail())
                    .estadoRegistro(companiaRequestDto.getEstadoRegistro())
                    .build();
            CompaniaEntity companiaActualizada = this.companiaRepository.save(actualizarCompania);
            return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.COMPANIA_ACTUALIZADA,this.companiaMapper.toDto(companiaActualizada));

        } catch (Exception e) {
            log.error("Error al actualizar la compañia con el ID: {} con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"),null);
        }
    }

    @Override
    public CompaniaEntity buscarPorIdCompania(Long id){
        return this.companiaRepository.findById(id).orElse(null);
    }

    @Override
    public ResponseGenerico eliminar(Long id) {
        try{
            boolean sucursalActiva = this.companiaRepository.isCompaniaWithoutSucursales(id, EstadoRegistroEnum.INACTIVO);
            if (!sucursalActiva){
                return this.generadorRespuesta.generarRespuesta(HttpStatus.BAD_REQUEST,EstadosEnum.ERROR,Constant.Message.SUCURSAL_ASIGNADA,null);
            }
            if (this.buscarPorIdCompania(id) != null && sucursalActiva){
                this.companiaRepository.marcarComoEliminado(id, LocalDate.now(),EstadoRegistroEnum.INACTIVO);
                return this.generadorRespuesta.generarRespuesta(HttpStatus.OK,EstadosEnum.SUCCESS,Constant.Message.OPERACION_EXITO,null);
            }
            return this.generadorRespuesta.noExiste();

        } catch (Exception e) {
            log.error("Error al eliminar la compañia con el ID: {} con error : {}", id ,e.getMessage());
            return this.generadorRespuesta.generarRespuesta(HttpStatus.INTERNAL_SERVER_ERROR,EstadosEnum.ERROR,Constant.Message.ERROR_CONSULTADO.replace("%s", "compañia"),null);
        }
    }
}
