package com.chananya.todolist;

import android.graphics.Paint;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.animation.ObjectAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

public class TodoListActivity extends AppCompatActivity {
    private Configuration configuration;

    private int current_update_index = 0;

    private ArrayList<ToDoList> all_lists;
    private ToDoList todo_list;

    private ObjectAnimator oa1 = new ObjectAnimator();

    private LinearLayout linear_all;
    private HorizontalScrollView colors_hs;
    private EditText title;
    private ListView all_items;
    private LinearLayout linear_add;
    private LinearLayout linear_update_item;
    private EditText new_item;
    private EditText edittext_update;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.todo_list);
        initialize();
        initializeLogic();
    }


    private void initialize() {
        configuration = Configuration.getInstance(TodoListActivity.this);

        linear_all = (LinearLayout) findViewById(R.id.linear_all);
        colors_hs = (HorizontalScrollView) findViewById(R.id.colors_hs);
        ImageView back_iv = (ImageView) findViewById(R.id.back_iv);
        title = (EditText) findViewById(R.id.title);
        ImageView palette_iv = (ImageView) findViewById(R.id.palette_iv);
        all_items = (ListView) findViewById(R.id.items_lv);
        linear_add = (LinearLayout) findViewById(R.id.linear_add);
        linear_update_item = (LinearLayout) findViewById(R.id.linear_update_item);
        new_item = (EditText) findViewById(R.id.new_item);
        ImageView add_item_iv = (ImageView) findViewById(R.id.add_item_iv);
        ImageView cancel_iv = (ImageView) findViewById(R.id.cancel_iv);
        edittext_update = (EditText) findViewById(R.id.edittext_update);
        ImageView ok_iv = (ImageView) findViewById(R.id.ok_iv);
        ImageView imageview0 = (ImageView) findViewById(R.id.imageview0);
        ImageView imageview1 = (ImageView) findViewById(R.id.imageview1);
        ImageView imageview2 = (ImageView) findViewById(R.id.imageview2);
        ImageView imageview3 = (ImageView) findViewById(R.id.imageview3);
        ImageView imageview4 = (ImageView) findViewById(R.id.imageview4);
        ImageView imageview5 = (ImageView) findViewById(R.id.imageview5);
        ImageView imageview6 = (ImageView) findViewById(R.id.imageview6);
        ImageView imageview7 = (ImageView) findViewById(R.id.imageview7);
        ImageView imageview8 = (ImageView) findViewById(R.id.imageview8);
        ImageView imageview9 = (ImageView) findViewById(R.id.imageview9);
        ImageView imageview10 = (ImageView) findViewById(R.id.imageview10);
        ListView colors_linear = (ListView) findViewById(R.id.colors_lv);
        colors_linear.setAdapter(new ColorsAdapter());

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        palette_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                if (colors_hs.getTranslationY() == 0) {
                    oa1.setTarget(colors_hs);
                    oa1.setPropertyName("translationY");
                    oa1.setFloatValues(0, SketchwareUtil.getDip(getApplicationContext(), -64));
                    oa1.setDuration(300);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa1.start();
                } else {
                    oa1.setTarget(colors_hs);
                    oa1.setPropertyName("translationY");
                    oa1.setFloatValues(SketchwareUtil.getDip(getApplicationContext(), -64), 0);
                    oa1.setDuration(300);
                    oa1.setInterpolator(new DecelerateInterpolator());
                    oa1.start();
                }
            }
        });

        new_item.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String charSeq = _param1.toString();
                // checking for 'Enter' pressing, and saving the item without the \n
                if (charSeq.contains(Consts.NEW_LINE)) {
                    ToDoItem todo_item = new ToDoItem();
                    todo_item.m_content = charSeq.substring(0, charSeq.indexOf(Consts.NEW_LINE));
                    todo_item.m_is_checked = false;
                    todo_list.m_items.add(todo_item);
                    if (charSeq.length() > charSeq.indexOf(Consts.NEW_LINE) + 1) {
                        new_item.setText(charSeq.substring(charSeq.indexOf(Consts.NEW_LINE) + 1));
                    } else {
                        new_item.setText(Consts.EMPTY_STRING);
                    }
                    refreshListView();
                    all_items.smoothScrollToPosition(todo_list.m_items.size());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        add_item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                ToDoItem todo_item = new ToDoItem();
                todo_item.m_content = new_item.getText().toString();
                todo_item.m_is_checked = false;
                todo_list.m_items.add(todo_item);
                refreshListView();
                new_item.setText(Consts.EMPTY_STRING);
                all_items.smoothScrollToPosition(todo_list.m_items.size());
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                setUpdateItemModeTo(false);
            }
        });

        edittext_update.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String charSeq = _param1.toString();
                // checking for 'Enter' pressing, and saving the item without the \n
                if (charSeq.contains(Consts.NEW_LINE)) {
                    todo_list.m_items.get(current_update_index).m_content = charSeq.replace(Consts.NEW_LINE, Consts.EMPTY_STRING);
                    edittext_update.setText(Consts.EMPTY_STRING);
                    refreshListView();
                    all_items.smoothScrollToPosition(todo_list.m_items.size());
                    if (current_update_index + 1 < todo_list.m_items.size()) {
                        current_update_index++;
                        setUpdateString(todo_list.m_items.get(current_update_index).m_content);
                    } else {
                        setUpdateItemModeTo(false);
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        ok_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_items.get(current_update_index).m_content = edittext_update.getText().toString();
                setUpdateItemModeTo(false);
                refreshListView();
            }
        });

        imageview0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 0;
                paintView(linear_all, 0);
            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 1;
                paintView(linear_all, 1);
            }
        });

        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 2;
                paintView(linear_all, 2);
            }
        });

        imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 3;
                paintView(linear_all, 3);
            }
        });

        imageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 4;
                paintView(linear_all, 4);
            }
        });

        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 5;
                paintView(linear_all, 5);
            }
        });

        imageview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 6;
                paintView(linear_all, 6);
            }
        });

        imageview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 7;
                paintView(linear_all, 7);
            }
        });

        imageview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 8;
                paintView(linear_all, 8);
            }
        });

        imageview9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.m_background_color = 9;
                paintView(linear_all, 9);
            }
        });

        imageview10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo_list.m_background_color = 10;
                paintView(linear_all, 10);
            }
        });
    }


    private void initializeLogic() {
        all_lists = configuration.data.lists;

        current_update_index = -1;
        setUpdateItemModeTo(false);

        if (getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM) == Consts.NO_ITEM) {
            todo_list = new ToDoList();
            todo_list.m_title = Consts.EMPTY_STRING;
            todo_list.m_background_color = 0;
            todo_list.m_expanded = false;
            todo_list.m_items = new ArrayList<>();
            title.requestFocus();
        } else {
            todo_list = all_lists.get(getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM));
            title.setText(todo_list.m_title);
            paintView(linear_all, todo_list.m_background_color);
            new_item.requestFocus();
        }
        all_items.setAdapter(new ToDoItemsAdapter(todo_list.m_items));
    }

    @Override
    public void onPause() {
        super.onPause();
        todo_list.m_title = title.getText().toString();
        if (getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM) == Consts.NO_ITEM) {
            all_lists.add(todo_list);
        } else {
            all_lists.set(getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM), todo_list);
        }
        configuration.data.lists = all_lists; //TODO: redundant?
        configuration.saveConfig();
    }


    private void paintView(final View view, final int id) {
        view.setBackgroundColor(Consts.colors.get(id));
    }


    private void refreshListView() {
        Collections.sort(todo_list.m_items, Consts.itemsComparator);
        ((BaseAdapter) all_items.getAdapter()).notifyDataSetChanged();
    }


    private void setUpdateItemModeTo(final boolean mode) {
        if (mode) {
            linear_add.setVisibility(View.GONE);
            linear_update_item.setVisibility(View.VISIBLE);

            new_item.setText(Consts.EMPTY_STRING);
            oa1.setTarget(linear_update_item);
            oa1.setPropertyName("translationY");
            oa1.setFloatValues(-600, 0);
            oa1.setDuration(350);
            oa1.setInterpolator(new DecelerateInterpolator());
            oa1.start();
            edittext_update.requestFocus();
        } else {
            current_update_index = -1;
            linear_add.setVisibility(View.VISIBLE);
            linear_update_item.setVisibility(View.GONE);

            edittext_update.setText(Consts.EMPTY_STRING);
            new_item.requestFocus();
        }
    }


    private void setUpdateString(final String string) {
        edittext_update.setText(string);
    }


    public class ToDoItemsAdapter extends BaseAdapter {
        private ArrayList<ToDoItem> _data;

        public ToDoItemsAdapter(ArrayList<ToDoItem> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public ToDoItem getItem(int _index) {
            return _data.get(_index);
        }

        @Override
        public long getItemId(int _index) {
            return _index;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = _inflater.inflate(R.layout.todo_item, null);
            }

            final LinearLayout linear1 = (LinearLayout) view.findViewById(R.id.linear1);
            final CheckBox done_cb = (CheckBox) view.findViewById(R.id.done_cb);
            final TextView textview = (TextView) view.findViewById(R.id.textview);
            final ImageView delete_item_iv = (ImageView) view.findViewById(R.id.delete_item_iv);

            if (_data.get(position).m_is_checked) {
                done_cb.setChecked(true);
                textview.setPaintFlags(textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textview.setTextColor(0x44000000);
            } else {
                done_cb.setChecked(false);
                textview.setPaintFlags(textview.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                textview.setTextColor(0xff000000);
            }
            textview.setText(_data.get(position).m_content);
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_update_index = position;
                    setUpdateItemModeTo(true);
                    setUpdateString(_data.get(position).m_content);
                }
            });
            delete_item_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo_list.m_items.remove(position);
                    if (current_update_index == position) {
                        setUpdateItemModeTo(false);
                    }
                    if (current_update_index > position) {
                        current_update_index--;
                        setUpdateString(todo_list.m_items.get(current_update_index).m_content);
                    }
                    refreshListView();
                }
            });
            done_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo_list.m_items.get(position).m_is_checked = done_cb.isChecked();
                    refreshListView();
                }
            });

            return view;
        }
    }


    public class ColorsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Consts.colors_arr.size();
        }

        @Override
        public Object getItem(int i) {
            return Consts.colors_arr.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            LayoutInflater _inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (view == null) {
                view = _inflater.inflate(R.layout.color_item, null);
            }

            final ImageView color_iv = (ImageView) view.findViewById(R.id.single_color);
            color_iv.setBackgroundColor(Consts.colors_arr.get(position));
            color_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo_list.m_background_color = Consts.colors_arr.get(position);
                    paintView(linear_all, todo_list.m_background_color);
                }
            });

            return view;
        }
    }
}
