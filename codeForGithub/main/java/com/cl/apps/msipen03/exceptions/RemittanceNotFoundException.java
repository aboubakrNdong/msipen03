package com.cl.apps.msipen03.exceptions;

public class RemittanceNotFoundException extends InternalMSIPEN03Exception {
    public RemittanceNotFoundException(String referenceComete) {
        super("ReferenceComete not found for ID: " + referenceComete);
    }
}
