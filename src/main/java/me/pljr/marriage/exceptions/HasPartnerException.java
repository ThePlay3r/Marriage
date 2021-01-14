package me.pljr.marriage.exceptions;

import java.util.UUID;

public class HasPartnerException extends Exception {
    private final UUID source;

    public HasPartnerException(UUID source){
        super(source + " has a partner.");
        this.source = source;
    }

    public UUID getSource() {
        return source;
    }
}
