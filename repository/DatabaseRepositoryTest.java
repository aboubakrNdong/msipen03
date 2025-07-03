package com.cl.msipen03.repository;

import com.cl.msipen03.entities.RemittanceOperationRequest;
import com.cl.msipen03.mapper.RemittanceOperationsListMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class DatabaseRepositoryTest {

    @Mock
    private DataSource dataSource;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private DatabaseRepository databaseRepository;

    @BeforeEach
    void setUp() {
        databaseRepository = new DatabaseRepository(dataSource);
        ReflectionTestUtils.setField(databaseRepository, "jdbcTemplate", jdbcTemplate);
    }

    @Test
    void testShouldReturnPagedResultsWhenDataExists() {
        List<RemittanceOperationRequest> mockOperations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 2L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 2L)
        );

        when(jdbcTemplate.query(
                anyString(),
                any(RemittanceOperationsListMapper.class),
                anyString(),
                anyInt(),
                anyInt()
        )).thenReturn(mockOperations);


        Page<RemittanceOperationRequest> result = databaseRepository.listRemittanceOperation(
                "IPF000000127670",
                0,
                10
        );

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2L);
        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    void testShouldReturnEmptyPageWhenNoDataExists() {
        when(jdbcTemplate.query(
                anyString(),
                any(RemittanceOperationsListMapper.class),
                anyString(),
                anyInt(),
                anyInt()
        )).thenReturn(Collections.emptyList());


        Page<RemittanceOperationRequest> result = databaseRepository.listRemittanceOperation(
                "IPF000000127670",
                0,
                10
        );

        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getNumber()).isZero();
        assertThat(result.getSize()).isEqualTo(10);
    }

    @Test
    void testShouldHandlePagination() {
        List<RemittanceOperationRequest> mockOperations = Arrays.asList(
                new RemittanceOperationRequest("IPF000000127670", "PART", 5L),
                new RemittanceOperationRequest("IPF000000127668", "PART", 5L)
        );

        when(jdbcTemplate.query(
                anyString(),
                any(RemittanceOperationsListMapper.class),
                anyString(),
                anyInt(),
                anyInt()
        )).thenReturn(mockOperations);


        Page<RemittanceOperationRequest> result = databaseRepository.listRemittanceOperation(
                "IPF000000127670",
                1,
                2
        );

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(5L);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(2);
    }
}