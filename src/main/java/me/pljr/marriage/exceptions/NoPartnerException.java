package me.pljr.marriage.exceptions;

import java.util.UUID;

public class NoPartnerException extends Exception {
    private final UUID source;

    public NoPartnerException(UUID source){
        super(source + " has no partner.");
        this.source = source;
    }

    public UUID getPlayerId() {
        return source;
    }
}
