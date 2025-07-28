package com.cl.msipen03.repository;

import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.mapper.RemittanceOperationsMapper;
import com.cl.msipen03.mapper.RemittanceRequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
        void testShouldReturnRemittanceRequest() {
                Date executionDate = Date.valueOf("2025-07-28");
                LocalDateTime creationDate = LocalDateTime.of(2024, 6, 19, 14, 3, 41);

                RemittanceRequest mockRequest = new RemittanceRequest(
                    123456,
                    Timestamp.from(creationDate.toInstant(ZoneOffset.UTC)),
                    "REM000000101000",
                    "PART",
                    "CONDAT SAS",
                    102481,
                    11,
                        executionDate,
                    new BigDecimal("100057.00")
            );

                when(jdbcTemplate.queryForObject(
                        anyString(),
                        any(RemittanceRequestMapper.class),
                        anyString()
                )).thenReturn(mockRequest);

                RemittanceRequest result = databaseRepository.findByRemittanceOrderId("REM000000101000");

                assertThat(result).isNotNull();
                assertThat(result.numero_de_la_remise()).isEqualTo("REM000000101000");
                assertThat(result.statut_de_la_remise()).isEqualTo("PART");
                assertThat(result.libelle_du_compte_DO()).isEqualTo("CONDAT SAS");
        }

        @Test
        void testShouldReturnPagedOperationsWhenDataExists() {
                Date executionDate = Date.valueOf("2025-07-28");
                List<RemittanceOperations> mockOperations = Arrays.asList(
                        new RemittanceOperations(
                                "LCL CREDIT LYONNAIS",
                                "30002003870000017010T61",
                                "CRLYFRPPXXX",
                                executionDate,
                                "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                                "PACIFICA2000157390",
                                "REF123",
                                new BigDecimal("500.00"),
                                "EUR",
                                "BNK123",
                                "REJECTED",
                                "Specific transaction/message amount is greater than allowed maximum",
                                2L
                        ),
                        new RemittanceOperations(
                                "LCL CREDIT LYONNAIS",
                                "30002095050000079135P57",
                                "CRLYFRPPXXX",
                                executionDate,
                                "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                                "PACIFICA2000157390",
                                "REF124",
                                new BigDecimal("500.00"),
                                "EUR",
                                "BNK124",
                                "REJECTED",
                                "Account number is invalid or missing.",
                                2L
                        )
                );

                when(jdbcTemplate.query(
                        anyString(),
                        any(RemittanceOperationsMapper.class),
                        anyString(),
                        anyInt(),
                        anyInt()
                )).thenReturn(mockOperations);

                Page<RemittanceOperations> result = databaseRepository.findByRemittanceOrderIdPagination(
                        "REM000000101000",
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
                        any(RemittanceOperationsMapper.class),
                        anyString(),
                        anyInt(),
                        anyInt()
                )).thenReturn(Collections.emptyList());

                Page<RemittanceOperations> result = databaseRepository.findByRemittanceOrderIdPagination(
                        "REM000000101000",
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
                Date executionDate = Date.valueOf("2025-07-28");
                List<RemittanceOperations> mockOperations = Arrays.asList(
                        new RemittanceOperations(
                                "LCL CREDIT LYONNAIS",
                                "30002003870000017010T61",
                                "CRLYFRPPXXX",
                                executionDate,
                                "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                                "PACIFICA2000157390",
                                "REF123",
                                new BigDecimal("500.00"),
                                "EUR",
                                "BNK123",
                                "REJECTED",
                                "Specific transaction/message amount is greater than allowed maximum",
                                2L
                        ),
                        new RemittanceOperations(
                                "LCL CREDIT LYONNAIS",
                                "30002095050000079135P57",
                                "CRLYFRPPXXX",
                                executionDate,
                                "0157390000153517262 REMONTEE COTISATIONS NEGATIVES PACIFICA",
                                "PACIFICA2000157390",
                                "REF124",
                                new BigDecimal("500.00"),
                                "EUR",
                                "BNK124",
                                "REJECTED",
                                "Account number is invalid or missing.",
                                2L
                        )
                );

                when(jdbcTemplate.query(
                        anyString(),
                        any(RemittanceOperationsMapper.class),
                        anyString(),
                        anyInt(),
                        anyInt()
                )).thenReturn(mockOperations);

                Page<RemittanceOperations> result = databaseRepository.findByRemittanceOrderIdPagination(
                        "REM000000101000",
                        1,
                        2
                );

                assertThat(result.getContent()).hasSize(2);
                assertThat(result.getTotalElements()).isEqualTo(4L);
                assertThat(result.getNumber()).isEqualTo(1);
                assertThat(result.getSize()).isEqualTo(2);
        }
}