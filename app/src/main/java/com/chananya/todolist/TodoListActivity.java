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
import java.util.HashMap;
import java.util.ArrayList;
import android.widget.LinearLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.View;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class TodoListActivity extends AppCompatActivity {
	
	
	private HashMap<String, Object> todo_item = new HashMap<>();
	private HashMap<String, Object> info = new HashMap<>();
	private boolean in_update_mode = false;
	private double current_update_index = 0;
	
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
		f = getSharedPreferences("data", Activity.MODE_PRIVATE);
		
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
					oa1.setFloatValues((float)(0), (float)(SketchwareUtil.getDip(getApplicationContext(), (int)(-64))));
					oa1.setDuration((int)(300));
					oa1.setInterpolator(new DecelerateInterpolator());
					oa1.start();
				}
				else {
					oa1.setTarget(colors_hs);
					oa1.setPropertyName("translationY");
					oa1.setFloatValues((float)(SketchwareUtil.getDip(getApplicationContext(), (int)(-64))), (float)(0));
					oa1.setDuration((int)(300));
					oa1.setInterpolator(new DecelerateInterpolator());
					oa1.start();
				}
			}
		});
		
		new_item.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				// checking for 'Enter' pressing, and saving the item without the \n
				if (_charSeq.contains("\n")) {
					todo_item = new HashMap<>();
					todo_item.put("text", _charSeq.substring((int)(0), (int)(_charSeq.indexOf("\n"))));
					todo_item.put("done", "false");
					todo_list.add(todo_item);
					if (_charSeq.length() > (_charSeq.indexOf("\n") + 1)) {
						new_item.setText(_charSeq.substring((int)(_charSeq.indexOf("\n") + 1), (int)(_charSeq.length())));
					}
					else {
						new_item.setText("");
					}
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					listview1.smoothScrollToPosition((int)(todo_list.size()));
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
				todo_item.put("text", new_item.getText().toString());
				todo_item.put("done", "false");
				todo_list.add(todo_item);
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				new_item.setText("");
				listview1.smoothScrollToPosition((int)(todo_list.size()));
			}
		});
		
		cancel_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				_set_update_mode_to(false);
			}
		});
		
		edittext_update.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
				final String _charSeq = _param1.toString();
				// checking for 'Enter' pressing, and saving the item without the \n
				if (_charSeq.contains("\n")) {
					todo_item = new HashMap<>();
					todo_item.put("text", _charSeq.replace("\n", ""));
					todo_item.put("done", todo_list.get((int)current_update_index).get("done").toString());
					todo_list.remove((int)(current_update_index));
					todo_list.add((int)current_update_index, todo_item);
					edittext_update.setText("");
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					listview1.smoothScrollToPosition((int)(todo_list.size()));
					if ((current_update_index + 1) < todo_list.size()) {
						current_update_index++;
						_set_update_string(todo_list.get((int)current_update_index).get("text").toString());
					}
					else {
						_set_update_mode_to(false);
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
				todo_list.get((int)current_update_index).put("text", edittext_update.getText().toString());
				_set_update_mode_to(false);
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
		});
		
		imageview0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "0");
				_paintView(linear_all, 0);
			}
		});
		
		imageview1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "1");
				_paintView(linear_all, 1);
			}
		});
		
		imageview2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "2");
				_paintView(linear_all, 2);
			}
		});
		
		imageview3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "3");
				_paintView(linear_all, 3);
			}
		});
		
		imageview4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "4");
				_paintView(linear_all, 4);
			}
		});
		
		imageview5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "5");
				_paintView(linear_all, 5);
			}
		});
		
		imageview6.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "6");
				_paintView(linear_all, 6);
			}
		});
		
		imageview7.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "7");
				_paintView(linear_all, 7);
			}
		});
		
		imageview8.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "8");
				_paintView(linear_all, 8);
			}
		});
		
		imageview9.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "9");
				_paintView(linear_all, 9);
			}
		});
		
		imageview10.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _view) {
				info.put("color", "10");
				_paintView(linear_all, 10);
			}
		});
	}
	private void initializeLogic() {
		all_lists = new Gson().fromJson(f.getString("all lists", ""), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		if (getIntent().getStringExtra("item No.").equals("-1")) {
			info = new HashMap<>();
			info.put("title", "");
			info.put("color", "0");
			info.put("expand", "false");
			info.put("list", "[]");
			todo_list.clear();
		}
		else {
			info = all_lists.get((int)Double.parseDouble(getIntent().getStringExtra("item No.")));
			title.setText(info.get("title").toString());
			_paintView(linear_all, Double.parseDouble(info.get("color").toString()));
			todo_list = new Gson().fromJson(info.get("list").toString(), new TypeToken<ArrayList<HashMap<String, Object>>>(){}.getType());
		}
		listview1.setAdapter(new Listview1Adapter(todo_list));
		current_update_index = -1;
		_set_update_mode_to(false);
		title.requestFocus();
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
		info.put("title", title.getText().toString());
		info.put("list", new Gson().toJson(todo_list));
		if (getIntent().getStringExtra("item No.").equals("-1")) {
			all_lists.add(info);
		}
		else {
			all_lists.remove((int)(Double.parseDouble(getIntent().getStringExtra("item No."))));
			all_lists.add((int)Double.parseDouble(getIntent().getStringExtra("item No.")), info);
		}
		f.edit().putString("all lists", new Gson().toJson(all_lists)).commit();
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
	
	
	private void _set_update_mode_to (final boolean _mode) {
		if (_mode) {
			in_update_mode = true;
			linear_add.setVisibility(View.GONE);
			linear_update_item.setVisibility(View.VISIBLE);
			new_item.setText("");
			oa1.setTarget(linear_update_item);
			oa1.setPropertyName("translationY");
			oa1.setFloatValues((float)(-600), (float)(0));
			oa1.setDuration((int)(350));
			oa1.setInterpolator(new DecelerateInterpolator());
			oa1.start();
			edittext_update.requestFocus();
		}
		else {
			in_update_mode = false;
			current_update_index = -1;
			linear_add.setVisibility(View.VISIBLE);
			linear_update_item.setVisibility(View.GONE);
			edittext_update.setText("");
			new_item.requestFocus();
		}
	}
	
	
	private void _set_update_string (final String _string) {
		edittext_update.setText(_string);
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
				_v = _inflater.inflate(R.layout.todo_item, null);
			}
			
			final LinearLayout linear1 = (LinearLayout) _v.findViewById(R.id.linear1);
			final CheckBox done_cb = (CheckBox) _v.findViewById(R.id.done_cb);
			final EditText edittext = (EditText) _v.findViewById(R.id.edittext);
			final ImageView delete_item_iv = (ImageView) _v.findViewById(R.id.delete_item_iv);
			
			if (_data.get((int)_position).get("done").toString().equals("true")) {
				done_cb.setChecked(true);
			}
			else {
				done_cb.setChecked(false);
			}
			edittext.setText(_data.get((int)_position).get("text").toString());
			edittext.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					current_update_index = _position;
					_set_update_mode_to(true);
					_set_update_string(_data.get((int)_position).get("text").toString());
				}
			});
			delete_item_iv.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					todo_list.remove((int)(_position));
					if (current_update_index == _position) {
						_set_update_mode_to(false);
					}
					if (current_update_index > _position) {
						current_update_index--;
						_set_update_string(todo_list.get((int)current_update_index).get("text").toString());
					}
					_refreshListView();
				}
			});
			done_cb.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					todo_item = todo_list.get((int)_position);
					if (done_cb.isChecked()) {
						todo_item.put("done", "true");
					}
					else {
						todo_item.put("done", "false");
					}
					todo_list.remove((int)(_position));
					todo_list.add((int)_position, todo_item);
				}
			});
			
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
