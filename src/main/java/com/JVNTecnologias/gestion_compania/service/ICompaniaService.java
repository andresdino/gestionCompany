package com.JVNTecnologias.gestion_compania.service;

import com.JVNTecnologias.gestion_compania.dto.CompaniaRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;

public interface ICompaniaService {

    ResponseGenerico guardar(CompaniaRequestDto companiaRequestDto);
    ResponseGenerico listar();
    ResponseGenerico buscarPorId(Long id);
    ResponseGenerico actualizar(Long id,CompaniaRequestDto companiaRequestDto);
    ResponseGenerico eliminar(Long id);
    CompaniaEntity buscarPorIdCompania(Long id);
}
