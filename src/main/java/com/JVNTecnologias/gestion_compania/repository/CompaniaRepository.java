package com.JVNTecnologias.gestion_compania.repository;

import com.JVNTecnologias.gestion_compania.Enum.EstadoRegistroEnum;
import com.JVNTecnologias.gestion_compania.entity.CompaniaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompaniaRepository extends JpaRepository<CompaniaEntity,Long> {

    @Query(value = "SELECT compania FROM CompaniaEntity compania")
    List<CompaniaEntity> findAllByAllEstadoRegistro();


    @Modifying
    @Transactional
    @Query("UPDATE CompaniaEntity c SET c.estadoRegistro = :estado, c.updatedAt = :fechaActualizacion WHERE c.idCompania = :id")
    void marcarComoEliminado(Long id, LocalDate fechaActualizacion, EstadoRegistroEnum estado);

    @Query("SELECT COUNT(s) = 0 FROM SucursalEntity s WHERE s.compania.idCompania = :idCompania AND s.estadoRegistro = :estado")
    boolean isCompaniaWithoutSucursales(@Param("idCompania") Long idCompania, EstadoRegistroEnum estado);
}
