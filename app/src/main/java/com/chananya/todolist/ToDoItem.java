package com.chananya.todolist;

public class ToDoItem {
    public String m_content;
    public boolean m_is_checked;

    @Override
    public String toString() {
        if (m_is_checked) {
            return Consts.V + m_content;
        } else {
            return Consts.X + m_content;
        }
    }
}
