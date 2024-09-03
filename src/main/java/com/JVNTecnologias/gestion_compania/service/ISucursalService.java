package com.JVNTecnologias.gestion_compania.service;

import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.dto.SucursalRequestDto;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;

public interface ISucursalService {
    ResponseGenerico guardar(SucursalRequestDto sucursalRequestDto);
    ResponseGenerico listar();
    ResponseGenerico buscarPorId(Long id);
    ResponseGenerico actualizar(Long id,SucursalRequestDto sucursalRequestDto);
    ResponseGenerico eliminar(Long id);
    SucursalEntity buscarPorIdEntity(Long id);
}
