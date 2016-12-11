package com.jibu.app.fragment;

import com.jibu.app.R;
import com.jibu.app.database.CejuService;
import com.jibu.app.entity.User;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TraceListFragment extends ListFragment {

	Cursor mCursor;
	
	CejuService cejuService;
	User user;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	
	
	private static class TraceCursorAdapter extends CursorAdapter {

		
		public TraceCursorAdapter(Context context, Cursor c) {
			super(context, c, 0);
		}

		@Override
		public void bindView(View arg0, Context arg1, Cursor arg2) {
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup root) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			return inflater.inflate(R.layout.item_wifi, root, false);
		}

	}

}
