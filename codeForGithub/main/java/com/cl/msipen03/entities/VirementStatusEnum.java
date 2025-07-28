package com.cl.msipen03.entities;

public enum VirementStatusEnum {

    ACTC("ACCEPTED"),
    ACCP("ACCEPTED"),
    RJCT("REJECTED"),
    PART("PARTIAL");

    private final String label;

    VirementStatusEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String getStatusLabel(String code) {
        try {
            return valueOf(code).getLabel();
        } catch (IllegalArgumentException | NullPointerException e) {
            return code;
        }
    }
}