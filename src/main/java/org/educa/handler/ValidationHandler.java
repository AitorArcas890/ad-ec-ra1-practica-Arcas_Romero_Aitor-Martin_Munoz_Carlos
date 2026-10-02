package org.educa.handler;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;

public class ValidationHandler implements ValidationEventHandler {
    @Override
    public boolean handleEvent(ValidationEvent validationEvent) {
        System.err.println("Evento de validacion (" + validationEvent.getSeverity() + ")"
                + "[linea: " + validationEvent.getLocator().getLineNumber() + "]"
                + "[columna: " + validationEvent.getLocator().getColumnNumber() + "]"
                + validationEvent.getMessage());
        return (validationEvent.getSeverity() != ValidationEvent.FATAL_ERROR);
    }
}
