package com.chananya.todolist;

import java.util.Comparator;
import java.util.HashMap;

public class Consts {
    // General
    public static final String SharedPreferencesName = "data";
    public static HashMap<Integer, Integer> colors;
    public static Comparator<ToDoItem> itemsComparator;

    static {
        colors = new HashMap<>();
        colors.put(0, 0xFFFFFFFF);
        colors.put(1, 0xFFF44336);
        colors.put(2, 0xFFBA68C8);
        colors.put(3, 0xFF7986CB);
        colors.put(4, 0xFF42A5F5);
        colors.put(5, 0xFF4CAF50);
        colors.put(6, 0xFFFFEE58);
        colors.put(7, 0xFFFFCA28);
        colors.put(8, 0xFFFF9800);
        colors.put(9, 0xFFA1887F);
        colors.put(10, 0xFF78909C);

        itemsComparator = new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem item1, ToDoItem item2) {
                if (item1.m_is_checked == item2.m_is_checked) {
                    return 0;
                } else if (item1.m_is_checked) { // send item1 to bottom
                    return 1;
                } else { // send item2 to bottom
                    return -1;
                }
            }
        };
    }

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
    public static final String EMPTY_LIST_STRING = "[]";
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
