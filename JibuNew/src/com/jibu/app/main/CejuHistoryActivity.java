package com.jibu.app.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import com.jibu.app.R;
import com.jibu.app.database.CejuService;
import com.jibu.app.entity.CejuData;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CejuHistoryActivity extends Activity {

	private final String TAG = "CejuHistoryActivity";
	
	ListView listViewCejuHistory;
	
	MainApplication mainApplication;
	
	User user;
	
	CejuService cejuService;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm");
	
	boolean inSeletMode = false;
	boolean isInEdit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_cejuhistory);
		mainApplication = (MainApplication) this.getApplication();
		mainApplication.addActivity(this);
		
		if (null == mainApplication || null == mainApplication.user) {
			WelcomeActivity.gotoActivity(this);
			this.finish();
		} else {
			user = mainApplication.user;
			cejuService = new CejuService(CejuHistoryActivity.this);
			initView();
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	private void initView() {
		listViewCejuHistory = (ListView) findViewById(R.id.id_listview_ceju_history);
		
		CejuListViewAdapter Adapter = new CejuListViewAdapter(CejuHistoryActivity.this);
		
		listViewCejuHistory.setAdapter(Adapter);
		
		findViewById(R.id.id_linearlayout_title_left).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CejuHistoryActivity.this.finish();
			}
		});
//		registerForContextMenu(listViewCejuHistory);
//		listViewCejuHistory.setOnItemLongClickListener(Adapter);
//		listViewCejuHistory.setOnItemClickListener(Adapter);
		
	}
	
	public class CejuListViewAdapter extends BaseAdapter {
		

		public Vector<CejuData> cejuDatas = null;
		
		Context mContext;
		
		private boolean seletedMode;
		
		public CejuListViewAdapter(Context context) {
			mContext = context;
			cejuDatas = cejuService.queryAllCejuData(user.userId);
			if (cejuDatas == null) {
				cejuDatas = new Vector<CejuData>();
			}
			seletedMode = false;
		}
		
		@Override
		public int getCount() {
			return cejuDatas.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			final CejuData cejuData = cejuDatas.elementAt(position);
			final int position2 = position;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.item_cejuhistory, null);
				holder.textViewTimestamp = (TextView) convertView.findViewById(R.id.id_textview_timestamp);
				holder.textViewStep = (TextView) convertView.findViewById(R.id.id_textview_step);
				holder.buttonCancel = (Button)   convertView.findViewById(R.id.id_button_cancel);
				holder.buttonEdit	= (Button)	 convertView.findViewById(R.id.id_button_edit);
				holder.editText		= (EditText) convertView.findViewById(R.id.id_edittext_info);
				holder.checkbox		= (CheckBox) convertView.findViewById(R.id.id_checkBox_selected);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder != null) {
				float buchang = HuanSuanUtil.getBuchang(user.height*100)/100;
				float juli    = buchang * cejuData.step;
				
				holder.textViewTimestamp.setText(longToDate(cejuData.timestamp));
				
				holder.textViewStep.setText(getString(R.string.history_distance, 
						juli, cejuData.step, cejuData.duration/60, cejuData.duration%60));
				
				holder.editText.setText(cejuData.info);
				holder.editText.setInputType(InputType.TYPE_NULL);
				holder.buttonEdit.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						Button b = (Button) v;
//						if (b.getText().toString().equals(getString(R.string.edit))) {
							//按钮显示的是编辑
//							b.setText(getString(R.string.confirm));
//							holder.buttonCancel.setText(getString(R.string.cancel));
//							holder.editText.setInputType(InputType.TYPE_CLASS_TEXT);
//							holder.editText.setBackgroundColor(getResources().getColor(R.color.cejutext_bg));
//							InputMethodManager inputManager =  
//					                 (InputMethodManager)holder.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);  
//					             inputManager.showSoftInput(holder.editText, 0);  
//						} else { 
							//按钮显示的是确定  更新info
//							updateItem(position2, cejuData, holder.editText.getText().toString());
//							b.setText(getString(R.string.edit));
//							holder.buttonCancel.setText(getString(R.string.delete));
//							holder.editText.setInputType(InputType.TYPE_NULL);
//							holder.editText.setBackgroundColor(getResources().getColor(R.color.transparent));
//						}
						fixCejuDialog(mContext, position2, cejuData);
					}
				});
				
				holder.buttonCancel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {	
						
//						Button b = (Button) v;
//						if (b.getText().toString().equals(getString(R.string.delete))) {
//							//按钮显示的是删除
						showConfirmDeleteDailog(mContext, position2, cejuData);
//							deleteItem(position2, cejuData);
//							holder.editText.setText(cejuData.info);
//						} else { 
//							//按钮显示的是取消 
//							b.setText(getString(R.string.delete));
//							holder.buttonEdit.setText(getString(R.string.edit));
//							holder.editText.setInputType(InputType.TYPE_NULL);
//							holder.editText.setBackgroundColor(getResources().getColor(R.color.transparent));
//						}
					}
					
				});
				

				
			} 
			return convertView;
		}
		private void updateItem(int position, CejuData cejuData, String textInfo) {
			if(cejuDatas.size() > position) {
				cejuDatas.remove(position);
				CejuData data = new CejuData(cejuData.userId, cejuData.timestamp, cejuData.step, cejuData.duration, textInfo);
				cejuService.insertCejuData(data);
				cejuDatas.insertElementAt(data, position);
				this.notifyDataSetChanged();
			}

		}
		
		private void deleteItem(int position , CejuData cejuData) {
			if(cejuDatas.size() > position) {
				cejuDatas.remove(position);
				this.notifyDataSetChanged();
			}
			cejuService.deletedCejuData(user.userId, cejuData.timestamp);
		}

//		@Override
//		public boolean onItemLongClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			Log.e(TAG, "ONItemlongClick");
//			seletedMode = true;
//			this.notifyDataSetChanged();
//			return false;
//		}
//		
		private AlertDialog fixCejuDialog(Context context,final int position,final CejuData cejuData) {
			
			
			final View v = LayoutInflater.from(context)
					.inflate(R.layout.linearlayout_fix_ceju, null);
			final AlertDialog saveDailog = new AlertDialog.Builder(context).create();
			saveDailog.show();
			saveDailog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			saveDailog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
			saveDailog.setContentView(v);
			
			TextView tv  = (TextView) v.findViewById(R.id.id_textview_fix_data);
			final EditText ed  = (EditText) v.findViewById(R.id.id_edittext_fix_info);
			
			
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					updateItem(position, cejuData, ed.getText().toString());
					saveDailog.dismiss();
					
				}
			});
			
			return saveDailog;
		}
		
		private void showConfirmDeleteDailog(Context context, final int position, final CejuData cejuData) {
			
			new AlertDialog.Builder(context)
			.setMessage(R.string.confirm_delete_ceju_item)
			.setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteItem(position, cejuData);
				}
				
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

				@Override

				public void onClick(DialogInterface dialog, int which) {

					dialog.dismiss(); //关闭alertDialog

				}

			})
			.show();
		}
	}
	
	public final class ViewHolder{
		public TextView textViewTimestamp;
		public TextView textViewStep;
		public EditText editText;
		public Button buttonEdit;
		public Button buttonCancel;
		public CheckBox checkbox;
	}
	
	private String longToDate(long Time) {
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(Time);
		
		String date = sdf.format(cl.getTime());
		return date;
	}
	
    public static void gotoActivity(Context context) {
    	Intent intent = new Intent(context, CejuHistoryActivity.class);
    	context.startActivity(intent);
    }
}
