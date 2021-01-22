package com.chananya.todolist;

import android.os.*;
import android.view.*;
import android.content.*;

import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;

import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.ClipData;
import android.content.ClipboardManager;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private SharedPreferences sharedPreferences;

    private int characterLimit = 0;
    private int linesCount = 0;
    private int selectedListPosition = 0;

    private ArrayList<ToDoList> all_lists = new ArrayList<>();
    private ArrayList<String> menuSpinnerOptions = new ArrayList<>();
    private Spinner menuSpinner;
    private ListView todoLists;


    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.main);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        context = MainActivity.this;

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        Button settings_b = findViewById(R.id.settings_b);
        Button about_b = findViewById(R.id.about_b);
        menuSpinner = findViewById(R.id.spinner);
        todoLists = findViewById(R.id.listview1);

        sharedPreferences = getSharedPreferences(Consts.SharedPreferencesName, Activity.MODE_PRIVATE);

        settings_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        about_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder about_dialog = new AlertDialog.Builder(context);
                about_dialog.setTitle(R.string.about_title);
                about_dialog.setMessage(R.string.about_content);
                about_dialog.setNegativeButton(R.string.ok, null);
                about_dialog.create().show();
            }
        });

        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _param1, View _param2, int selected_menu_item_position, long _param4) {
                if ((selectedListPosition == -1) || (selected_menu_item_position == (menuSpinnerOptions.size() - 1))) {
                    return;
                }

                ToDoList todo_list = all_lists.get(selectedListPosition);
                StringBuilder list_preview = new StringBuilder(todo_list.m_title + ":" + Consts.NEW_LINE);
                for (ToDoItem item : todo_list.m_items) {
                    if (item.m_is_checked) {
                        list_preview.append(Consts.V);
                    } else {
                        list_preview.append(Consts.X);
                    }
                    list_preview.append(item.m_content).append(Consts.NEW_LINE);
                }
                if (list_preview.length() > 0) {
                    list_preview = list_preview.deleteCharAt(list_preview.length() - 1); // remove last new line
                }

                if (selected_menu_item_position == 0) { // copy
                    ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", list_preview.toString()));
                    SketchwareUtil.showMessage(context, getString(R.string.copied_to_clipboard));
                    resetSpinner();
                } else if (selected_menu_item_position == 1) { // share
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My list");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, list_preview.toString());
                    startActivity(intent);
                    resetSpinner();
                } else if (selected_menu_item_position == 2) { // delete
                    AlertDialog.Builder d = new AlertDialog.Builder(context);
                    d.setTitle(R.string.delete_title);
                    d.setMessage(all_lists.get(selectedListPosition).m_title);
                    d.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            all_lists.remove(selectedListPosition);
                            sharedPreferences.edit().putString(Consts.KEY_ALL_LISTS, new Gson().toJson(all_lists)).apply();
                            ((BaseAdapter) todoLists.getAdapter()).notifyDataSetChanged();
                            resetSpinner();
                        }
                    });
                    d.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface _dialog, int _which) {
                            resetSpinner();
                        }
                    });
                    d.create().show();
                } else { // cancel
                    resetSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> _param1) {
            }
        });

        todoLists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _param1, View _param2, int _position, long _param4) {
                Intent intent = new Intent();
                intent.setClass(context, TodoListActivity.class);
                intent.putExtra(Consts.ITEM_NUMBER, _position);
                startActivity(intent);
            }
        });

        todoLists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _position, long _param4) {
                selectedListPosition = _position;
                menuSpinner.performClick();
                return true;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                Intent intent = new Intent();
                intent.setClass(context, TodoListActivity.class);
                intent.putExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM);
                startActivity(intent);
            }
        });
    }

    private void initializeLogic() {
        menuSpinnerOptions.add(getString(R.string.copy));
        menuSpinnerOptions.add(getString(R.string.share));
        menuSpinnerOptions.add(getString(R.string.delete));
        menuSpinnerOptions.add(getString(R.string.cancel));
        menuSpinner.setAdapter(new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, menuSpinnerOptions));
        menuSpinner.setGravity(Gravity.CENTER_HORIZONTAL);
        resetSpinner();
        if (sharedPreferences.getString(Consts.KEY_TUTORIAL, Consts.EMPTY_STRING).equals(Consts.EMPTY_STRING)) {
            sharedPreferences.edit().putString(Consts.KEY_TUTORIAL, "done").apply();
            showTutorial();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(Consts.KEY_ALL_LISTS, Consts.EMPTY_LIST_STRING).equals(Consts.EMPTY_LIST_STRING)) {
            sharedPreferences.edit().putString(Consts.KEY_ALL_LISTS, Consts.EMPTY_LIST_STRING).apply();
        }
        if (sharedPreferences.getInt(Consts.KEY_LINES_COUNT, Consts.INVALID_LINES_COUNT) == Consts.INVALID_LINES_COUNT) {
            sharedPreferences.edit().putInt(Consts.KEY_LINES_COUNT, Consts.DEFAULT_LINES_COUNT).apply();
        }
        if (sharedPreferences.getInt(Consts.KEY_CHARACTER_LIMIT, Consts.INVALID_CHARACTER_LIMIT) == Consts.INVALID_CHARACTER_LIMIT) {
            sharedPreferences.edit().putInt(Consts.KEY_CHARACTER_LIMIT, Consts.DEFAULT_CHARACTER_LIMIT).apply();
        }
        all_lists = new Gson().fromJson(sharedPreferences.getString(Consts.KEY_ALL_LISTS, Consts.EMPTY_LIST_STRING), new TypeToken<ArrayList<ToDoList>>() {
        }.getType());
        linesCount = sharedPreferences.getInt(Consts.KEY_LINES_COUNT, Consts.DEFAULT_LINES_COUNT);
        characterLimit = sharedPreferences.getInt(Consts.KEY_CHARACTER_LIMIT, Consts.DEFAULT_CHARACTER_LIMIT);
        todoLists.setAdapter(new ToDoListsAdapter(all_lists));
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.edit().putString(Consts.KEY_ALL_LISTS, new Gson().toJson(all_lists)).commit();
    }

    private String cutAfterNLines(final String string, final int n) {
        String nLines = Consts.EMPTY_STRING;
        if (n == 0) {
            nLines = Consts.SO_ON;
        } else {
            // find the position of the n-th new line
            int pos = string.indexOf(Consts.NEW_LINE);
            for (int i = 0; i < n && pos != -1; i++) {
                pos = string.indexOf(Consts.NEW_LINE, pos + 1);
            }

            int nthEndLine = pos;
            if (nthEndLine == -1) {
                nLines = string;
            } else {
                nLines = string.substring(0, nthEndLine) + Consts.NEW_LINE + Consts.SO_ON;
            }
        }
        return nLines;
    }


    private void paintView(final View view, final int id) {
        view.setBackgroundColor(Consts.colors.get(id));
    }


    private void refreshListView() {
        ((BaseAdapter) todoLists.getAdapter()).notifyDataSetChanged();
    }


    private void showTutorial() {
        final AlertDialog.Builder d = new AlertDialog.Builder(context);
        final AlertDialog.Builder d1 = new AlertDialog.Builder(context);
        final AlertDialog.Builder d2 = new AlertDialog.Builder(context);
        final AlertDialog.Builder d3 = new AlertDialog.Builder(context);
        d.setTitle(R.string.tutorial_1_title);
        d.setMessage(R.string.tutorial_1_content);
        d.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface _dialog, int _which) {
                d1.setTitle(R.string.tutorial_2_title);
                d1.setMessage(R.string.tutorial_2_content);
                d1.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        d2.setTitle(R.string.tutorial_3_title);
                        d2.setMessage(R.string.tutorial_3_content);
                        d2.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {
                                d3.setTitle(R.string.tutorial_4_title);
                                d3.setMessage(R.string.tutorial_4_content);
                                d3.setPositiveButton(R.string.got_it, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface _dialog, int _which) {
                                        sharedPreferences.edit().putString(Consts.KEY_TUTORIAL, "done").apply();
                                    }
                                });
                                d3.setNegativeButton(R.string.show_again, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface _dialog, int _which) {
                                        sharedPreferences.edit().remove(Consts.KEY_TUTORIAL).apply();
                                    }
                                });
                                d3.create().show();
                            }
                        });
                        d2.setNegativeButton(R.string.close, null);
                        d2.create().show();
                    }
                });
                d1.setNegativeButton(R.string.close, null);
                d1.create().show();
            }
        });
        d.setNegativeButton(R.string.close, null);
        d.create().show();
    }


    private void resetSpinner() {
        selectedListPosition = -1;
        menuSpinner.setSelection(menuSpinnerOptions.size() - 1);
    }


    public class ToDoListsAdapter extends BaseAdapter {
        ArrayList<ToDoList> lists;

        public ToDoListsAdapter(ArrayList<ToDoList> arr) {
            lists = arr;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public ToDoList getItem(int index) {
            return lists.get(index);
        }

        @Override
        public long getItemId(int index) {
            return index;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = inflater.inflate(R.layout.item_list, null);
            }

            final LinearLayout linear_all = view.findViewById(R.id.linear_all);
            final TextView summary = view.findViewById(R.id.summary);
            final Button expand_b = view.findViewById(R.id.expand_b);
            final TextView title = view.findViewById(R.id.title);

            paintView(linear_all, lists.get(position).m_background_color);
            expand_b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lists.get(position).m_expanded = !lists.get(position).m_expanded;
                    refreshListView();
                }
            });
            title.setText(lists.get(position).m_title);
            ToDoList todo_list = lists.get(position);

            StringBuilder listSummary = new StringBuilder(Consts.EMPTY_STRING);
            for (ToDoItem item : todo_list.m_items) {
                if (item.m_is_checked) {
                    listSummary.append(Consts.V);
                } else {
                    listSummary.append(Consts.X);
                }
                listSummary.append(item.m_content).append(Consts.NEW_LINE);
            }
            if (listSummary.length() > 0) {
                listSummary = listSummary.deleteCharAt(listSummary.length() - 1);
            }
            if (lists.get(position).m_expanded) {
                expand_b.setRotation(180);
            } else {
                if (listSummary.length() > characterLimit) {
                    listSummary = listSummary.replace(characterLimit, listSummary.length()-1, Consts.SO_ON);
                }
                if (linesCount != 16) {
                    listSummary = new StringBuilder(cutAfterNLines(listSummary.toString(), linesCount));
                }
                expand_b.setRotation(0);
            }
            summary.setText(listSummary.toString());

            return view;
        }
    }
}
