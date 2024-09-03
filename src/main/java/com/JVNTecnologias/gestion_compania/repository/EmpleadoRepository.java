package com.JVNTecnologias.gestion_compania.repository;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.entity.EmpleadosEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface EmpleadoRepository extends JpaRepository<EmpleadosEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE EmpleadosEntity s SET s.estadoRegistro = :estado, s.updatedAt = :fechaActualizacion WHERE s.idEmpleado = :id")
    void marcarComoEliminado(Long id, LocalDate fechaActualizacion, EstadoRegistroEnum estado);
}
