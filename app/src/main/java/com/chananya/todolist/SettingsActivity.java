package com.chananya.todolist;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.net.*;
import android.text.*;
import android.util.*;
import android.webkit.*;
import android.animation.*;
import android.view.animation.*;
import java.util.*;
import java.text.*;
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
import android.net.Uri;
import android.view.View;
import android.content.ClipData;
import android.content.ClipboardManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SettingsActivity extends AppCompatActivity {
	
	
	private Toolbar _toolbar;
	private String item_text = "";
	private String all_text = "";
	private double index_0 = 0;
	private double index_1 = 0;
	
	private ArrayList<HashMap<String, Object>> all_lists = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> single_list = new ArrayList<>();
	
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
	
	private AlertDialog.Builder d;
	private SharedPreferences f;
	private AlertDialog.Builder d_single_button;
	private AlertDialog.Builder d1;
	private AlertDialog.Builder d2;
	private AlertDialog.Builder d3;
	private Intent i1 = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.settings);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_toolbar = (Toolbar) findViewById(R.id._toolbar);
		setSupportActionBar(_toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
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
		d = new AlertDialog.Builder(this);
		f = getSharedPreferences("data", Activity.MODE_PRIVATE);
		d_single_button = new AlertDialog.Builder(this);
		d1 = new AlertDialog.Builder(this);
		d2 = new AlertDialog.Builder(this);
		d3 = new AlertDialog.Builder(this);
		
		lines_count_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				if (_progressValue == 16) {
					lines_count_tv.setText("all");
				}
				else {
					lines_count_tv.setText(String.valueOf((long)(_progressValue)));
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
			public void onProgressChanged (SeekBar _param1, int _param2, boolean _param3) {
				final int _progressValue = _param2;
				character_limit_tv.setText(String.valueOf((long)(_progressValue * 10)));
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
				d.setTitle("restore");
				d.setMessage("Restore all settings?");
				d.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						lines_count_sb.setProgress((int)5);
						character_limit_sb.setProgress((int)10);
					}
				});
				d.setNegativeButton("no", new DialogInterface.OnClickListener() {
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
				d.setTitle("delete");
				d.setMessage("Delete all existing data?\nThis action cannot be undone.");
				d.setPositiveButton("yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						f.edit().remove("all lists").commit();
					}
				});
				d.setNegativeButton("no", new DialogInterface.OnClickListener() {
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
				f.edit().remove("tutorial").commit();
				SketchwareUtil.showMessage(getApplicationContext(), "done");
			}
		});
		
		help_b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				d.setTitle("tutorial #1");
				d.setMessage("Welcome to TODO list app!\n\nHere you can create your own lists so you won't forget to buy anything from the store, or to take equipment to a trip.\n\nOn the main screen all your lists appears in minimize mode. Pressing on the little triangle will expand the list and show it all. (On the settings screen you can change the settings of the minimize mode to your favorite size).");
				d.setPositiveButton("next", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						d1.setTitle("tutorial #2");
						d1.setMessage("To add a new list just tap on the big, gray 'plus' button at the bottom of the screen. To see existing list, click on it, and it'll enter editing mode, where you can change the background color (upper right on the screen to show/hide coloring menu), and of course add new items to the list, and updating, deleting or mark as 'done' existing items.");
						d1.setPositiveButton("next", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								d2.setTitle("tutorial #3");
								d2.setMessage("At the beginning the title of the list will be in focus, but one click on 'enter' will move the cursor to 'add new item' line.\n\nNote: each item can hold only one line. So if 'enter' was pressed - new item will be added, or the next item will be in the updating line.");
								d2.setPositiveButton("next", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface _dialog, int _which) {
										d3.setTitle("tutorial #4");
										d3.setMessage("Enjoy!");
										d3.setPositiveButton("Got it!", new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface _dialog, int _which) {
												f.edit().putString("tutorial", "done").commit();
											}
										});
										d3.create().show();
									}
								});
								d2.setNegativeButton("close", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface _dialog, int _which) {
										
									}
								});
								d2.create().show();
							}
						});
						d1.setNegativeButton("close", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface _dialog, int _which) {
								
							}
						});
						d1.create().show();
					}
				});
				d.setNegativeButton("close", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				d.create().show();
			}
		});
		
		copy_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_allDataTo___all_text();
				((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", all_text));
				SketchwareUtil.showMessage(getApplicationContext(), "copied to clipboard!");
			}
		});
		
		share_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_allDataTo___all_text();
				i1 = new Intent(android.content.Intent.ACTION_SEND);
				i1.setType("text/plain");
				i1.putExtra(android.content.Intent.EXTRA_SUBJECT, "My lists");
				i1.putExtra(android.content.Intent.EXTRA_TEXT, all_text);
				startActivity(i1);
			}
		});
	}
	private void initializeLogic() {
		setTitle("Settings");
		lines_count_sb.setProgress((int)Double.parseDouble(f.getString("lines count", "")));
		character_limit_sb.setProgress((int)Double.parseDouble(f.getString("character limit", "")) / 10);
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
		f.edit().putString("lines count", String.valueOf((long)(lines_count_sb.getProgress()))).commit();
		f.edit().putString("character limit", String.valueOf((long)(character_limit_sb.getProgress() * 10))).commit();
	}
	private void _allDataTo___all_text () {
		all_lists = new Gson().fromJson(f.getString("all lists", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		all_text = "";
		index_0 = 0;
		for(int _repeat14 = 0; _repeat14 < (int)(all_lists.size()); _repeat14++) {
			single_list = new Gson().fromJson(all_lists.get((int)index_0).get("list").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			item_text = all_lists.get((int)index_0).get("title").toString().concat(":\n");
			index_1 = 0;
			for(int _repeat24 = 0; _repeat24 < (int)(single_list.size()); _repeat24++) {
				if (single_list.get((int)index_1).get("done").toString().equals("true")) {
					item_text = item_text.concat("✓ ".concat(single_list.get((int)index_1).get("text").toString()).concat("\n"));
				}
				else {
					item_text = item_text.concat("× ".concat(single_list.get((int)index_1).get("text").toString()).concat("\n"));
				}
				index_1++;
			}
			if (item_text.length() > 0) {
				item_text = item_text.substring((int)(0), (int)(item_text.length() - 1));
			}
			all_text = all_text.concat(item_text.concat("\n-----\n"));
			index_0++;
		}
		if (all_text.length() > 0) {
			all_text = all_text.substring((int)(0), (int)(all_text.length() - 6));
		}
	}
	
	
	@Deprecated
	public void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}
	
	@Deprecated
	public int getLocationX(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[0];
	}
	
	@Deprecated
	public int getLocationY(View _v) {
		int _location[] = new int[2];
		_v.getLocationInWindow(_location);
		return _location[1];
	}
	
	@Deprecated
	public int getRandom(int _min, int _max) {
		Random random = new Random();
		return random.nextInt(_max - _min + 1) + _min;
	}
	
	@Deprecated
	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
			_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}
	
	@Deprecated
	public float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}
	
	@Deprecated
	public int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}
	
	@Deprecated
	public int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}
	
}
