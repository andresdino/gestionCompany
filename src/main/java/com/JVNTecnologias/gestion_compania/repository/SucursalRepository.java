package com.JVNTecnologias.gestion_compania.repository;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.entity.SucursalEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface SucursalRepository extends JpaRepository<SucursalEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE SucursalEntity s SET s.estadoRegistro = :estado, s.updatedAt = :fechaActualizacion WHERE s.idSucursal = :id")
    void marcarComoEliminado(Long id, LocalDate fechaActualizacion, EstadoRegistroEnum estado);
}
