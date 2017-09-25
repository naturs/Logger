package com.github.naturs.logger.border;

/**
 * Thick border.
 */
public class ThickBorder implements LogBorder {

    private static final String TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════";
    private static final String LEFT_BORDER = "║";
    private static final String MIDDLE_BORDER = LEFT_BORDER + "---------------------------------------------------------------------------------------";

    private final boolean showMiddleBorder;

    public ThickBorder(boolean showMiddleBorder) {
        this.showMiddleBorder = showMiddleBorder;
    }

    @Override
    public String topBorder() {
        return TOP_BORDER;
    }

    @Override
    public String middleBorder() {
        return MIDDLE_BORDER;
    }

    @Override
    public String bottomBorder() {
        return BOTTOM_BORDER;
    }

    @Override
    public String leftBorder() {
        return LEFT_BORDER;
    }

    @Override
    public boolean showMiddleBorder() {
        return showMiddleBorder;
    }
}
