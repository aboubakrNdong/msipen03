package com.cl.msipen03.service;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.repository.DatabaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class RemittanceOperationsStatusServiceTest {

    @Mock
    private DatabaseRepository databaseRepository;

    @InjectMocks
    private RemittanceOperationsStatusService service;

    private List<RemittanceOperationRequest> mockOperations;
    private Page<RemittanceOperationRequest> mockPage;

    @BeforeEach
    void setUp() {
        mockOperations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 2L)
        );
        mockPage = new PageImpl<>(mockOperations);
    }

    @Test
    void testShouldReturnRemittanceOperations() {

        when(databaseRepository.listRemittanceOperation(anyString(), anyInt(), anyInt()))
                .thenReturn(mockPage);

        Page<RemittanceOperationRequest> result = service.getRemittanceOperationResponse("IPF000000127670", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).OperationId()).isEqualTo("IPF000000127670");
        assertThat(result.getContent().get(0).OperationStatus()).isEqualTo("PART");
    }

    @Test
    void testShouldReturnEmptyPage() {

        Page<RemittanceOperationRequest> emptyPage = new PageImpl<>(List.of());
        when(databaseRepository.listRemittanceOperation(anyString(), anyInt(), anyInt()))
                .thenReturn(emptyPage);

        Page<RemittanceOperationRequest> result = service.getRemittanceOperationResponse("IPF000000127670", 0, 10);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }
}