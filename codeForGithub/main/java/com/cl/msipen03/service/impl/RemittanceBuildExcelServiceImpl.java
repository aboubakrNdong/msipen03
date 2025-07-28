package com.cl.msipen03.service.impl;

import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.msipen03.entities.RemittanceOperations;
import com.cl.msipen03.entities.RemittanceRequest;
import com.cl.msipen03.entities.RemittanceRequestPaginationResponse;
import com.cl.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.msipen03.service.RemittanceBuildExcelService;
import com.cl.msipen03.utils.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class RemittanceBuildExcelServiceImpl implements RemittanceBuildExcelService {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceBuildExcelServiceImpl.class).logger();

    private static final String[] TABLE_HEADERS = {
            "Prénom Nom bénéficiaire", "Compte bénéficiaire", "Banque bénéficiaire ",
             "Date de l'exécution ", "Motif du virement",
            "Référence du virement","Référence Bout en bout", "Montant du virement","Devise",
            "Référence Banque Opération", "Statut du virement", "Motif de rejet du virement"
    };

    @Override
    public ByteArrayInputStream generateExcelContent(RemittanceRequestPaginationResponse response) {
        try (var workbook = new XSSFWorkbook();
             var out = new ByteArrayOutputStream()) {

            LOGGER.info("Starting Generation of excel file");
            Sheet sheet = workbook.createSheet("Liste des virements");

            int currentRow = createSyntheseSection(sheet, response.request());
            createVirementsSection(sheet, currentRow, response);

            autoSizeColumns(sheet);
            workbook.write(out);

            LOGGER.info("Generation Excel file done");
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            LOGGER.error("Failed to generate Excel file", e);
            throw new InternalMSIPEN03Exception("Failed to generate Excel content", e);
        }
    }

    private int createSyntheseSection(Sheet sheet, RemittanceRequest request) {
        int rowNum = 0;

        rowNum = addSectionTitle(sheet, rowNum, "Synthèse d'opération");

        // Ajout des données dans le fichier
        Map<String, String> synthese = createSyntheseMap(request);
        rowNum = addKeyValuePairs(sheet, rowNum, synthese);

        return rowNum + 1; // ajout d'une ligne vide pour séparer les deux parties
    }

    private int createVirementsSection(Sheet sheet, int startRow,
                                       RemittanceRequestPaginationResponse response) {
        int rowNum = startRow;

        rowNum = addSectionTitle(sheet, rowNum, "Mes virements");

        rowNum = addTableHeaders(sheet, rowNum);

        return addOperationsData(sheet, rowNum, response.list());
    }

    private Map<String, String> createSyntheseMap(RemittanceRequest req) {
        Map<String, String> synthese = new LinkedHashMap<>();
        synthese.put("Id Reper", String.valueOf(req.id_reper()));
        synthese.put("Date et heure de saisie", Utils.transformsTimeStampIntoString(req.date_et_heure_de_saisie()));
        synthese.put("Numéro de la remise", req.numero_de_la_remise());
        synthese.put("Statut de la remise", req.statut_de_la_remise());
        synthese.put("Libellé compte DO", req.libelle_du_compte_DO());
        synthese.put("Numéro compte DO", String.valueOf(req.numero_compte_DO()));
        synthese.put("Nombre virements", String.valueOf(req.nombre_virements()));
        synthese.put("Date exécution remise", String.valueOf(req.date_execution_remise()));
        synthese.put("Montant total de la remise", String.valueOf(req.montant_total_de_la_remise()));
        return synthese;
    }

    private int addSectionTitle(Sheet sheet, int rowNum, String title) {
        Row titleRow = sheet.createRow(rowNum);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(title);
        return rowNum + 1;
    }

    private int addKeyValuePairs(Sheet sheet, int startRow, Map<String, String> data) {
        int rowNum = startRow;
        for (var entry : data.entrySet()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
        return rowNum;
    }

    private int addTableHeaders(Sheet sheet, int rowNum) {
        Row headerRow = sheet.createRow(rowNum);

        for (int i = 0; i < TABLE_HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(TABLE_HEADERS[i]);
        }

        return rowNum + 1;
    }

    private int addOperationsData(Sheet sheet, int startRow, Iterable<RemittanceOperations> operations) {
        int rowNum = startRow;
        for (RemittanceOperations op : operations) {
            Row row = sheet.createRow(rowNum++);
            addOperationRow(row, op);
        }
        return rowNum;
    }

    private void addOperationRow(Row row, RemittanceOperations op) {
        int col = 0;
        row.createCell(col++).setCellValue(op.prenom_nom_beneficiaire());
        row.createCell(col++).setCellValue(op.iban_beneficiaire());
        row.createCell(col++).setCellValue(op.bic_beneficiare());
        row.createCell(col++).setCellValue(String.valueOf(op.date_execution_virement()));
        row.createCell(col++).setCellValue(op.motif_virement());
        row.createCell(col++).setCellValue(op.reference_virement());
        row.createCell(col++).setCellValue(op.reference_Bout_en_bout());
        row.createCell(col++).setCellValue(String.valueOf(op.montant_virement()));
        row.createCell(col++).setCellValue(op.devise());
        row.createCell(col++).setCellValue(op.reference_banque_Operation());
        row.createCell(col++).setCellValue(op.statut_virement());
        row.createCell(col).setCellValue(op.motif_rejet());
    }

    private void autoSizeColumns(Sheet sheet) {
        int maxCol = Math.max(1, TABLE_HEADERS.length - 1);
        for (int i = 0; i <= maxCol; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}