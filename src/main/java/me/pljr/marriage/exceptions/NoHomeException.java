package me.pljr.marriage.exceptions;

import java.util.UUID;

public class NoHomeException extends Exception {
    private final UUID source;

    public NoHomeException(UUID source){
        super(source + " does not have a home.");
        this.source = source;
    }

    public UUID getSource() {
        return source;
    }
}
