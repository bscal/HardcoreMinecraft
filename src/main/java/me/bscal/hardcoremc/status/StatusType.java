package me.bscal.hardcoremc.status;

public class StatusType {

    public final static StatusType BLEED = new StatusType(EStatusType.BLEED);

    public final String value;

    public StatusType(String type) {
        value = type;
    }

    public StatusType(EStatusType type) {
        value = type.toString();
    }
}