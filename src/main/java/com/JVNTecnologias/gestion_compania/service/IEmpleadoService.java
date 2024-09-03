package com.JVNTecnologias.gestion_compania.service;

import com.JVNTecnologias.gestion_compania.dto.EmpleadoRequestDto;
import com.JVNTecnologias.gestion_compania.dto.ResponseGenerico;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import lombok.extern.java.Log;

public interface IEmpleadoService {
    ResponseGenerico guardar(EmpleadoRequestDto empleadoRequestDto);
    ResponseGenerico listar();
    ResponseGenerico buscarPorId(Long id);
    ResponseGenerico actualizar(Long id,EmpleadoRequestDto empleadoRequestDto);
    ResponseGenerico eliminar(Long id);
    EmpleadosEntity buscarPorIdEntity(Long id);
}
