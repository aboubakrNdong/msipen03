package com.cl.msipen03.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public record RemittanceOperationRequest(

		@JsonProperty("Operation_id")
		String OperationId,

		@JsonProperty("operation_status")
		String OperationStatus,

		@JsonIgnore
		long totalCount

) {}