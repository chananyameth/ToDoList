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
import android.support.design.widget.FloatingActionButton;
import java.util.HashMap;
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
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.content.ClipData;
import android.content.ClipboardManager;

public class MainActivity extends AppCompatActivity {
	
	
	private FloatingActionButton _fab;
	private double index = 0;
	private double temp1 = 0;
	private double index2 = 0;
	private HashMap<String, Object> temp2 = new HashMap<>();
	private String CAnL_return = "";
	private String temp3 = "";
	private double character_limit = 0;
	private double lines_count = 0;
	private double position = 0;
	
	private ArrayList<HashMap<String, Object>> all_lists = new ArrayList<>();
	private ArrayList<HashMap<String, Object>> todo_list = new ArrayList<>();
	private ArrayList<String> spinnerOptions = new ArrayList<>();
	
	private LinearLayout linear_toolbar;
	private LinearLayout linear_all;
	private Button settings_b;
	private TextView textview1;
	private Button about_b;
	private Spinner spinner;
	private ListView listview1;
	
	private SharedPreferences f;
	private Intent i = new Intent();
	private AlertDialog.Builder d;
	private AlertDialog.Builder d_single_button;
	private AlertDialog.Builder d1;
	private AlertDialog.Builder d2;
	private AlertDialog.Builder d3;
	private Intent i1 = new Intent();
	@Override
	protected void onCreate(Bundle _savedInstanceState) {
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.main);
		initialize(_savedInstanceState);
		initializeLogic();
	}
	
	private void initialize(Bundle _savedInstanceState) {
		
		_fab = (FloatingActionButton) findViewById(R.id._fab);
		
		linear_toolbar = (LinearLayout) findViewById(R.id.linear_toolbar);
		linear_all = (LinearLayout) findViewById(R.id.linear_all);
		settings_b = (Button) findViewById(R.id.settings_b);
		textview1 = (TextView) findViewById(R.id.textview1);
		about_b = (Button) findViewById(R.id.about_b);
		spinner = (Spinner) findViewById(R.id.spinner);
		listview1 = (ListView) findViewById(R.id.listview1);
		f = getSharedPreferences("data", Activity.MODE_PRIVATE);
		d = new AlertDialog.Builder(this);
		d_single_button = new AlertDialog.Builder(this);
		d1 = new AlertDialog.Builder(this);
		d2 = new AlertDialog.Builder(this);
		d3 = new AlertDialog.Builder(this);
		
		settings_b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), SettingsActivity.class);
				startActivity(i);
			}
		});
		
		about_b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				d_single_button.setTitle("About");
				d_single_button.setMessage("This app created by Chananya © חנניה. You may use it and share it freely, but you may not charge money for it.\n\nYou can send me your comments and ideas to chananya@g.jct.ac.il.\n\nEnjoy!");
				d_single_button.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface _dialog, int _which) {
						
					}
				});
				d_single_button.create().show();
			}
		});
		
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				//'position' (not '_position', the parameter) should hold the position in 'all_lists' of the selected item
				if ((position == -1) || (_position == (spinnerOptions.size() - 1))) {
					return;
				}
				todo_list = new Gson().fromJson(all_lists.get((int)position).get("list").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
				temp3 = all_lists.get((int)position).get("title").toString().concat(":\n");
				temp1 = 0;
				for(int _repeat37 = 0; _repeat37 < (int)(todo_list.size()); _repeat37++) {
					if (todo_list.get((int)temp1).get("done").toString().equals("true")) {
						temp3 = temp3.concat("✓ ".concat(todo_list.get((int)temp1).get("text").toString()).concat("\n"));
					}
					else {
						temp3 = temp3.concat("× ".concat(todo_list.get((int)temp1).get("text").toString()).concat("\n"));
					}
					temp1++;
				}
				if (temp3.length() > 0) {
					temp3 = temp3.substring((int)(0), (int)(temp3.length() - 1));
				}
				if (_position == 0) {
					((ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(ClipData.newPlainText("clipboard", temp3));
					SketchwareUtil.showMessage(getApplicationContext(), "copied to clipboard!");
					_reset_spinner();
				}
				else {
					if (_position == 1) {
						i1 = new Intent(android.content.Intent.ACTION_SEND);
						i1.setType("text/plain");
						i1.putExtra(android.content.Intent.EXTRA_SUBJECT, "My list");
						i1.putExtra(android.content.Intent.EXTRA_TEXT, temp3);
						startActivity(i1);
						_reset_spinner();
					}
					else {
						if (_position == 2) {
							d.setTitle("Delete this list?");
							d.setMessage(all_lists.get((int)position).get("title").toString());
							d.setPositiveButton("delete", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									all_lists.remove((int)(position));
									f.edit().putString("all lists", new Gson().toJson(all_lists)).commit();
									((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
									_reset_spinner();
								}
							});
							d.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface _dialog, int _which) {
									_reset_spinner();
								}
							});
							d.create().show();
						}
						else {
							_reset_spinner();
							return;
						}
					}
				}
			}
			
			@Override
			public void onNothingSelected(AdapterView<?> _param1) {
				
			}
		});
		
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				i.setClass(getApplicationContext(), TodoListActivity.class);
				i.putExtra("item No.", String.valueOf((long)(_position)));
				startActivity(i);
			}
		});
		
		listview1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> _param1, View _param2, int _param3, long _param4) {
				final int _position = _param3;
				position = _position;
				spinner.performClick();
				return true;
			}
		});
		
		_fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				i.setClass(getApplicationContext(), TodoListActivity.class);
				i.putExtra("item No.", "-1");
				startActivity(i);
			}
		});
	}
	private void initializeLogic() {
		spinnerOptions.add("copy");
		spinnerOptions.add("share");
		spinnerOptions.add("delete");
		spinnerOptions.add("cancel");
		spinner.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item, spinnerOptions));
		spinner.setGravity(Gravity.CENTER_HORIZONTAL);
		_reset_spinner();
		if (f.getString("tutorial", "").equals("")) {
			f.edit().putString("tutorial", "done").commit();
			_show_tutorial();
		}
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
	public void onResume() {
		super.onResume();
		if (f.getString("all lists", "").equals("")) {
			f.edit().putString("all lists", "[]").commit();
		}
		if (f.getString("lines count", "").equals("")) {
			f.edit().putString("lines count", "5").commit();
		}
		if (f.getString("character limit", "").equals("")) {
			f.edit().putString("character limit", "100").commit();
		}
		all_lists = new Gson().fromJson(f.getString("all lists", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		lines_count = Double.parseDouble(f.getString("lines count", ""));
		character_limit = Double.parseDouble(f.getString("character limit", ""));
		listview1.setAdapter(new Listview1Adapter(all_lists));
	}
	
	@Override
	public void onPause() {
		super.onPause();
		f.edit().putString("all lists", new Gson().toJson(all_lists)).commit();
	}
	private void _cut_after_n_lines (final String _string, final double _n) {
		if (_n == 0) {
			CAnL_return = "...";
		}
		else {
			int pos=_string.indexOf("\n"), n=(int)_n;
			while (--n > 0 && pos != -1) {
				pos = _string.indexOf("\n",pos+1);
			}
			temp1 = pos;
			if (temp1 == -1) {
				CAnL_return = _string;
			}
			else {
				CAnL_return = _string.substring((int)(0), (int)(temp1));
				CAnL_return = CAnL_return.concat("\n...");
			}
		}
	}
	
	
	private void _paintView (final View _view, final double _id) {
		if (_id == 0) {
			_view.setBackgroundColor(0xFFFFFFFF);
			return;
		}
		if (_id == 1) {
			_view.setBackgroundColor(0xFFF44336);
			return;
		}
		if (_id == 2) {
			_view.setBackgroundColor(0xFFBA68C8);
			return;
		}
		if (_id == 3) {
			_view.setBackgroundColor(0xFF7986CB);
			return;
		}
		if (_id == 4) {
			_view.setBackgroundColor(0xFF42A5F5);
			return;
		}
		if (_id == 5) {
			_view.setBackgroundColor(0xFF4CAF50);
			return;
		}
		if (_id == 6) {
			_view.setBackgroundColor(0xFFFFEE58);
			return;
		}
		if (_id == 7) {
			_view.setBackgroundColor(0xFFFFCA28);
			return;
		}
		if (_id == 8) {
			_view.setBackgroundColor(0xFFFF9800);
			return;
		}
		if (_id == 9) {
			_view.setBackgroundColor(0xFFA1887F);
			return;
		}
		if (_id == 10) {
			_view.setBackgroundColor(0xFF78909C);
			return;
		}
	}
	
	
	private void _refreshListView () {
		((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
	}
	
	
	private void _show_tutorial () {
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
								d3.setNegativeButton("show me also next time", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface _dialog, int _which) {
										f.edit().remove("tutorial").commit();
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
	
	
	private void _reset_spinner () {
		position = -1;
		spinner.setSelection((int)(spinnerOptions.size() - 1));
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
		public View getView(final int _position, View _view, ViewGroup _viewGroup) {
			LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View _v = _view;
			if (_v == null) {
				_v = _inflater.inflate(R.layout.item_list, null);
			}
			
			final LinearLayout linear_all = (LinearLayout) _v.findViewById(R.id.linear_all);
			final LinearLayout linear2 = (LinearLayout) _v.findViewById(R.id.linear2);
			final TextView summary = (TextView) _v.findViewById(R.id.summary);
			final Button expand_b = (Button) _v.findViewById(R.id.expand_b);
			final TextView title = (TextView) _v.findViewById(R.id.title);
			
			_paintView(linear_all, Double.parseDouble(_data.get((int)_position).get("color").toString()));
			expand_b.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					temp2 = _data.get((int)_position);
					if (expand_b.getRotation() == 0) {
						temp2.put("expand", "true");
					}
					else {
						temp2.put("expand", "false");
					}
					_data.remove((int)(_position));
					_data.add((int)_position, temp2);
					_refreshListView();
				}
			});
			title.setText(_data.get((int)_position).get("title").toString());
			todo_list = new Gson().fromJson(_data.get((int)_position).get("list").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
			summary.setText("");
			index = 0;
			for(int _repeat24 = 0; _repeat24 < (int)(todo_list.size()); _repeat24++) {
				if (todo_list.get((int)index).get("done").toString().equals("true")) {
					summary.setText(summary.getText().toString().concat("✓ ".concat(todo_list.get((int)index).get("text").toString()).concat("\n")));
				}
				else {
					summary.setText(summary.getText().toString().concat("× ".concat(todo_list.get((int)index).get("text").toString()).concat("\n")));
				}
				index++;
			}
			if (summary.getText().toString().length() > 0) {
				summary.setText(summary.getText().toString().substring((int)(0), (int)(summary.getText().toString().length() - 1)));
			}
			if (_data.get((int)_position).get("expand").toString().equals("true")) {
				expand_b.setRotation((float)(180));
			}
			else {
				if (summary.getText().toString().length() > character_limit) {
					summary.setText(summary.getText().toString().substring((int)(0), (int)(character_limit)).concat("..."));
				}
				if (!(lines_count == 16)) {
					_cut_after_n_lines(summary.getText().toString(), lines_count);
					summary.setText(CAnL_return);
				}
				expand_b.setRotation((float)(0));
			}
			
			return _v;
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
