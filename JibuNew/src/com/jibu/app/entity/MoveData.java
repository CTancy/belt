package com.jibu.app.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jibu.app.R;
import com.jibu.app.database.MoveDataService;

import android.text.format.Time;
import android.util.Log;

public class MoveData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String userId;
	
	public int year;
	public int mounth;
	public int day;
	//public int week;
	
	public int time_0;
	public int time_0_5;
	
	public int time_1;
	public int time_1_5;
	
	public int time_2;
	public int time_2_5;
	
	public int time_3;
	public int time_3_5;
	
	public int time_4;
	public int time_4_5;
	
	public int time_5;
	public int time_5_5;
	
	public int time_6;
	public int time_6_5;
	
	public int time_7;
	public int time_7_5;
	
	public int time_8;
	public int time_8_5;
	
	public int time_9;
	public int time_9_5;
	
	public int time_10;
	public int time_10_5;
	
	public int time_11;
	public int time_11_5;
	
	public int time_12;
	public int time_12_5;
	
	public int time_13;
	public int time_13_5;
	
	public int time_14;
	public int time_14_5;
	
	public int time_15;
	public int time_15_5;
	
	public int time_16;
	public int time_16_5;
	
	public int time_17;
	public int time_17_5;
	
	public int time_18;
	public int time_18_5;
	
	public int time_19;
	public int time_19_5;
	
	public int time_20;
	public int time_20_5;
	
	public int time_21;
	public int time_21_5;
	
	public int time_22;
	public int time_22_5;
	
	public int time_23;
	public int time_23_5;
	
	public int todayTarget;
	
	/*定义某个时段的运动类型*/
	public final static int NO_WEAR = 0;
	public final static int NO_MOVE = 1;
	public final static int S_SPORT = 2;
	public final static int M_SPORT = 3;
	public final static int B_SPORT = 4;
	
	public MoveData(){
		
		
	}
	
	
	
	
	public MoveData(String userId, int year,int mounth,int day, int time_0, int time_0_5,
			int time_1, int time_1_5, int time_2, int time_2_5, int time_3,
			int time_3_5, int time_4, int time_4_5, int time_5, int time_5_5,
			int time_6, int time_6_5, int time_7, int time_7_5, int time_8,
			int time_8_5, int time_9, int time_9_5, int time_10, int time_10_5,
			int time_11, int time_11_5, int time_12, int time_12_5,
			int time_13, int time_13_5, int time_14, int time_14_5,
			int time_15, int time_15_5, int time_16, int time_16_5,
			int time_17, int time_17_5, int time_18, int time_18_5,
			int time_19, int time_19_5, int time_20, int time_20_5,
			int time_21, int time_21_5, int time_22, int time_22_5,
			int time_23, int time_23_5, int todayTarget) {
		super();
		this.userId = userId;
		
		this.year = year;
		this.mounth = mounth;
		this.day = day;
		
		this.time_0 = time_0;
		this.time_0_5 = time_0_5;
		this.time_1 = time_1;
		this.time_1_5 = time_1_5;
		this.time_2 = time_2;
		this.time_2_5 = time_2_5;
		this.time_3 = time_3;
		this.time_3_5 = time_3_5;
		this.time_4 = time_4;
		this.time_4_5 = time_4_5;
		this.time_5 = time_5;
		this.time_5_5 = time_5_5;
		this.time_6 = time_6;
		this.time_6_5 = time_6_5;
		this.time_7 = time_7;
		this.time_7_5 = time_7_5;
		this.time_8 = time_8;
		this.time_8_5 = time_8_5;
		this.time_9 = time_9;
		this.time_9_5 = time_9_5;
		this.time_10 = time_10;
		this.time_10_5 = time_10_5;
		this.time_11 = time_11;
		this.time_11_5 = time_11_5;
		this.time_12 = time_12;
		this.time_12_5 = time_12_5;
		this.time_13 = time_13;
		this.time_13_5 = time_13_5;
		this.time_14 = time_14;
		this.time_14_5 = time_14_5;
		this.time_15 = time_15;
		this.time_15_5 = time_15_5;
		this.time_16 = time_16;
		this.time_16_5 = time_16_5;
		this.time_17 = time_17;
		this.time_17_5 = time_17_5;
		this.time_18 = time_18;
		this.time_18_5 = time_18_5;
		this.time_19 = time_19;
		this.time_19_5 = time_19_5;
		this.time_20 = time_20;
		this.time_20_5 = time_20_5;
		this.time_21 = time_21;
		this.time_21_5 = time_21_5;
		this.time_22 = time_22;
		this.time_22_5 = time_22_5;
		this.time_23 = time_23;
		this.time_23_5 = time_23_5;
		
		this.todayTarget = todayTarget;
	}


	
	public MoveData(String userId, long initTime) {
		super();
		this.userId = userId;
		
		Time time = new Time();
		time.set(initTime);
		this.year = time.year;
		this.mounth = time.month;
		this.day = time.monthDay;
		
		
		
		this.time_0 = -1;
		this.time_0_5 = -1;
		this.time_1 = -1;
		this.time_1_5 = -1;
		this.time_2 = -1;
		this.time_2_5 = -1;
		this.time_3 = -1;
		this.time_3_5 = -1;
		this.time_4 = -1;
		this.time_4_5 = -1;
		this.time_5 = -1;
		this.time_5_5 = -1;
		this.time_6 = -1;
		this.time_6_5 = -1;
		this.time_7 = -1;
		this.time_7_5 = -1;
		this.time_8 = -1;
		this.time_8_5 = -1;
		this.time_9 = -1;
		this.time_9_5 = -1;
		this.time_10 = -1;
		this.time_10_5 = -1;
		this.time_11 = -1;
		this.time_11_5 = -1;
		this.time_12 = -1;
		this.time_12_5 = -1;
		this.time_13 = -1;
		this.time_13_5 = -1;
		this.time_14 = -1;
		this.time_14_5 = -1;
		this.time_15 = -1;
		this.time_15_5 = -1;
		this.time_16 = -1;
		this.time_16_5 = -1;
		this.time_17 = -1;
		this.time_17_5 = -1;
		this.time_18 = -1;
		this.time_18_5 = -1;
		this.time_19 = -1;
		this.time_19_5 = -1;
		this.time_20 = -1;
		this.time_20_5 = -1;
		this.time_21 = -1;
		this.time_21_5 = -1;
		this.time_22 = -1;
		this.time_22_5 = -1;
		this.time_23 = -1;
		this.time_23_5 = -1;
		
		this.todayTarget = 0;
	}

	public MoveData(String userId) {
		super();
		this.userId = userId;
		
		
		this.year = -1;
		this.mounth = -1;
		this.day = -1;
	
		this.time_0 = -1;
		this.time_0_5 = -1;
		this.time_1 = -1;
		this.time_1_5 = -1;
		this.time_2 = -1;
		this.time_2_5 = -1;
		this.time_3 = -1;
		this.time_3_5 = -1;
		this.time_4 = -1;
		this.time_4_5 = -1;
		this.time_5 = -1;
		this.time_5_5 = -1;
		this.time_6 = -1;
		this.time_6_5 = -1;
		this.time_7 = -1;
		this.time_7_5 = -1;
		this.time_8 = -1;
		this.time_8_5 = -1;
		this.time_9 = -1;
		this.time_9_5 = -1;
		this.time_10 = -1;
		this.time_10_5 = -1;
		this.time_11 = -1;
		this.time_11_5 = -1;
		this.time_12 = -1;
		this.time_12_5 = -1;
		this.time_13 = -1;
		this.time_13_5 = -1;
		this.time_14 = -1;
		this.time_14_5 = -1;
		this.time_15 = -1;
		this.time_15_5 = -1;
		this.time_16 = -1;
		this.time_16_5 = -1;
		this.time_17 = -1;
		this.time_17_5 = -1;
		this.time_18 = -1;
		this.time_18_5 = -1;
		this.time_19 = -1;
		this.time_19_5 = -1;
		this.time_20 = -1;
		this.time_20_5 = -1;
		this.time_21 = -1;
		this.time_21_5 = -1;
		this.time_22 = -1;
		this.time_22_5 = -1;
		this.time_23 = -1;
		this.time_23_5 = -1;
		
		this.todayTarget = 0;
	}


	public int getWeekDay(){
		
		Calendar cl = Calendar.getInstance();
		cl.set(year, mounth, day);
		
		Log.i("log","getWeekDay:year="+year+" month="+mounth+" day="+day+" week="+cl.get(Calendar.DAY_OF_WEEK));
		
		return cl.get(Calendar.DAY_OF_WEEK);
	}
	
	public MoveData(String userId, int year,int month,int day) {
		super();
		this.userId = userId;
	
		this.year = year;
		this.mounth = month;
		this.day = day;
		this.time_0 = -1;
		this.time_0_5 = -1;
		this.time_1 = -1;
		this.time_1_5 = -1;
		this.time_2 = -1;
		this.time_2_5 = -1;
		this.time_3 = -1;
		this.time_3_5 = -1;
		this.time_4 = -1;
		this.time_4_5 = -1;
		this.time_5 = -1;
		this.time_5_5 = -1;
		this.time_6 = -1;
		this.time_6_5 = -1;
		this.time_7 = -1;
		this.time_7_5 = -1;
		this.time_8 = -1;
		this.time_8_5 = -1;
		this.time_9 = -1;
		this.time_9_5 = -1;
		this.time_10 = -1;
		this.time_10_5 = -1;
		this.time_11 = -1;
		this.time_11_5 = -1;
		this.time_12 = -1;
		this.time_12_5 = -1;
		this.time_13 = -1;
		this.time_13_5 = -1;
		this.time_14 = -1;
		this.time_14_5 = -1;
		this.time_15 = -1;
		this.time_15_5 = -1;
		this.time_16 = -1;
		this.time_16_5 = -1;
		this.time_17 = -1;
		this.time_17_5 = -1;
		this.time_18 = -1;
		this.time_18_5 = -1;
		this.time_19 = -1;
		this.time_19_5 = -1;
		this.time_20 = -1;
		this.time_20_5 = -1;
		this.time_21 = -1;
		this.time_21_5 = -1;
		this.time_22 = -1;
		this.time_22_5 = -1;
		this.time_23 = -1;
		this.time_23_5 = -1;
		
		this.todayTarget = 0;
	}


	
	
	public float getMoveLength(){
		
		return HuanSuanUtil.getLengthByStep(getStep());
	}
	
	public String getYMD(){
		int nowYear = year;
		int nowMonth = mounth+1;
		String sNowMonth;
		if(nowMonth<10){
			sNowMonth = "0"+nowMonth;
		}else{
			sNowMonth = ""+nowMonth;
		}
		
		int nowDay = day;
		
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
	
	public int getWeek(){
		int ret = -1;
		switch (getWeekDay()) {
		case 1:
			ret = R.string.sunday;
			break;
		case 2:
			ret = R.string.monday;		
			break;
		case 3:
			ret = R.string.tuesday;
			break;
		case 4:
			ret = R.string.wednesday;
			break;
		case 5:
			ret = R.string.thrusday;
			break;
		case 6:
			ret = R.string.friday;
			break;
		case 7:
			ret = R.string.saturday;
		break;
		
		}
		return ret;
	}
	
	public int getWeekDesc(){
		int ret = -1;
		switch (getWeekDay()) {
		case 1:
			ret = R.string.day;
			break;
		case 2:
			ret = R.string.one;		
			break;
		case 3:
			ret = R.string.two;
			break;
		case 4:
			ret = R.string.three;
			break;
		case 5:
			ret = R.string.four;
			break;
		case 6:
			ret = R.string.five;
			break;
			case 7:
				ret = R.string.six;
				break;
		
		}
		return ret;
	}
	
	public int getMoveCal(){
		
		return HuanSuanUtil.getCalByStep(getStep());
	}
	
	public float getMoveTime(){
		
		int minMoveStep = 0;
		
		float time = (float)0.0;
		
		
		if(time_0>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_0_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_1>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_1_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_2>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_2_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_3>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_3_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_4>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_4_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_5_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_6>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_6_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_7>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_7_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_8>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_8_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_9>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_9_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_10>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_10_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_11>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_11_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_12>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_12_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_13>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_13_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_14>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_14_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_15>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_15_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_16>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_16_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_17>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_17_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_18>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_18_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_19>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_19_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_20>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_20_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_21>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_21_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_22>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_22_5>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_23>minMoveStep){
			time = time+(float)0.5;
		}
		if(time_23_5>minMoveStep){
			time = time+(float)0.5;
		}
		
		
		
		return time;
		
	}
	
	
	public int getStep(){
		
		int step = 0;
		
		if(time_0>0){
			step = step+time_0;
		}
		if(time_0_5>0){
			step = step+time_0_5;
		}
		if(time_1>0){
			step = step+time_1;
		}
		if(time_1_5>0){
			step = step+time_1_5;
		}
		if(time_2>0){
			step = step+time_2;
		}
		if(time_2_5>0){
			step = step+time_2_5;
		}
		if(time_3>0){
			step = step+time_3;
		}
		if(time_3_5>0){
			step = step+time_3_5;
		}
		if(time_4>0){
			step = step+time_4;
		}
		if(time_4_5>0){
			step = step+time_4_5;
		}
		if(time_5>0){
			step = step+time_5;
		}
		if(time_5_5>0){
			step = step+time_5_5;
		}
		if(time_6>0){
			step = step+time_6;
		}
		if(time_6_5>0){
			step = step+time_6_5;
		}
		if(time_7>0){
			step = step+time_7;
		}
		if(time_7_5>0){
			step = step+time_7_5;
		}
		if(time_8>0){
			step = step+time_8;
		}
		if(time_8_5>0){
			step = step+time_8_5;
		}
		if(time_9>0){
			step = step+time_9;
		}
		if(time_9_5>0){
			step = step+time_9_5;
		}
		if(time_10>0){
			step = step+time_10;
		}
		if(time_10_5>0){
			step = step+time_10_5;
		}
		if(time_11>0){
			step = step+time_11;
		}
		if(time_11_5>0){
			step = step+time_11_5;
		}
		if(time_12>0){
			step = step+time_12;
		}
		if(time_12_5>0){
			step = step+time_12_5;
		}
		if(time_13>0){
			step = step+time_13;
		}
		if(time_13_5>0){
			step = step+time_13_5;
		}
		if(time_14>0){
			step = step+time_14;
		}
		if(time_14_5>0){
			step = step+time_14_5;
		}
		if(time_15>0){
			step = step+time_15;
		}
		if(time_15_5>0){
			step = step+time_15_5;
		}
		if(time_16>0){
			step = step+time_16;
		}
		if(time_16_5>0){
			step = step+time_16_5;
		}
		if(time_17>0){
			step = step+time_17;
		}
		if(time_17_5>0){
			step = step+time_17_5;
		}
		if(time_18>0){
			step = step+time_18;
		}
		if(time_18_5>0){
			step = step+time_18_5;
		}
		if(time_19>0){
			step = step+time_19;
		}
		if(time_19_5>0){
			step = step+time_19_5;
		}
		if(time_20>0){
			step = step+time_20;
		}
		if(time_20_5>0){
			step = step+time_20_5;
		}
		if(time_21>0){
			step = step+time_21;
		}
		if(time_21_5>0){
			step = step+time_21_5;
		}
		if(time_22>0){
			step = step+time_22;
		}
		if(time_22_5>0){
			step = step+time_22_5;
		}
		if(time_23>0){
			step = step+time_23;
		}
		if(time_23_5>0){
			step = step+time_23_5;
		}
		
		return step;

	}
	
	
	public float getNoMoveTime(){
		
		
		
		float time = (float)0.0;
		
		Vector<NoMoveData> NoMoveDatas = getNoMoveDatas();
		
		for(int i=0; i<NoMoveDatas.size(); i++){
			
			time = time+NoMoveDatas.elementAt(i).getNoMoveTime();
		}
		
		
		return time;
		
	}
	
	public int getNoMoveIndex(){
		
		
		
		return getNoMoveDatas().size();
		
	}
	
	
	public Vector<NoMoveData> getNoMoveDatas(){
		
		

		Vector<Integer> times = new Vector<Integer>();
		times.add(time_0);
		times.add(time_0_5);
		times.add(time_1);
		times.add(time_1_5);
		times.add(time_2);
		times.add(time_2_5);
		times.add(time_3);
		times.add(time_3_5);
		times.add(time_4);
		times.add(time_4_5);
		times.add(time_5);
		times.add(time_5_5);
		times.add(time_6);
		times.add(time_6_5);
		times.add(time_7);
		times.add(time_7_5);
		times.add(time_8);
		times.add(time_8_5);
		times.add(time_9);
		times.add(time_9_5);
		times.add(time_10);
		times.add(time_10_5);
		times.add(time_11);
		times.add(time_11_5);
		times.add(time_12);
		times.add(time_12_5);
		times.add(time_13);
		times.add(time_13_5);
		times.add(time_14);
		times.add(time_14_5);
		times.add(time_15);
		times.add(time_15_5);
		times.add(time_16);
		times.add(time_16_5);
		times.add(time_17);
		times.add(time_17_5);
		times.add(time_18);
		times.add(time_18_5);
		times.add(time_19);
		times.add(time_19_5);
		times.add(time_20);
		times.add(time_20_5);
		times.add(time_21);
		times.add(time_21_5);
		times.add(time_22);
		times.add(time_22_5);
		times.add(time_23);
		times.add(time_23_5);
		
		int firstNoMoveTime = -1;
		int twoNoMoveTime = -1;
		
		Vector<NoMoveData> noMoveDatas = new Vector<NoMoveData>();
		
		for(int i=0; i<times.size(); i++){
			
			int step = times.elementAt(i);
			
			if(step== 0){  //此时间点为久坐	
				
				if(firstNoMoveTime ==-1){
					
					firstNoMoveTime = i;

				}else if(firstNoMoveTime!=-1){
					if (i == 47 && firstNoMoveTime != 47) { //处理最后一个时间段是久坐的情况
						twoNoMoveTime = 47;
						
						NoMoveData noMoveData = new NoMoveData(firstNoMoveTime,twoNoMoveTime);
						if(twoNoMoveTime-firstNoMoveTime>0){
							noMoveDatas.add(noMoveData);
						}
						
						firstNoMoveTime = twoNoMoveTime = -1;
					}
				}

				
			}else{	//此时间点为非久坐
				
				if(firstNoMoveTime ==-1){
					
				}else if(firstNoMoveTime!=-1){
					twoNoMoveTime = i-1;
					
					NoMoveData noMoveData = new NoMoveData(firstNoMoveTime,twoNoMoveTime);
					if(twoNoMoveTime-firstNoMoveTime>0){
						noMoveDatas.add(noMoveData);
					}
					
					firstNoMoveTime = twoNoMoveTime = -1;
				}
				
			}
			
		}
		

		
		
		return noMoveDatas;
		
	}
	
	
	public Vector<Integer> getProgress(){
		
		Vector<Integer> progress = new Vector<Integer>();
		
		progress.add(time_0);
		progress.add(time_0_5);
		progress.add(time_1);
		progress.add(time_1_5);
		progress.add(time_2);
		progress.add(time_2_5);
		progress.add(time_3);
		progress.add(time_3_5);
		progress.add(time_4);
		progress.add(time_4_5);
		progress.add(time_5);
		progress.add(time_5_5);
		progress.add(time_6);
		progress.add(time_6_5);
		progress.add(time_7);
		progress.add(time_7_5);
		progress.add(time_8);
		progress.add(time_8_5);
		progress.add(time_9);
		progress.add(time_9_5);
		progress.add(time_10);
		progress.add(time_10_5);
		progress.add(time_11);
		progress.add(time_11_5);
		progress.add(time_12);
		progress.add(time_12_5);
		progress.add(time_13);
		progress.add(time_13_5);
		progress.add(time_14);
		progress.add(time_14_5);
		progress.add(time_15);
		progress.add(time_15_5);
		progress.add(time_16);
		progress.add(time_16_5);
		progress.add(time_17);
		progress.add(time_17_5);
		progress.add(time_18);
		progress.add(time_18_5);
		progress.add(time_19);
		progress.add(time_19_5);
		progress.add(time_20);
		progress.add(time_20_5);
		progress.add(time_21);
		progress.add(time_21_5);
		progress.add(time_22);
		progress.add(time_22_5);
		progress.add(time_23);
		progress.add(time_23_5);
		
		return progress;
	}
	
	public static int getTotalStep(Vector<MoveData> moveDatas){
		
		int totalStep = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				totalStep = totalStep + moveData.getStep();
			}
			
			
		}
		
		
		return totalStep;
	}
	
	
	public static float getTotalMoveTime(Vector<MoveData> moveDatas){
		
		float totalTime = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				totalTime = totalTime + moveData.getMoveTime();
			}
			
			
		}
		
		
		return totalTime;
	}
	
	public static float getTotalMoveLength(Vector<MoveData> moveDatas){
		
		float totalLength = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				totalLength = totalLength + moveData.getMoveLength();
			}
			
			
		}
		
		
		return totalLength;
	}
	
	public static int getTotalMoveCal(Vector<MoveData> moveDatas){
		
		int totalCal = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				totalCal = totalCal + moveData.getMoveCal();
			}
			
			
		}
		
		
		return totalCal;
	}
	
	public static float getTotalNoMoveTime(Vector<MoveData> moveDatas){
		
		float totalNoTime = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				totalNoTime = totalNoTime + moveData.getNoMoveTime();
			}
			
			
		}
		
		
		return totalNoTime;
	}
	
	public static int getTotalNoMoveIndex(Vector<MoveData> moveDatas){
		
		int index = 0;
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				index = index + moveData.getNoMoveIndex();
			}
			
			
		}
		
		
		return index;
	}
	
	public static Vector<Integer> getTotalProgress(Vector<MoveData> moveDatas){
		
		Vector<Integer> progress = new Vector<Integer>();
		
		if(moveDatas!=null){
			
			for(int i=0; i<moveDatas.size(); i++){
				MoveData moveData = moveDatas.elementAt(i);
				progress.add(moveData.getStep());
			}
			
			
		}
		
		
		return progress;
	}
	
	
	@Override
	public String toString() {
		return "MoveData [userId=" + userId + ", year=" + year + ", mounth="
				+ mounth + ", day=" + day +  ", time_0="
				+ time_0 + ", time_0_5=" + time_0_5 + ", time_1=" + time_1
				+ ", time_1_5=" + time_1_5 + ", time_2=" + time_2
				+ ", time_2_5=" + time_2_5 + ", time_3=" + time_3
				+ ", time_3_5=" + time_3_5 + ", time_4=" + time_4
				+ ", time_4_5=" + time_4_5 + ", time_5=" + time_5
				+ ", time_5_5=" + time_5_5 + ", time_6=" + time_6
				+ ", time_6_5=" + time_6_5 + ", time_7=" + time_7
				+ ", time_7_5=" + time_7_5 + ", time_8=" + time_8
				+ ", time_8_5=" + time_8_5 + ", time_9=" + time_9
				+ ", time_9_5=" + time_9_5 + ", time_10=" + time_10
				+ ", time_10_5=" + time_10_5 + ", time_11=" + time_11
				+ ", time_11_5=" + time_11_5 + ", time_12=" + time_12
				+ ", time_12_5=" + time_12_5 + ", time_13=" + time_13
				+ ", time_13_5=" + time_13_5 + ", time_14=" + time_14
				+ ", time_14_5=" + time_14_5 + ", time_15=" + time_15
				+ ", time_15_5=" + time_15_5 + ", time_16=" + time_16
				+ ", time_16_5=" + time_16_5 + ", time_17=" + time_17
				+ ", time_17_5=" + time_17_5 + ", time_18=" + time_18
				+ ", time_18_5=" + time_18_5 + ", time_19=" + time_19
				+ ", time_19_5=" + time_19_5 + ", time_20=" + time_20
				+ ", time_20_5=" + time_20_5 + ", time_21=" + time_21
				+ ", time_21_5=" + time_21_5 + ", time_22=" + time_22
				+ ", time_22_5=" + time_22_5 + ", time_23=" + time_23
				+ ", time_23_5=" + time_23_5 + ", totoal_target="+ todayTarget + "]";
	}
	
	public String to8date() {
		Calendar cl = Calendar.getInstance();
		cl.set(this.year, this.mounth, this.day);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(cl.getTime());
	}
	
	public String toFormatdate(String formatDateString) {
		Calendar cl = Calendar.getInstance();
		cl.set(this.year, this.mounth, this.day);
		SimpleDateFormat sdf = new SimpleDateFormat(formatDateString);
		return sdf.format(cl.getTime());
	}
	
	public Long toLongTime() {
		Calendar cl = Calendar.getInstance();
		cl.set(this.year, this.mounth, this.day);
		return cl.getTimeInMillis();
	}



	public int getTotalDay(){
		
		return year*10000+mounth*100+day;
	}
	
	
	public int getMoveBFB(){
		
		if(getMoveTime()<0.1&&getNoMoveTime()<0.1){
			return 50;
		}else if(getMoveTime()<0.1&&getNoMoveTime()>0.1){
			return 0;
		}else if(getMoveTime()>0.1&&getNoMoveTime()<0.1){
			return 100;
		}else{
			
			return (int)((int)(getMoveTime()*60*100))/((int)((getMoveTime()+getNoMoveTime())*60));
		}
		
		
	}
	
	public int getNoMoveBFB(){
		
		return 100-getMoveBFB();
	}
	
public static void hdlrSyncStepData(User user,MoveDataService moveDataService,String data){
		
		try {
		JSONObject jsonObject = new JSONObject(data);

		JSONArray sportDataArray = jsonObject.getJSONArray("syncSportDataResult");

		if(sportDataArray!=null&&sportDataArray.length()>0){
			
			Log.i("log","sportDataArray.length()="+sportDataArray.length());
			
			int dayNumbers = sportDataArray.length()/48;
			int yushu = sportDataArray.length()%48;
			
			if(yushu>0){
				dayNumbers = dayNumbers+1;
			}
			
			for(int dayIndex = 0; dayIndex<dayNumbers; dayIndex++){
				
				MoveData moveData = new MoveData(user.userId);
				Log.i("log","dayIndex="+dayIndex);
				for(int i=0; (((dayIndex*48)+i)<sportDataArray.length())&&(i<48); i++){
					String deviceId = sportDataArray.getJSONObject((dayIndex*48)+i).getString("deviceId");
					String sportType = sportDataArray.getJSONObject((dayIndex*48)+i).getString("sportType");
					String sportTime = sportDataArray.getJSONObject((dayIndex*48)+i).getString("sportTime");
					boolean isWear = sportDataArray.getJSONObject((dayIndex*48)+i).getBoolean("isWear");
					int sportStep = sportDataArray.getJSONObject((dayIndex*48)+i).getInt("sportStep");
					
					//if(i==0){
						int year = Integer.valueOf(sportTime.substring(0, 4));
						int month = Integer.valueOf(sportTime.substring(4, 6))-1;
						int day = Integer.valueOf(sportTime.substring(6, 8));
						
						
						moveData.year = year;
						moveData.mounth = month;
						moveData.day = day;
					//}
					
					String time = sportTime.substring(8, 14);
						Log.i("log",year+" "+month+" "+day+" "+time+" sportStep="+sportStep);
					if(time.equals("000000")){
						if(!isWear){
							moveData.time_0 = -1;
						}else{
							moveData.time_0 = sportStep;
						}
					}else if(time.equals("003000")){
						if(!isWear){
							moveData.time_0_5 = -1;
						}else{
							moveData.time_0_5 = sportStep;
						}
					}else if(time.equals("010000")){
						if(!isWear){
							moveData.time_1 = -1;
						}else{
							moveData.time_1 = sportStep;
						}
					}else if(time.equals("013000")){
						if(!isWear){
							moveData.time_1_5 = -1;
						}else{
							moveData.time_1_5 = sportStep;
						}
					}else if(time.equals("020000")){
						if(!isWear){
							moveData.time_2 = -1;
						}else{
							moveData.time_2 = sportStep;
						}
					}else if(time.equals("023000")){
						if(!isWear){
							moveData.time_2_5 = -1;
						}else{
							moveData.time_2_5 = sportStep;
						}
					}else if(time.equals("030000")){
						if(!isWear){
							moveData.time_3 = -1;
						}else{
							moveData.time_3 = sportStep;
						}
					}else if(time.equals("033000")){
						if(!isWear){
							moveData.time_3_5 = -1;
						}else{
							moveData.time_3_5 = sportStep;
						}
					}else if(time.equals("040000")){
						if(!isWear){
							moveData.time_4 = -1;
						}else{
							moveData.time_4 = sportStep;
						}
					}else if(time.equals("043000")){
						if(!isWear){
							moveData.time_4_5 = -1;
						}else{
							moveData.time_4_5 = sportStep;
						}
					}else if(time.equals("050000")){
						if(!isWear){
							moveData.time_5 = -1;
						}else{
							moveData.time_5 = sportStep;
						}
					}else if(time.equals("053000")){
						if(!isWear){
							moveData.time_5_5 = -1;
						}else{
							moveData.time_5_5 = sportStep;
						}
					}else if(time.equals("060000")){
						if(!isWear){
							moveData.time_6 = -1;
						}else{
							moveData.time_6 = sportStep;
						}
					}else if(time.equals("063000")){
						if(!isWear){
							moveData.time_6_5 = -1;
						}else{
							moveData.time_6_5 = sportStep;
						}
					}else if(time.equals("070000")){
						if(!isWear){
							moveData.time_7 = -1;
						}else{
							moveData.time_7 = sportStep;
						}
					}else if(time.equals("073000")){
						if(!isWear){
							moveData.time_7_5 = -1;
						}else{
							moveData.time_7_5 = sportStep;
						}
					}else if(time.equals("080000")){
						if(!isWear){
							moveData.time_8 = -1;
						}else{
							moveData.time_8 = sportStep;
						}
					}else if(time.equals("083000")){
						if(!isWear){
							moveData.time_8_5 = -1;
						}else{
							moveData.time_8_5 = sportStep;
						}
					}else if(time.equals("090000")){
						if(!isWear){
							moveData.time_9 = -1;
						}else{
							moveData.time_9 = sportStep;
						}
					}else if(time.equals("093000")){
						if(!isWear){
							moveData.time_9_5 = -1;
						}else{
							moveData.time_9_5 = sportStep;
						}
					}else if(time.equals("100000")){
						if(!isWear){
							moveData.time_10 = -1;
						}else{
							moveData.time_10 = sportStep;
						}
					}else if(time.equals("103000")){
						if(!isWear){
							moveData.time_10_5 = -1;
						}else{
							moveData.time_10_5 = sportStep;
						}
					}else if(time.equals("110000")){
						if(!isWear){
							moveData.time_11 = -1;
						}else{
							moveData.time_11 = sportStep;
						}
					}else if(time.equals("113000")){
						if(!isWear){
							moveData.time_11_5 = -1;
						}else{
							moveData.time_11_5 = sportStep;
						}
					}else if(time.equals("120000")){
						if(!isWear){
							moveData.time_12 = -1;
						}else{
							moveData.time_12 = sportStep;
						}
					}else if(time.equals("123000")){
						if(!isWear){
							moveData.time_12_5 = -1;
						}else{
							moveData.time_12_5 = sportStep;
						}
					}else if(time.equals("130000")){
						if(!isWear){
							moveData.time_13 = -1;
						}else{
							moveData.time_13 = sportStep;
						}
					}else if(time.equals("133000")){
						if(!isWear){
							moveData.time_13_5 = -1;
						}else{
							moveData.time_13_5 = sportStep;
						}
					}else if(time.equals("140000")){
						if(!isWear){
							moveData.time_14 = -1;
						}else{
							moveData.time_14 = sportStep;
						}
					}else if(time.equals("143000")){
						if(!isWear){
							moveData.time_14_5 = -1;
						}else{
							moveData.time_14_5 = sportStep;
						}
					}else if(time.equals("150000")){
						if(!isWear){
							moveData.time_15 = -1;
						}else{
							moveData.time_15 = sportStep;
						}
					}else if(time.equals("153000")){
						if(!isWear){
							moveData.time_15_5 = -1;
						}else{
							moveData.time_15_5 = sportStep;
						}
					}else if(time.equals("160000")){
						if(!isWear){
							moveData.time_16 = -1;
						}else{
							moveData.time_16 = sportStep;
						}
					}else if(time.equals("163000")){
						if(!isWear){
							moveData.time_16_5 = -1;
						}else{
							moveData.time_16_5 = sportStep;
						}
					}else if(time.equals("170000")){
						if(!isWear){
							moveData.time_17 = -1;
						}else{
							moveData.time_17 = sportStep;
						}
					}else if(time.equals("173000")){
						if(!isWear){
							moveData.time_17_5 = -1;
						}else{
							moveData.time_17_5 = sportStep;
						}
					}else if(time.equals("180000")){
						if(!isWear){
							moveData.time_18 = -1;
						}else{
							moveData.time_18 = sportStep;
						}
					}else if(time.equals("183000")){
						if(!isWear){
							moveData.time_18_5 = -1;
						}else{
							moveData.time_18_5 = sportStep;
						}
					}else if(time.equals("190000")){
						if(!isWear){
							moveData.time_19 = -1;
						}else{
							moveData.time_19 = sportStep;
						}
					}else if(time.equals("193000")){
						if(!isWear){
							moveData.time_19_5 = -1;
						}else{
							moveData.time_19_5 = sportStep;
						}
					}else if(time.equals("200000")){
						if(!isWear){
							moveData.time_20 = -1;
						}else{
							moveData.time_20 = sportStep;
						}
					}else if(time.equals("203000")){
						if(!isWear){
							moveData.time_20_5 = -1;
						}else{
							moveData.time_20_5 = sportStep;
						}
					}else if(time.equals("210000")){
						if(!isWear){
							moveData.time_21 = -1;
						}else{
							moveData.time_21 = sportStep;
						}
					}else if(time.equals("213000")){
						if(!isWear){
							moveData.time_21_5 = -1;
						}else{
							moveData.time_21_5 = sportStep;
						}
					}else if(time.equals("220000")){
						if(!isWear){
							moveData.time_22 = -1;
						}else{
							moveData.time_22 = sportStep;
						}
					}else if(time.equals("223000")){
						if(!isWear){
							moveData.time_22_5 = -1;
						}else{
							moveData.time_22_5 = sportStep;
						}
					}else if(time.equals("230000")){
						if(!isWear){
							moveData.time_23 = -1;
						}else{
							moveData.time_23 = sportStep;
						}
					}else if(time.equals("233000")){
						if(!isWear){
							moveData.time_23_5 = -1;
						}else{
							moveData.time_23_5 = sportStep;
						}
					}
				}
				Log.i("log","moveData.toString()="+moveData.toString());
				moveDataService.insertMoveData(moveData);
			}
			
			
		}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public Vector<SportEvent> getSportEvent() {
		Vector<Integer> times = new Vector<Integer>();
		times.add(time_0);
		times.add(time_0_5);
		times.add(time_1);
		times.add(time_1_5);
		times.add(time_2);
		times.add(time_2_5);
		times.add(time_3);
		times.add(time_3_5);
		times.add(time_4);
		times.add(time_4_5);
		times.add(time_5);
		times.add(time_5_5);
		times.add(time_6);
		times.add(time_6_5);
		times.add(time_7);
		times.add(time_7_5);
		times.add(time_8);
		times.add(time_8_5);
		times.add(time_9);
		times.add(time_9_5);
		times.add(time_10);
		times.add(time_10_5);
		times.add(time_11);
		times.add(time_11_5);
		times.add(time_12);
		times.add(time_12_5);
		times.add(time_13);
		times.add(time_13_5);
		times.add(time_14);
		times.add(time_14_5);
		times.add(time_15);
		times.add(time_15_5);
		times.add(time_16);
		times.add(time_16_5);
		times.add(time_17);
		times.add(time_17_5);
		times.add(time_18);
		times.add(time_18_5);
		times.add(time_19);
		times.add(time_19_5);
		times.add(time_20);
		times.add(time_20_5);
		times.add(time_21);
		times.add(time_21_5);
		times.add(time_22);
		times.add(time_22_5);
		times.add(time_23);
		times.add(time_23_5);
		
		Vector<SportEvent> sportEvents = new Vector<SportEvent>();
		/**先遍历一遍设置每个时段的类型
		 * 
		 * 
		 * */
		Vector<Integer> types = new Vector<Integer>();
		int step = 65535;
		for(int i = 0; i < times.size(); i++) {
			step = times.elementAt(i);
			if (step < 0) {
				types.add(NO_WEAR);
			} else if (step == 0) {
				types.add(NO_MOVE);
			} 
//			else if (step < 100) {
//				types.add(S_SPORT);
//			} else if(step < 1000) {
//				types.add(M_SPORT);
//			} 
			else {
				types.add(M_SPORT);
			} 
//			Log.e("MoveData", "timeIndex = " + i + " step " + step);
		}
		
		int type = -1;
		int fristTimeIndex = -1;
		int secondTimeIndex = -1;
		int i, j;
		for(i = 0 ; i < types.size(); ) {
			type = types.elementAt(i);
			fristTimeIndex = i;
			for (j = i + 1; j < types.size(); j++) {
				if (type == types.elementAt(j)) {
					continue;
				} else {
					break;
				}
			}
			secondTimeIndex = j - 1;
			if ( !(type == NO_MOVE && secondTimeIndex == fristTimeIndex) && type != NO_WEAR) {
				/*计算某事件流中步数*/
				int steps = 0;
				for (int k = fristTimeIndex; k <= secondTimeIndex; k++ ) {
					steps += times.elementAt(k);
				}
				sportEvents.add(new SportEvent(fristTimeIndex, secondTimeIndex, type, steps));
//			Log.e("MoveData", "sportEvent.index = " + sportEvents.size()
//					+ " fristTimeIndex = " + fristTimeIndex 
//					+ " secondTimeIndex = " + secondTimeIndex + " type = " + type);
			}
			i = j;
		}
		
		
		return sportEvents;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MoveData){
			MoveData moveData = (MoveData) o;
			return moveData.getStep() == this.getStep() &&
					moveData.getMoveTime() == this.getMoveTime() &&
					moveData.getNoMoveTime() == this.getNoMoveTime() && 
					moveData.todayTarget == this.todayTarget;
					
		}
		return false;
	}


	
}
