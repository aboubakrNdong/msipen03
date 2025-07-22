package com.cl.msipen03.entities;

import com.cl.msipen03.utils.FormatAmountTwoDecimals;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record RemittanceOperation(

        String nom_beneficiaire,
        String compte_emetteur,
        String bic_beneficiare,
        String iban_beneficiaire,
        Timestamp date_execution_virement,
        String motif_virement,
        String reference_virement,
        @JsonSerialize(using = FormatAmountTwoDecimals.class)
        BigDecimal montant_virement, //TODO: mettre en forme avec deux décimales //DONE
        String statut_virement, //TODO : faire la transfomation de RJCT à rejecter, et ACPT à accepté  //DONE
        String motif_rejet, //TODO: si c'est accepted pas besoin d'afficher motif rejet

        @JsonIgnore
        long totalCount

) {
}