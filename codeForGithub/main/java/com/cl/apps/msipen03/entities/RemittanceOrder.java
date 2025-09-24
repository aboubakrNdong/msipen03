package com.cl.apps.msipen03.entities;

import com.cl.apps.msipen03.utils.FormatAmountTwoDecimals;
import com.cl.apps.msipen03.utils.Utils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.sql.Date;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "ordre")
@Getter
@Setter
public class RemittanceOrder {
    @Id // Fake ID - Just so Hibernate doesn't throw an error
    @Column(name = "id_ordpmt_cl")
    private String referenceBanqueDeLaRemise;

    @Column(name = "libidemsg")
    private String referenceDuMessageDorigine;

    @Column(name = "id_clihrs")
    private String referenceDeLaRemiseDorigine;

    @Column(name = "numtecprs")
    private Long idReper;

    @Convert(converter = Utils.StatusAttributeConverter.class)
    @Column(name = "codetagrpord_psr2")
    private String statutDeLaRemise;

    @Column(name = "libnomdbt")
    private String libelleDuCompteDO;

    @Column(name = "iban_dbt")
    private String compteDO;

    @Column(name = "nbrtrnlot")
    private Integer nombreVirements;

    @Column(name = "timreaordvir")
    private Date dateExecution;

    @Column(name = "mntlot")
    @JsonSerialize(using = FormatAmountTwoDecimals.class)
    private BigDecimal montantTotalDeLaRemise;
}