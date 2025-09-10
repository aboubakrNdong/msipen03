package com.cl.apps.msipen03.service.impl;

import com.cl.apps.msipen03.utils.Utils;
import com.cl.logs.commun.CommonLoggerFactory;
import com.cl.apps.msipen03.entities.RemittanceOperation;
import com.cl.apps.msipen03.entities.RemittanceOrder;
import com.cl.apps.msipen03.entities.RemittanceOrderPaginationResponse;
import com.cl.apps.msipen03.exceptions.InternalMSIPEN03Exception;
import com.cl.apps.msipen03.service.RemittanceCsvFileService;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.cl.apps.msipen03.utils.Utils.*;

@Service
public class RemittanceCsvFileServiceImpl implements RemittanceCsvFileService {

    private static final Logger LOGGER = CommonLoggerFactory.getLogger(RemittanceCsvFileServiceImpl.class).logger();
    private static final String CSV_SEPARATOR = ";";

    private static final String[] TABLE_HEADERS = {
            "Nom beneficiaire", "Iban beneficiaire", "Bic beneficiaire ", "Montant", "Devise", "Motif du paiement",
            "Reference du paiement", "Reference de Bout en bout", "Reference Banque", "Statut",
            "Code rejet ISO", "Motif de rejet"
    };

    @Override
    public ByteArrayInputStream generateCsvFileContent(RemittanceOrderPaginationResponse response) {
        try (var out = new ByteArrayOutputStream();
             var writer = new PrintWriter(out, true, StandardCharsets.UTF_8)) {

            LOGGER.info("Starting Generation of CSV file");

            writeSyntheseSection(writer, response.request());
            writer.println(); // Empty line between sections
            writeVirementsSection(writer, response);

            LOGGER.info("Generation CSV file done");
            return new ByteArrayInputStream(out.toByteArray());

        } catch (Exception e) {
            LOGGER.error("Failed to generate CSV file", e);
            throw new InternalMSIPEN03Exception("Failed to generate CSV content", e);
        }
    }

    private void writeSyntheseSection(PrintWriter writer, RemittanceOrder req) {
        writer.println("COMPTE RENDU DE TRAITEMENT DE LA REMISE");

        Map<String, Object> synthese = new LinkedHashMap<>();
        synthese.put("Reference Banque de la remise", req.getReferenceBanqueDeLaRemise());
        synthese.put("Reference du message d'origine", req.getReferenceDuMessageDorigine());
        synthese.put("Reference de la remise d'origine", req.getReferenceDeLaRemiseDorigine());
        synthese.put("Statut de la remise", req.getStatutDeLaRemise());
        synthese.put("Libelle du compte donneur d'ordre", req.getLibelleDuCompteDO());
        synthese.put("Compte donneur d'ordre", req.getCompteDO());
        synthese.put("Nombre de virements", req.getNombreVirements());
        synthese.put("Date d'execution", Utils.checkDateToAvoidNPE(req.getDateExecution(), DATE_FORMATTER));
        synthese.put("Montant total de la remise", req.getMontantTotalDeLaRemise());

        synthese.forEach((key, value) -> writer.println(key + CSV_SEPARATOR + value));
    }

    private void writeVirementsSection(PrintWriter writer, RemittanceOrderPaginationResponse response) {
        writer.println("DETAIL DES OPERATIONS DE LA REMISE");

        // Write headers
        writer.println(String.join(CSV_SEPARATOR, TABLE_HEADERS));

        // Write data
        response.list().forEach(op -> writer.println(formatOperationLine(op)));
    }

    private String formatOperationLine(RemittanceOperation op) {
        return String.join(CSV_SEPARATOR,
                escapeCsvField(op.getNomBeneficiaire()),
                escapeCsvField(op.getIbanBeneficiaire()),
                escapeCsvField(op.getBicBeneficare()),
                escapeCsvField(Utils.checkStringValueToAvoidNPE(op.getMontant())),
                escapeCsvField(op.getDevise()),
                escapeCsvField(op.getMotifPaiement()),
                escapeCsvField(op.getReferencePaiement()),
                escapeCsvField(op.getReferenceBoutEnBout()),
                escapeCsvField(op.getReferenceBanque()),
                escapeCsvField(op.getStatut()),
                escapeCsvField(op.getCodeRejetIso()),
                escapeCsvField(op.getMotifRejet())
        );
    }

    private String escapeCsvField(String field) {
        if (field == null) return "";
        return "\"" + field.replace("\"", "\"\"") + "\"";
    }

}