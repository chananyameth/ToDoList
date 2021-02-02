package com.chananya.todolist;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private Configuration configuration;

    private SeekBar lines_count_sb;
    private SeekBar character_limit_sb;
    private TextView lines_count_tv;
    private TextView character_limit_tv;

    private AlertDialog.Builder d;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.settings);
        initialize();
        initializeLogic();
    }

    private void initialize() {
        configuration = Configuration.getInstance(SettingsActivity.this);

        lines_count_sb = (SeekBar) findViewById(R.id.lines_count_sb);
        character_limit_sb = (SeekBar) findViewById(R.id.character_limit_sb);
        Button restore_b = (Button) findViewById(R.id.restore_b);
        Button import_b = (Button) findViewById(R.id.import_data);
        Button export_b = (Button) findViewById(R.id.export_data);
        Button delete_all_b = (Button) findViewById(R.id.delete_all_b);
        Button tutorial_b = (Button) findViewById(R.id.tutorial_b);
        ImageView help_b = (ImageView) findViewById(R.id.help_iv);
        lines_count_tv = (TextView) findViewById(R.id.lines_count_tv);
        character_limit_tv = (TextView) findViewById(R.id.character_limit_tv);
        ImageView copy_iv = (ImageView) findViewById(R.id.copy_iv);
        ImageView share_iv = (ImageView) findViewById(R.id.share_iv);
        d = new AlertDialog.Builder(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id._toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                onBackPressed();
            }
        });

        lines_count_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar _param1, int _progressValue, boolean _param3) {
                if (_progressValue == 16) {
                    lines_count_tv.setText(R.string.all);
                } else {
                    lines_count_tv.setText(String.valueOf(_progressValue));
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

        import_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                d.setTitle(R.string.import_warning_title1);
                d.setMessage(R.string.import_warning_content1);
                d.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface _dialog, int _which) {
                        ClipData clip = Objects.requireNonNull(
                                ((ClipboardManager) Objects.requireNonNull(
                                        getSystemService(Context.CLIPBOARD_SERVICE)))
                                        .getPrimaryClip());
                        final String data = clip.getItemAt(0).getText().toString();
                        if (clip.getItemCount() < 1) {
                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.nothing_in_clipboard));
                            SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.nothing_in_clipboard));
                        } else {
                            AlertDialog.Builder d1 = new AlertDialog.Builder(SettingsActivity.this);
                            d1.setTitle(R.string.import_warning_title2);
                            d1.setMessage(getString(R.string.import_warning_content2) + "\n\n" + data);
                            d1.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface _dialog, int _which) {
                                    configuration.data = Configuration.gson.fromJson(data, Configuration.Data.class);
                                    configuration.saveConfig();
                                    SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.imported_successfully));
                                }
                            });
                            d1.setNegativeButton(R.string.no, null);
                            d1.create().show();
                        }
                    }
                });
                d.setNegativeButton(R.string.no, null);
                d.create().show();
            }
        });
        export_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                ((ClipboardManager) Objects.requireNonNull(getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(ClipData.newPlainText("clipboard", Configuration.gson.toJson(configuration.data)));
                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.exported_to_clipboard));
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
                        configuration.data.lists.clear();
                    }
                });
                d.setNegativeButton(R.string.no, null);
                d.create().show();
            }
        });

        tutorial_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                configuration.data.show_tutorial = true;
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
                                                configuration.data.show_tutorial = false;
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
                allDataToString();
                ((ClipboardManager) Objects.requireNonNull(getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(ClipData.newPlainText("clipboard", allDataToString()));
                SketchwareUtil.showMessage(getApplicationContext(), getString(R.string.copied_to_clipboard));
            }
        });

        share_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _view) {
                allDataToString();
                Intent share_intent = new Intent(android.content.Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "My lists");
                share_intent.putExtra(android.content.Intent.EXTRA_TEXT, allDataToString());
                startActivity(share_intent);
            }
        });
    }

    private void initializeLogic() {
        setTitle(getString(R.string.settings));
        lines_count_sb.setProgress(configuration.data.lines_count);
        character_limit_sb.setProgress(configuration.data.character_limit / 10);
    }


    @Override
    public void onPause() {
        super.onPause();
        configuration.data.lines_count = lines_count_sb.getProgress();
        configuration.data.character_limit = character_limit_sb.getProgress() * 10;
        configuration.saveConfig();
    }

    private String allDataToString() {
        StringBuilder allText = new StringBuilder(Consts.EMPTY_STRING);
        String separator = Consts.NEW_LINE + "-----" + Consts.NEW_LINE;
        for (ToDoList list : configuration.data.lists) {
            allText.append(list.toStringWithTitle()).append(separator);
        }
        if (allText.length() != 0) { // there's 1 separator too many
            return allText.substring(0, allText.length() - separator.length());
        } else {
            return allText.toString();
        }
    }
}
