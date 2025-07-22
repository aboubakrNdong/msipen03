package com.cl.msipen03.facades;

import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import java.util.UUID;
import com.cl.msipen03.entities.RemittanceOperation;
import com.cl.msipen03.service.RemittanceStatusService;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RemittanceOperationsFacade {

    private final RemittanceStatusService remittanceStatusService;

    public RemittanceRequestPaginationResponse getOperationListResponse(String remittanceOrderId, int page, int size) {
        MDC.put("CORRELATION_ID", UUID.randomUUID().toString());

        Page<RemittanceOperation> remittanceOperations = remittanceStatusService
                .getRemittanceOperationResponse(remittanceOrderId, page, size);

        RemittanceRequest request = remittanceStatusService.getRemittanceRequestResponse(remittanceOrderId);

        return RemittanceRequestPaginationResponse.builder()
                .size(remittanceOperations.getSize())
                .page(remittanceOperations.getNumber())
                .total(remittanceOperations.getTotalElements())
                .date_et_heure_de_saisie(request.date_et_heure_de_saisie())
                .numero_de_la_remise(request.numero_de_la_remise())
                .statut_de_la_remise(request.statut_de_la_remise())
                .libelle_compte_DO(request.libelle_compte_DO())
                .numero_compte_DO(request.numero_compte_DO())
                .nombre_virements(request.nombre_virements())
                .date_execution_remise(request.date_execution_remise())
                .montant_total_de_la_remise(request.montant_total_de_la_remise())
                .list(remittanceOperations.getContent())
                .build();
    }
}