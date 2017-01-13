package com.framgia.photoeditor.data.model;

public enum Effects {
    EFFECT_NODE(0),
    EFFECT_CONTRAST(1),
    EFFECT_CROSSPROCESS(2),
    EFFECT_DOCUMENTARY(3),
    EFFECT_GRAYSCALE(4),
    EFFECT_FILLLIGHT(5),
    EFFECT_LOMOISH(6),
    EFFECT_SEPIA(7),
    EFFECT_SHARPEN(8),
    EFFECT_TEMPERATURE(9);
    private int mValue;

    Effects(int value) {
        mValue = value;
    }
}
