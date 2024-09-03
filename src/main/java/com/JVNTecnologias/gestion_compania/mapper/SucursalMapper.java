package com.JVNTecnologias.gestion_compania.mapper;

import com.JVNTecnologias.gestion_compania.dto.SucursalDto;
import com.JVNTecnologias.gestion_compania.dto.SucursalRequestDto;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = CompaniasMapper.class)
public interface SucursalMapper {
    SucursalMapper INSTANCE = Mappers.getMapper(SucursalMapper.class);

    SucursalEntity toSucursalEntity(SucursalRequestDto sucursalRequestDto);

    @Mapping(source = "compania", target = "companiaDto")
    @Mapping(source = "idSucursal", target = "idSucursal")
    SucursalDto toSucursalDto(SucursalEntity sucursalEntity);
}
