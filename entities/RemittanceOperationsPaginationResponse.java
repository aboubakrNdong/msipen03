package com.cl.msipen03.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record RemittanceOperationsPaginationResponse(
		int size,
		int page,
		long total,
		@JsonProperty("Remittance_operations")
		List<RemittanceOperationRequest> list
) {}