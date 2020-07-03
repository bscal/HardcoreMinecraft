package me.bscal.hardcoremc.status;

public enum StatusGroup {

    NONE("none"),
    DEFAULT("default"),
    BLEED("bleed"),
	FRACTURE("fracture");

    public final String key;

    StatusGroup(final String key) {
        this.key = key;
    }

    StatusGroup(final EStatusType type) {
        this.key = type.toString();
    }
}