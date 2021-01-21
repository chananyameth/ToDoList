package com.chananya.todolist;

import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.*;

import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.ArrayList;

import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TodoListActivity extends AppCompatActivity {
    private HashMap<String, Object> todo_item = new HashMap<>();
    private HashMap<String, Object> info = new HashMap<>();
    private boolean in_update_mode = false;
    private int current_update_index = 0;

    private ArrayList<HashMap<String, Object>> todo_list = new ArrayList<>();
    private ArrayList<HashMap<String, Object>> all_lists = new ArrayList<>();

    private LinearLayout linear_toolbar;
    private LinearLayout linear_all;
    private HorizontalScrollView colors_hs;
    private ImageView back_iv;
    private EditText title;
    private Button color_b;
    private ListView listview1;
    private LinearLayout linear_add;
    private LinearLayout linear_update_item;
    private EditText new_item;
    private Button add_item_b;
    private ImageView cancel_iv;
    private EditText edittext_update;
    private Button ok_b;
    private LinearLayout linear_colors;
    private ImageView imageview0;
    private ImageView imageview1;
    private ImageView imageview2;
    private ImageView imageview3;
    private ImageView imageview4;
    private ImageView imageview5;
    private ImageView imageview6;
    private ImageView imageview7;
    private ImageView imageview8;
    private ImageView imageview9;
    private ImageView imageview10;

    private SharedPreferences f;
    private ObjectAnimator oa1 = new ObjectAnimator();

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.todo_list);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        linear_toolbar = (LinearLayout) findViewById(R.id.linear_toolbar);
        linear_all = (LinearLayout) findViewById(R.id.linear_all);
        colors_hs = (HorizontalScrollView) findViewById(R.id.colors_hs);
        back_iv = (ImageView) findViewById(R.id.back_iv);
        title = (EditText) findViewById(R.id.title);
        color_b = (Button) findViewById(R.id.color_b);
        listview1 = (ListView) findViewById(R.id.listview1);
        linear_add = (LinearLayout) findViewById(R.id.linear_add);
        linear_update_item = (LinearLayout) findViewById(R.id.linear_update_item);
        new_item = (EditText) findViewById(R.id.new_item);
        add_item_b = (Button) findViewById(R.id.add_item_b);
        cancel_iv = (ImageView) findViewById(R.id.cancel_iv);
        edittext_update = (EditText) findViewById(R.id.edittext_update);
        ok_b = (Button) findViewById(R.id.ok_b);
        linear_colors = (LinearLayout) findViewById(R.id.linear_colors);
        imageview0 = (ImageView) findViewById(R.id.imageview0);
        imageview1 = (ImageView) findViewById(R.id.imageview1);
        imageview2 = (ImageView) findViewById(R.id.imageview2);
        imageview3 = (ImageView) findViewById(R.id.imageview3);
        imageview4 = (ImageView) findViewById(R.id.imageview4);
        imageview5 = (ImageView) findViewById(R.id.imageview5);
        imageview6 = (ImageView) findViewById(R.id.imageview6);
        imageview7 = (ImageView) findViewById(R.id.imageview7);
        imageview8 = (ImageView) findViewById(R.id.imageview8);
        imageview9 = (ImageView) findViewById(R.id.imageview9);
        imageview10 = (ImageView) findViewById(R.id.imageview10);
        f = getSharedPreferences(Consts.SharedPreferencesName, Activity.MODE_PRIVATE);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                finish();
            }
        });

        color_b.setOnClickListener(new View.OnClickListener() {
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
                    todo_item = new HashMap<>();
                    todo_item.put(Consts.KEY_TEXT, charSeq.substring(0, charSeq.indexOf(Consts.NEW_LINE)));
                    todo_item.put(Consts.KEY_DONE, false);
                    todo_list.add(todo_item);
                    if (charSeq.length() > (charSeq.indexOf(Consts.NEW_LINE) + 1)) {
                        new_item.setText(charSeq.substring(charSeq.indexOf(Consts.NEW_LINE) + 1, charSeq.length()));
                    } else {
                        new_item.setText(Consts.EMPTY_STRING);
                    }
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    listview1.smoothScrollToPosition(todo_list.size());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {

            }

            @Override
            public void afterTextChanged(Editable _param1) {

            }
        });

        add_item_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_item = new HashMap<>();
                todo_item.put(Consts.KEY_TEXT, new_item.getText().toString());
                todo_item.put(Consts.KEY_DONE, false);
                todo_list.add(todo_item);
                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                new_item.setText(Consts.EMPTY_STRING);
                listview1.smoothScrollToPosition(todo_list.size());
            }
        });

        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                setUpdateModeTo(false);
            }
        });

        edittext_update.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
                final String charSeq = _param1.toString();
                // checking for 'Enter' pressing, and saving the item without the \n
                if (charSeq.contains(Consts.NEW_LINE)) {
                    todo_item = new HashMap<>();
                    todo_item.put(Consts.KEY_TEXT, charSeq.replace(Consts.NEW_LINE, Consts.EMPTY_STRING));
                    todo_item.put(Consts.KEY_DONE, todo_list.get(current_update_index).get(Consts.KEY_DONE));
                    todo_list.remove(current_update_index);
                    todo_list.add(current_update_index, todo_item);
                    edittext_update.setText(Consts.EMPTY_STRING);
                    ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
                    listview1.smoothScrollToPosition(todo_list.size());
                    if ((current_update_index + 1) < todo_list.size()) {
                        current_update_index++;
                        setUpdateString(todo_list.get(current_update_index).get(Consts.KEY_TEXT).toString());
                    } else {
                        setUpdateModeTo(false);
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

        ok_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                todo_list.get(current_update_index).put(Consts.KEY_TEXT, edittext_update.getText().toString());
                setUpdateModeTo(false);
                ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
            }
        });

        imageview0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 0);
                paintView(linear_all, 0);
            }
        });

        imageview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 1);
                paintView(linear_all, 1);
            }
        });

        imageview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 2);
                paintView(linear_all, 2);
            }
        });

        imageview3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 3);
                paintView(linear_all, 3);
            }
        });

        imageview4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 4);
                paintView(linear_all, 4);
            }
        });

        imageview5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 5);
                paintView(linear_all, 5);
            }
        });

        imageview6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 6);
                paintView(linear_all, 6);
            }
        });

        imageview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 7);
                paintView(linear_all, 7);
            }
        });

        imageview8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 8);
                paintView(linear_all, 8);
            }
        });

        imageview9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                info.put(Consts.KEY_COLOR, 9);
                paintView(linear_all, 9);
            }
        });

        imageview10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info.put(Consts.KEY_COLOR, 10);
                paintView(linear_all, 10);
            }
        });
    }

    private void initializeLogic() {
        all_lists = new Gson().fromJson(f.getString(Consts.KEY_ALL_LISTS, Consts.EMPTY_STRING), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        if (getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM) == Consts.NO_ITEM) {
            info = new HashMap<>();
            info.put(Consts.KEY_TITLE, Consts.EMPTY_STRING);
            info.put(Consts.KEY_COLOR, 0);
            info.put(Consts.KEY_EXPAND, false);
            info.put(Consts.KEY_LIST, "[]");
            todo_list.clear();
        } else {
            info = all_lists.get(getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM));
            title.setText(info.get(Consts.KEY_TITLE).toString());
            paintView(linear_all, (int)info.get(Consts.KEY_COLOR));
            todo_list = new Gson().fromJson(info.get(Consts.KEY_LIST).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
        }
        listview1.setAdapter(new Listview1Adapter(todo_list));
        current_update_index = -1;
        setUpdateModeTo(false);
        title.requestFocus();
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
    public void onPause() {
        super.onPause();
        info.put(Consts.KEY_TITLE, title.getText().toString());
        info.put(Consts.KEY_LIST, new Gson().toJson(todo_list));
        if (getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM) == Consts.NO_ITEM) {
            all_lists.add(info);
        } else {
            all_lists.remove(getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM));
            all_lists.add(getIntent().getIntExtra(Consts.ITEM_NUMBER, Consts.NO_ITEM), info);
        }
        f.edit().putString(Consts.KEY_ALL_LISTS, new Gson().toJson(all_lists)).commit();
    }

    private void paintView(final View view, final double id) {
        view.setBackgroundColor(MainActivity.colors.get(id));
    }


    private void refreshListView() {
        ((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
    }


    private void setUpdateModeTo(final boolean mode) {
        if (mode) {
            in_update_mode = true;
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
            in_update_mode = false;
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


    public class Listview1Adapter extends BaseAdapter {
        ArrayList<HashMap<String, Object>> _data;

        public Listview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
            _data = _arr;
        }

        @Override
        public int getCount() {
            return _data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int _index) {
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
            final EditText edittext = (EditText) view.findViewById(R.id.edittext);
            final ImageView delete_item_iv = (ImageView) view.findViewById(R.id.delete_item_iv);

            if (_data.get(position).get(Consts.KEY_DONE) == true) {
                done_cb.setChecked(true);
            } else {
                done_cb.setChecked(false);
            }
            edittext.setText(_data.get(position).get(Consts.KEY_TEXT).toString());
            edittext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current_update_index = position;
                    setUpdateModeTo(true);
                    setUpdateString(_data.get(position).get(Consts.KEY_TEXT).toString());
                }
            });
            delete_item_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo_list.remove(position);
                    if (current_update_index == position) {
                        setUpdateModeTo(false);
                    }
                    if (current_update_index > position) {
                        current_update_index--;
                        setUpdateString(todo_list.get(current_update_index).get(Consts.KEY_TEXT).toString());
                    }
                    refreshListView();
                }
            });
            done_cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo_item = todo_list.get(position);
                    if (done_cb.isChecked()) {
                        todo_item.put(Consts.KEY_DONE, true);
                    } else {
                        todo_item.put(Consts.KEY_DONE, false);
                    }
                    todo_list.remove(position);
                    todo_list.add(position, todo_item);
                }
            });

            return view;
        }
    }
}
