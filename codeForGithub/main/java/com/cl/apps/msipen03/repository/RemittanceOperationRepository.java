package com.cl.apps.msipen03.repository;

import com.cl.apps.msipen03.entities.RemittanceOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

public interface RemittanceOperationRepository extends JpaRepository<RemittanceOperation, Long> {


    @Query(nativeQuery = true,
            value = "select * from virinst1a.fct_feedback_masse_restitution_statut_niveau_operation(:referenceComete, :page, :size)")
    Page<RemittanceOperation> findOperationsByReferenceComete(
            @Param("referenceComete") String referenceComete,
            @Param("page") int page,
            @Param("size") int size,
            Pageable pageable);
}