package com.chananya.todolist;

public class Consts{
    // General
    public static final String SharedPreferencesName = "data";

    //Shared Preferences keys
    public static final String KEY_ALL_LISTS = "all lists";
    public static final String KEY_TUTORIAL = "tutorial";
    public static final String KEY_LINES_COUNT = "lines count";
    public static final String KEY_CHARACTER_LIMIT = "character limit";

    // ToDoList keys
    public static final String ITEM_NUMBER = "item No.";
    public static final String KEY_TITLE = "title";
    public static final String KEY_LIST = "list";
    public static final String KEY_TEXT = "text";
    public static final String KEY_DONE = "done";
    public static final String KEY_COLOR = "color";
    public static final String KEY_EXPAND = "expand";

    // Strings
    public static final String NEW_LINE = "\n";
    public static final String EMPTY_STRING = "";
    public static final String V = "✓ ";
    public static final String X = "× ";
    public static final String SO_ON = "...";

    // Invalid values
    public static final int INVALID_LINES_COUNT = -1;
    public static final int INVALID_CHARACTER_LIMIT = -1;
    public static final int NO_ITEM = -1;

    // Default values
    public static final int DEFAULT_LINES_COUNT = 5;
    public static final int DEFAULT_CHARACTER_LIMIT = 100;
}
