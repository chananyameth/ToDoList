package com.chananya.todolist;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private Configuration configuration;

    private int characterLimit = 0;
    private int linesCount = 0;
    private int selectedListPosition = 0;

    private ArrayList<ToDoList> all_lists = null;
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
        configuration = Configuration.getInstance(context);

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        Button settings_b = findViewById(R.id.settings_b);
        Button about_b = findViewById(R.id.about_b);
        menuSpinner = findViewById(R.id.spinner);
        todoLists = findViewById(R.id.items_lv);

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
                String list_preview = todo_list.toStringWithTitle();

                if (selected_menu_item_position == 0) { // copy
                    ((ClipboardManager) Objects.requireNonNull(getSystemService(CLIPBOARD_SERVICE))).setPrimaryClip(ClipData.newPlainText("clipboard", list_preview));
                    SketchwareUtil.showMessage(context, getString(R.string.copied_to_clipboard));
                    resetSpinner();
                } else if (selected_menu_item_position == 1) { // share
                    Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.my_list));
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, list_preview);
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
                            configuration.data.lists = all_lists; // TODO: might be redundant: all_lists already points to the config?
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
        if (configuration.data.show_tutorial) {
            configuration.data.show_tutorial = false;
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
        all_lists = configuration.data.lists;
        linesCount = configuration.data.lines_count;
        characterLimit = configuration.data.character_limit;
        todoLists.setAdapter(new ToDoListsAdapter(all_lists));
    }

    @Override
    public void onPause() {
        super.onPause();
        configuration.saveConfig();
    }

    private String cutAfterNLines(final String string, final int n) {
        String nLines;
        if (n == 0) {
            nLines = Consts.SO_ON;
        } else {
            // find the position of the n-th new line
            int pos = string.indexOf(Consts.NEW_LINE);
            for (int i = 1; i < n && pos != -1; i++) {
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
                                        configuration.data.show_tutorial = false;
                                    }
                                });
                                d3.setNegativeButton(R.string.show_again, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface _dialog, int _which) {
                                        configuration.data.show_tutorial = true;
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

            StringBuilder listSummary = new StringBuilder(todo_list.toString());
            if (lists.get(position).m_expanded) {
                expand_b.setRotation(180);
            } else {
                if (listSummary.length() > characterLimit) {
                    listSummary = listSummary.replace(characterLimit, listSummary.length(), Consts.SO_ON);
                }
                if (linesCount != 16) { // max=all lines
                    listSummary = new StringBuilder(cutAfterNLines(listSummary.toString(), linesCount));
                }
                expand_b.setRotation(0);
            }
            summary.setText(listSummary.toString());

            return view;
        }
    }
}
