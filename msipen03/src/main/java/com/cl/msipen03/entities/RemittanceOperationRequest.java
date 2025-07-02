package com.cl.msipen03.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RemittanceOperationRequest(

		@JsonProperty("Remittance_transaction_id")
		String RemittanceTransactionId,
		@JsonProperty("sct_transaction_id")
		String sctTransactionId,
		@JsonIgnore
		long totalCount

) {}