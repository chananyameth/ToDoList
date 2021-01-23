package com.chananya.todolist;

import android.text.TextUtils;

import java.util.ArrayList;

public class ToDoList {
    public String m_title;
    public ArrayList<ToDoItem> m_items;
    public int m_background_color; // mapped to colors dictionary, for now
    public boolean m_expanded;

    @Override
    public String toString() { // only the items!
        return TextUtils.join(Consts.NEW_LINE, m_items);
    }

    public String toStringWithTitle() {
        return m_title + ":" + Consts.NEW_LINE + TextUtils.join(Consts.NEW_LINE, m_items);
    }
}
