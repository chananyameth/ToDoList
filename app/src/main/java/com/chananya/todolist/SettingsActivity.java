package com.chananya.todolist;

import android.os.*;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.Intent;
import android.view.View;
import android.content.ClipData;
import android.content.ClipboardManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SettingsActivity extends AppCompatActivity {
    private String allText = Consts.EMPTY_STRING;

    private LinearLayout linear_all;
    private LinearLayout linear6;
    private SeekBar lines_count_sb;
    private LinearLayout linear7;
    private SeekBar character_limit_sb;
    private Button restore_b;
    private LinearLayout linear8;
    private Button delete_all_b;
    private Button tutorial_b;
    private Button help_b;
    private TextView textview4;
    private TextView lines_count_tv;
    private TextView textview6;
    private TextView character_limit_tv;
    private ImageView copy_iv;
    private TextView textview7;
    private ImageView share_iv;

    private SharedPreferences f;
    private AlertDialog.Builder d;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.settings);
        initialize(_savedInstanceState);
        initializeLogic();
    }

    private void initialize(Bundle _savedInstanceState) {

        Toolbar toolbar = (Toolbar) findViewById(R.id._toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });
        linear_all = (LinearLayout) findViewById(R.id.linear_all);
        linear6 = (LinearLayout) findViewById(R.id.linear6);
        lines_count_sb = (SeekBar) findViewById(R.id.lines_count_sb);
        linear7 = (LinearLayout) findViewById(R.id.linear7);
        character_limit_sb = (SeekBar) findViewById(R.id.character_limit_sb);
        restore_b = (Button) findViewById(R.id.restore_b);
        linear8 = (LinearLayout) findViewById(R.id.linear8);
        delete_all_b = (Button) findViewById(R.id.delete_all_b);
        tutorial_b = (Button) findViewById(R.id.tutorial_b);
        help_b = (Button) findViewById(R.id.help_b);
        textview4 = (TextView) findViewById(R.id.textview4);
        lines_count_tv = (TextView) findViewById(R.id.lines_count_tv);
        textview6 = (TextView) findViewById(R.id.textview6);
        character_limit_tv = (TextView) findViewById(R.id.character_limit_tv);
        copy_iv = (ImageView) findViewById(R.id.copy_iv);
        textview7 = (TextView) findViewById(R.id.textview7);
        share_iv = (ImageView) findViewById(R.id.share_iv);
        f = getSharedPreferences(Consts.SharedPreferencesName, Activity.MODE_PRIVATE);
        d = new AlertDialog.Builder(this);

        lines_count_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _param1, int _progressValue, boolean _param3) {
                if (_progressValue == 16) {
                    lines_count_tv.setText("all");
                } else {
                    lines_count_tv.setText(String.valueOf((long) (_progressValue)));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar _param1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar _param2) {

            }
        });

        character_limit_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _param1, int _progressValue, boolean _param3) {
                character_limit_tv.setText(String.valueOf(_progressValue * 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar _param1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar _param2) {

            }
        });

        restore_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setTitle(R.string.restore_settings_title);
                d.setMessage(R.string.restore_settings_content);
                d.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        lines_count_sb.setProgress(5);
                        character_limit_sb.setProgress(10);
                    }
                });
                d.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d.create().show();
            }
        });

        delete_all_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setTitle(R.string.delete_all_title);
                d.setMessage(R.string.delete_all_content);
                d.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        f.edit().remove(Consts.KEY_ALL_LISTS).commit();
                    }
                });
                d.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d.create().show();
            }
        });

        tutorial_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                f.edit().remove(Consts.KEY_TUTORIAL).commit();
                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.done));
            }
        });

        help_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                final AlertDialog.Builder d0 = new AlertDialog.Builder(SettingsActivity.this);
                final AlertDialog.Builder d1 = new AlertDialog.Builder(SettingsActivity.this);
                final AlertDialog.Builder d2 = new AlertDialog.Builder(SettingsActivity.this);
                final AlertDialog.Builder d3 = new AlertDialog.Builder(SettingsActivity.this);
                d0.setTitle(R.string.tutorial_1_title);
                d0.setMessage(R.string.tutorial_1_content);
                d0.setPositiveButton(R.string.next, new DialogInterface.OnClickListener() {
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
                                                f.edit().putString(Consts.KEY_TUTORIAL, "done").commit();
                                            }
                                        });
                                        d3.create().show();
                                    }
                                });
                                d2.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface _dialog, int _which) {

                                    }
                                });
                                d2.create().show();
                            }
                        });
                        d1.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface _dialog, int _which) {

                            }
                        });
                        d1.create().show();
                    }
                });
                d0.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {

                    }
                });
                d0.create().show();
            }
        });

        copy_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                allLists_to_allText();
                ((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", allText));
                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.copied_to_clipboard));
            }
        });

        share_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                allLists_to_allText();
                Intent share_intent = new Intent(android.content.Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My lists");
                share_intent.putExtra(android.content.Intent.EXTRA_TEXT, allText);
                startActivity(share_intent);
            }
        });
    }

    private void initializeLogic() {
        setTitle(getString(R.string.settings));
        lines_count_sb.setProgress(f.getInt(Consts.KEY_LINES_COUNT, Consts.DEFAULT_LINES_COUNT));
        character_limit_sb.setProgress(f.getInt(Consts.KEY_CHARACTER_LIMIT, Consts.DEFAULT_CHARACTER_LIMIT) / 10);
    }

    @Override
    protected void onActivityResult(int _requestCode, int _resultCode, Intent _data) {
        super.onActivityResult(_requestCode, _resultCode, _data);

        switch (_requestCode) {

            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        f.edit().putInt(Consts.KEY_LINES_COUNT, lines_count_sb.getProgress()).commit();
        f.edit().putInt(Consts.KEY_CHARACTER_LIMIT, character_limit_sb.getProgress() * 10).commit();
    }

    private void allLists_to_allText() {
        ArrayList<HashMap<String, Object>> allLists = new Gson().fromJson(f.getString(Consts.KEY_ALL_LISTS, Consts.EMPTY_STRING), new TypeToken<ArrayList<HashMap<String, Object>>>() {
        }.getType());
        allText = Consts.EMPTY_STRING;
        for (int i = 0; i < allLists.size(); i++) {
            ArrayList<HashMap<String, Object>> single_list = new Gson().fromJson(allLists.get(i).get(Consts.KEY_LIST).toString(), new TypeToken<ArrayList<HashMap<String, Object>>>() {
            }.getType());
            String item_text = allLists.get(i).get(Consts.KEY_TITLE).toString() + ":\n";
            for (int j = 0; j < single_list.size(); j++) {
                if (single_list.get(j).get(Consts.KEY_DONE) == true) {
                    item_text += Consts.V;
                } else {
                    item_text += Consts.X;
                }
                item_text += single_list.get(j).get(Consts.KEY_TEXT).toString() + Consts.NEW_LINE;
            }
            if (item_text.length() > 0) {
                item_text = item_text.substring(0, item_text.length() - 1);
            }
            allText += item_text + "\n-----\n";
        }
        if (allText.length() > 0) {
            allText = allText.substring(0, allText.length() - 6);
        }
    }
}
