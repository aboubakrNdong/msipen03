package com.cl.msipen03.entities;

import com.cl.msipen03.utils.FormatAmountTwoDecimals;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.sql.Timestamp;

public record RemittanceRequest(

//TODO: pourquoi RemittanceRequest and RemittanceRequestPaginationResponse contiennent
// presque les meme champs ?
		Timestamp date_et_heure_de_saisie,
		String numero_de_la_remise,
		String statut_de_la_remise,
		String libelle_compte_DO,
		Integer numero_compte_DO,
		Integer nombre_virements,
		Timestamp date_execution_remise,
		@JsonSerialize(using = FormatAmountTwoDecimals.class)
		BigDecimal montant_total_de_la_remise

) {}