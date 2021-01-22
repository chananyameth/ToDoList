package com.chananya.todolist;

import java.util.ArrayList;

public class ToDoList {
    public String m_title;
    public int m_background_color; // mapped to colors dictionary, for now
    public boolean m_expanded;
    public ArrayList<ToDoItem> m_items;
}
