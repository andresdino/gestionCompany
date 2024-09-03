package com.JVNTecnologias.gestion_compania.mapper;

import com.JVNTecnologias.gestion_compania.dto.EmpleadoDto;
import com.JVNTecnologias.gestion_compania.dto.EmpleadoRequestDto;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = SucursalMapper.class)
public interface EmpleadoMapper {

    EmpleadoMapper INSTANCE = Mappers.getMapper(EmpleadoMapper.class);
    EmpleadosEntity toEmpleadosEntity(EmpleadoRequestDto empleadoRequestDto);

    @Mapping(source = "idEmpleado", target = "idEmpleado")
    @Mapping(source = "sucursal", target = "sucursal")
    EmpleadoDto toEmpleadoDto(EmpleadosEntity empleadosEntity);

    @Mapping(source = "idEmpleado", target = "idEmpleado")
    @Mapping(source = "sucursal", target = "sucursal")
    List<EmpleadoDto> toEmpleadoDtos(List<EmpleadosEntity> empleadosEntities);

}
