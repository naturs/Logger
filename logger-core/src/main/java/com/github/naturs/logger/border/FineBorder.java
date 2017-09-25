package com.github.naturs.logger.border;

/**
 * fine border.
 */
public class FineBorder implements LogBorder {

    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final String LEFT_BORDER = "│";
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

    private final boolean showMiddleBorder;

    public FineBorder(boolean showMiddleBorder) {
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
