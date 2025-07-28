package com.cl.msipen03.entities;

import java.io.ByteArrayInputStream;

public record ExcelGenerationEntity(ByteArrayInputStream contentOfExcelFile, Integer idReper) {}