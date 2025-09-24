package com.cl.apps.msipen03.entities;

import java.io.ByteArrayInputStream;

public record RemitanceCsvFile(ByteArrayInputStream contentOfCsvFile, Long idReper, String path) {
}