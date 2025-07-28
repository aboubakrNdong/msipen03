package com.cl.msipen03.entities;

import com.cl.msipen03.utils.FormatAmountTwoDecimals;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.sql.Date;

public record RemittanceOperations(

        String prenom_nom_beneficiaire,
        String iban_beneficiaire,
        String bic_beneficiare,
        Date date_execution_virement,
        String motif_virement,
        String reference_virement,
        String reference_Bout_en_bout,
        @JsonSerialize(using = FormatAmountTwoDecimals.class)
        BigDecimal montant_virement,
        String devise,
        String reference_banque_Operation,
        String statut_virement,
        String motif_rejet,

        @JsonIgnore
        long totalCount

) {
}