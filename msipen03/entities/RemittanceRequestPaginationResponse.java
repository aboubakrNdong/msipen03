package com.cl.msipen03.entities;

import com.cl.msipen03.utils.FormatAmountTwoDecimals;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Builder
public record RemittanceRequestPaginationResponse(
        int size,
        int page,
        long total,

        Timestamp date_et_heure_de_saisie,

        String numero_de_la_remise,

        String statut_de_la_remise,

        String libelle_compte_DO,

        Integer numero_compte_DO,

        Integer nombre_virements,

        Timestamp date_execution_remise,

        @JsonSerialize(using = FormatAmountTwoDecimals.class)
        BigDecimal montant_total_de_la_remise,

        @JsonProperty("Remittance_operations")
        List<RemittanceOperation> list


) {
}