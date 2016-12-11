package com.jibu.app.main;

import java.io.File;

import com.jibu.app.R;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileSelectActivity extends Activity implements
		OnItemClickListener
{
	private ListView mListView;
	private ListAdapter mAdapter;
	private String currtPathString = Environment.getExternalStorageDirectory().getPath();
	private File[] mFiles = null;
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_select);
		mListView = (ListView)findViewById(R.id.file_select_list);
		mAdapter = new ListAdapter();
		File tmpFile = new File(currtPathString+"/");
		mFiles = null;
		mFiles = tmpFile.listFiles();
		mAdapter.setFiles(mFiles);
		mListView.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
		mListView.setOnItemClickListener(this);
		mTextView = (TextView)findViewById(R.id.file_select_display_path);
		mTextView.setText(currtPathString+"/");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

	public class ListAdapter extends BaseAdapter
	{
		private File[] mAdapterFiles;

		public ListAdapter()
		{

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			// TODO Auto-generated method stub
			ListViewHolder mHolder;
			if(convertView == null)
			{
				LayoutInflater inflater = FileSelectActivity.this
						.getLayoutInflater();
				convertView = inflater.inflate(R.layout.file_list_item,null);
				mHolder = new ListViewHolder();
				mHolder.icon = (ImageView)convertView
						.findViewById(R.id.file_icon);
				mHolder.name = (TextView)convertView
						.findViewById(R.id.file_text);
				convertView.setTag(mHolder);
			} else
			{
				mHolder = (ListViewHolder)convertView.getTag();
			}
			if(position == 0)
			{
				mHolder.icon
						.setBackgroundResource(R.drawable.filedialog_folder_up);
				mHolder.name.setText("..");
			} else
			{
				if(mAdapterFiles[position-1].isFile())
				{
					mHolder.icon
							.setBackgroundResource(R.drawable.filedialog_files);
				} else
				{
					mHolder.icon
							.setBackgroundResource(R.drawable.filedialog_folder);
				}
				mHolder.name.setText("" + mAdapterFiles[position-1].getName());
			}

			return convertView;
		}

		public void setFiles(File[] files)
		{
			mAdapterFiles = files;
		}

		public boolean isFile(int position)
		{
			return mAdapterFiles[position].isFile();
		}

		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return mAdapterFiles.length+1;
		}

		@Override
		public Object getItem(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position)
		{
			// TODO Auto-generated method stub
			return position;
		}
	}

	public class ListViewHolder
	{
		public ImageView icon;
		public TextView name;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id)
	{
		// TODO Auto-generated method stub
		Intent data = new Intent();
		if(position == 0)
		{
			if(currtPathString.equals(Environment.getExternalStorageDirectory().getPath()))
			{
				setResult(RESULT_OK,data);
				finish();
			} else
			{
				currtPathString = currtPathString.substring(0,
						currtPathString.lastIndexOf("/"));
				Log.i("zznkey","onItemClick:currtPathString=" + currtPathString);
				File tmpFile = new File(currtPathString+"/");
				mFiles = tmpFile.listFiles();
				mAdapter.setFiles(mFiles);
				mAdapter.notifyDataSetChanged();
			}
		} else
		{
			if(mAdapter.isFile(position-1))
			{
				data.putExtra("selectfile",mFiles[position-1].getPath());
				setResult(RESULT_OK,data);
				finish();
			} else
			{
				currtPathString = mFiles[position-1].getPath();
				Log.i("zznkey","currtPathString=" + currtPathString);
				File tmpFile = new File(currtPathString+"/");
				mFiles = tmpFile.listFiles();
				if(mFiles!=null)
				{
					mAdapter.setFiles(mFiles);
					mAdapter.notifyDataSetChanged();
				}else {
					currtPathString = currtPathString.substring(0,
							currtPathString.lastIndexOf("/"));	
					tmpFile = new File(currtPathString+"/");
					mFiles = tmpFile.listFiles();
				}
			}
		}
		mTextView.setText(currtPathString+"/");
	}
	
	@Override
	protected void onPause() {
		super.onStop();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
}
