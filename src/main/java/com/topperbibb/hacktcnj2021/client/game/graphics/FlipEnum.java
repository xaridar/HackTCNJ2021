package com.topperbibb.hacktcnj2021.client.game.graphics;

/**
 * Determines axis that a sprite must be flipped by
 */
public enum FlipEnum {
    NONE, X, Y, BOTH;

    public FlipEnum flipped(FlipEnum flipType) {
        if (flipType == FlipEnum.NONE) {
            return this;
        } else if (this == FlipEnum.NONE) {
            return flipType;
        } else if (flipType == this) {
            return FlipEnum.NONE;
        } else if (flipType == FlipEnum.BOTH) {
            return this == FlipEnum.X ? FlipEnum.Y : FlipEnum.X;
        } else if (flipType == FlipEnum.X) {
            return this == FlipEnum.BOTH ? FlipEnum.Y : FlipEnum.BOTH;
        } else {
            return this == FlipEnum.BOTH ? FlipEnum.X : FlipEnum.BOTH;
        }
    }

    /**
     * used to determine whether sprite is flipped on the x axis
     * @return whether sprite is flipped on the x axis
     */
    public boolean flipX() {
        return this == X || this == BOTH;
    }

    /**
     * used to determine whether sprite is flipped on the y axis
     * @return whether sprite is flipped on the y axis
     */
    public boolean flipY() {
        return this == Y || this == BOTH;
    }
}
