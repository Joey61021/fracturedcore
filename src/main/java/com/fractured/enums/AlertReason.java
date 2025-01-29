package com.fractured.enums;

import lombok.Getter;

public enum AlertReason {
    BLOCK_PLACE(Message.REGION_ALERT_BLOCK_PLACE),
    BLOCK_BREAK(Message.REGION_ALERT_BLOCK_BREAK),
    CHEST_OPEN(Message.REGION_ALERT_CHEST_OPEN);

    @Getter private final Message message;

    AlertReason(Message message) {
        this.message = message;
    }
}
