package com.tw.kiera.patu;

public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;

    public static final ValidationResult VALID = new ValidationResult(true, null);

    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isValid() {
        return this.valid;
    }

    public boolean isInvalid() {
        return !this.valid;
    }
}
