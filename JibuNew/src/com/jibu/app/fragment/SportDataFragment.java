package com.jibu.app.fragment;



import java.util.Calendar;
import java.util.Vector;
import java.util.zip.Inflater;

import com.gelitenight.waveview.sample.SlidingUpPanelLayout;
import com.gelitenight.waveview.sample.WaveHelper;
import com.gelitenight.waveview.sample.SlidingUpPanelLayout.PanelSlideListener;
import com.gelitenight.waveview.sample.SlidingUpPanelLayout.PanelState;
import com.gelitenight.waveview.sample.WaveView;
import com.gelitenight.waveview.sample.WaveView2;
import com.jibu.app.R;
import com.jibu.app.entity.HuanSuanUtil;
import com.jibu.app.entity.MoveData;
import com.jibu.app.entity.SportEvent;
import com.jibu.app.entity.User;
import com.jibu.app.main.MainActivity;
import com.jibu.app.main.MainActivity.MainSlideupChangeListener;
import com.jibu.app.view.RoundProgressBar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SportDataFragment extends Fragment implements MainSlideupChangeListener{
	private final String TAG = "SportDataFragment";
	
	private final static String PANEL_STATE = "PanelState"; 
	private final static String FRAGMENT_POSITION = "fragment_position";
	private final static String MOVE_DATA = "MoveData";
	private final int MAX_SLIDING_SPEED = 10000;
	private int step = 0;
	private int position = -1;
	private MoveData moveData;
	SlidingUpPanelLayout slidingUpPanelLayout;
	
	PanelState panelState;
	
	View v;
	
	ListView listView;
	private EventViewAdapter mAdapter;
	
    private int mBorderColor = Color.parseColor("#00ff00");
    private int mBorderWidth = 0;
//	private WaveHelper waveHelper;
	private WaveView2 waveView;
	
	private TextView textview_move_time, textview_no_move_time, 
						textview_move_time_bfb, textview_no_move_time_bfb;
	private TextView textview_sport_stastic;
	private boolean isPrepared;
	private boolean isSliding;
	
	RoundProgressBar roundProgressBar;
	
	
	private final Handler mHandler = new Handler();
	private final Runnable mRunnable = new Runnable(){
		
		@Override
		public void run() {
			try {
				new InitListViewTask().execute(position);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	};
	public static SportDataFragment newInstance(MoveData data, int position, PanelState PanelState)
	{
		SportDataFragment sdf = new SportDataFragment();
        Bundle args = new Bundle();
//        args.putSerializable(MOVE_DATA, data);
        args.putInt(FRAGMENT_POSITION, position);
//        args.putSerializable(PANEL_STATE, PanelState);
        sdf.setArguments(args);
		return sdf;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		moveData = getArguments() != null ?(MoveData)getArguments().getSerializable(MOVE_DATA) : null;
		position = getArguments() != null ?(int) getArguments().getInt(FRAGMENT_POSITION): 0;
//		panelState = getArguments() != null ? (PanelState) getArguments().getSerializable(PANEL_STATE): PanelState.COLLAPSED ;
		panelState = ((MainActivity)getActivity()).panelState;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		v = inflater.inflate(R.layout.activity_main_slideup, container, false);
		v.setTag(position);
			initView(v);
			isPrepared = false;
			if (((MainActivity)getActivity()).NUM_ITEMS -1 == position) {
				moveData = ((MainActivity)getActivity()).getPositionMoveData(position);
				update(); 
			}
		return v;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((MainActivity) getActivity()).setFragmentListener(this);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e(TAG, System.currentTimeMillis() +"currentTime onResume");
	}

	@Override
	public void onDestroyView() {
		((MainActivity) getActivity()).removeFragmentListener(this);
		mHandler.removeCallbacks(mRunnable);
		super.onDestroyView();
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
	
	private void initView(View v) {
		if (null == v) {
			return ;
		}
		roundProgressBar = (RoundProgressBar) v.findViewById(R.id.id_roundProgressBar);
		slidingUpPanelLayout = (SlidingUpPanelLayout) v.findViewById(R.id.sliding_layout);
		textview_move_time = ((TextView) v.findViewById(R.id.id_textview_move_time_total));
		textview_no_move_time = ((TextView) v.findViewById(R.id.id_textview_no_move_time_total));
		textview_move_time_bfb =((TextView) v.findViewById(R.id.id_textview_move_time_bfb));
		textview_no_move_time_bfb = ((TextView) v.findViewById(R.id.id_textview_no_move_time_bfb));
		waveView = (WaveView2) v.findViewById(R.id.id_waveview);
		listView =(ListView) v.findViewById(R.id.id_list);
		
		/**init slide View*/
		slidingUpPanelLayout.setPanelSlideListener((PanelSlideListener) getActivity());
		slidingUpPanelLayout.setPanelState(panelState);
		slidingUpPanelLayout.setPositon(position);
		slidingUpPanelLayout.setDragView(R.id.id_linearlayout_drawview);
		
		/**init waveView*/
		waveView.setBorder(mBorderWidth, mBorderColor);
	    waveView.setShapeType(WaveView2.ShapeType.SQUARE);
	    waveView.setWaveColor(Color.parseColor("#87CEFA"), Color.WHITE);
//	    waveHelper = new WaveHelper(waveView);
	    
	    /**init statics view*/
	    textview_sport_stastic = (TextView) v.findViewById(R.id.id_textview_sport_stastic_value);
	    
	    
	}
	
	private void updateUI() {
		if (null == moveData) return;
		/**  init Progressbar   **/
		User user = ((MainActivity)getActivity()).user;
		if (user == null) {
			throw new IllegalArgumentException("initSportsDataFragment wrong == user is null");
		}
		int todayTarget = moveData.todayTarget > 0 ? moveData.todayTarget : user.step; 
		roundProgressBar.setMax(todayTarget);
//		roundProgressBar.setProgress(moveData.getStep());
		roundProgressBar.beginAnimation(step, moveData.getStep());
		step = moveData.getStep();
		
//		if(moveData.getYMD().equals(getCurrDay())){
			roundProgressBar.setTextTop(getString(R.string.today_step));
//		}else{
//			roundProgressBar.setTextTop("");
//		}
		roundProgressBar.setTextBottom(getString(R.string.target)+":" + todayTarget);
		/*********        *********/
		
		
		/**totoal time View*/
//		textview_move_time.setText(moveData.getMoveTime() + getActivity().getString(R.string.hour));
		textview_move_time.setText(moveData.getStep() + getActivity().getString(R.string.step_unit));
		textview_no_move_time.setText(moveData.getNoMoveTime() + getActivity().getString(R.string.hour));
		textview_move_time_bfb.setText(moveData.getMoveBFB()+"");
		textview_no_move_time_bfb.setText(moveData.getNoMoveBFB()+"" );
		
		/**当天活动统计*/
		String str = getActivity().getString(R.string.sport_stastic_value);	
		int step = moveData.getStep();
		textview_sport_stastic.setText(String.format(str, 
				step, HuanSuanUtil.getLengthByStep(step), HuanSuanUtil.getCalByStep(step), moveData.getNoMoveIndex()));
//		
//		/**init WaveView*/
	    waveView.setBFB(moveData.getMoveBFB()/100.0f);
//	    
		/**initListView*/
	    
		Log.e(TAG, System.currentTimeMillis() +"initListView begin");
		Vector<SportEvent> sportEvents = moveData.getSportEvent();
		initListView(sportEvents);
		Log.e(TAG, System.currentTimeMillis() +"initListView end");
		
	}
	
	private void initListView(Vector<SportEvent> sportEvents) {
		if(null == v || sportEvents == null) {
			return ;
		}
		
		if (mAdapter == null) {
			mAdapter = new EventViewAdapter(getActivity());
			listView.setAdapter(mAdapter);
		}
		listView.setDividerHeight(0);
//		listView.addHeaderView(getHeader(getActivity()));
//		Log.e(TAG, System.currentTimeMillis() +"currentTime");
		/*这些操作可以异步进行，提升效率,异步好像没有卵用*/
		mAdapter.clearAllDevieceItem();
		mAdapter.addAllItem(sportEvents);
//		listView.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				LinearLayout ll = (LinearLayout) view.findViewById(R.id.id_linearlayout_bottom);
//				if (ll != null ) {
//					if (((SportEvent)mAdapter.getItem(position)).getType() < MoveData.S_SPORT) {
//						return;
//					}
//					if (ll.getVisibility() == View.GONE) {
//						ll.setVisibility(View.VISIBLE);
//					} else {
//						ll.setVisibility(View.GONE);
//					}
//				}
//			}
//		});
		
	}
	
	/**
	 * 
	 * */
	public String getCurrDay(){
		int nowYear = Calendar.getInstance().get(Calendar.YEAR);
		int nowMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		String sNowMonth;
		if(nowMonth<10){
			sNowMonth = "0"+nowMonth;
		}else{
			sNowMonth = ""+nowMonth;
		}
		 
		int nowDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		
		String sNowDay;
		if(nowDay<10){
			sNowDay = "0"+nowDay;
		}else{
			sNowDay = ""+nowDay;
		}
		String value = nowYear+"/"+sNowMonth+"/"+sNowDay;
		Log.i("log","day="+value);
		return value;
	}
	
	public PanelState getPanelState() {
		return slidingUpPanelLayout.getPanelState();
	}
	
	public void setPanelState(PanelState panelState) {
		if (slidingUpPanelLayout != null) {
			slidingUpPanelLayout.setPanelState(panelState);
		}
	}
	


	public int getPosition() {
		return position;
	}

	@Override
	public void onCurrentViewChangeState(PanelState panelState) {
		setPanelState(panelState);
		isSliding = false;
	}

	@Override
	public void onCurrentViewSlide(float slideOffset) {
		if (!isSliding) {
			isSliding = true;
		}
		if (slideOffset >  0.6) slideOffset = 1;
		if (slideOffset <  0.4) slideOffset = 0;
		if (slidingUpPanelLayout != null) {
			slidingUpPanelLayout.smoothSlideTo(slideOffset, MAX_SLIDING_SPEED);
		}
	}

	/*同步数据结束后更新数据*/
	
	@Override
	public void onShowDataChange() {
		/**
		 * 数据有变化才改变视图数据
		 */
		if (!isPrepared) return;
//		if (!moveData.equals(this.moveData) && null != v) {
//			Log.e(TAG, "true");
//			this.moveData = moveData;
//			updateUI();
//			isPrepared = true;
//		}
		new InitListViewTask().execute(position);
	}
	
    public void loadData() {
    	if (!isPrepared) {
    		new InitListViewTask().execute(position);
    	}
    }
    
	public void update() {
		if (!isPrepared) {
			updateUI();
			isPrepared = true;
		}
	}

	class InitListViewTask extends AsyncTask<Integer, Integer, MoveData> {

		@Override
		protected MoveData doInBackground(Integer... params) {
			Log.e(TAG, "params[0] = " + params[0]);
			
			MoveData moveData = ((MainActivity)getActivity()).getPositionMoveData(params[0]);
			return moveData;
		}

		/**数据更新才更新视图*/
		@Override
		protected void onPostExecute(MoveData result) {
			if (result != null && !result.equals(moveData)) {
				moveData = result;
				updateUI();
				isPrepared = true;
			}
		}
	}
}
