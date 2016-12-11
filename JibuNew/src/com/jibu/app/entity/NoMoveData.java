package com.jibu.app.entity;

public class NoMoveData {

	public int firstNoMoveDataIndex;
	public int twoNoMoveDataIndex;
	
	public NoMoveData(int firstNoMoveDataIndex,int twoNoMoveDataIndex){
		
		this.firstNoMoveDataIndex = firstNoMoveDataIndex;
		this.twoNoMoveDataIndex = twoNoMoveDataIndex;
	}
	
	
	public float getNoMoveTime(){
		
		float time = 0;
		if(firstNoMoveDataIndex!=-1&&twoNoMoveDataIndex!=-1){
			time = (twoNoMoveDataIndex-firstNoMoveDataIndex+1)*(float)0.5;
		}
		
		return time;
	}
	
	public String getNoMoveTimeDesc(){
		
		/*fix by ctc NoMoveTime 延后半个小时*/
		return getTimeDesc(firstNoMoveDataIndex)+"~"+getTimeDesc((twoNoMoveDataIndex + 1) % 48);
		
		
	}
	
	
	public static String getTimeDesc(int timeIndex){
		String desc = "";
		switch (timeIndex) {
		case 0:
			desc = "00:00";
			break;
		case 1:
			desc = "00:30";
			break;
		case 2:
			desc = "01:00";
			break;
		case 3:
			desc = "01:30";
			break;
		case 4:
			desc = "02:00";
			break;
		case 5:
			desc = "02:30";
			break;
		case 6:
			desc = "03:00";
			break;
		case 7:
			desc = "03:30";
			break;
		case 8:
			desc = "04:00";
			break;
		case 9:
			desc = "04:30";
			break;
		case 10:
			desc = "05:00";
			break;
		case 11:
			desc = "05:30";
			break;
		case 12:
			desc = "06:00";
			break;
		case 13:
			desc = "06:30";
			break;
		case 14:
			desc = "07:00";
			break;
		case 15:
			desc = "07:30";
			break;
		case 16:
			desc = "08:00";
			break;
		case 17:
			desc = "08:30";
			break;
		case 18:
			desc = "09:00";
			break;
		case 19:
			desc = "09:30";
			break;
		case 20:
			desc = "10:00";
			break;
		case 21:
			desc = "10:30";
			break;
		case 22:
			desc = "11:00";
			break;
		case 23:
			desc = "11:30";
			break;
		case 24:
			desc = "12:00";
			break;
		case 25:
			desc = "12:30";
			break;
		case 26:
			desc = "13:00";
			break;
		case 27:
			desc = "13:30";
			break;
		case 28:
			desc = "14:00";
			break;
		case 29:
			desc = "14:30";
			break;
		case 30:
			desc = "15:00";
			break;
		case 31:
			desc = "15:30";
			break;
		case 32:
			desc = "16:00";
			break;
		case 33:
			desc = "16:30";
			break;
		case 34:
			desc = "17:00";
			break;
		case 35:
			desc = "17:30";
			break;
		case 36:
			desc = "18:00";
			break;
		case 37:
			desc = "18:30";
			break;
		case 38:
			desc = "19:00";
			break;
		case 39:
			desc = "19:30";
			break;
		case 40:
			desc = "20:00";
			break;
		case 41:
			desc = "20:30";
			break;
		case 42:
			desc = "21:00";
			break;
		case 43:
			desc = "21:30";
			break;
		case 44:
			desc = "22:00";
			break;
		case 45:
			desc = "22:30";
			break;
		case 46:
			desc = "23:00";
			break;
		case 47:
			desc = "23:30";
			break;
	
		}
		
		
		
		return desc;
		
	}
}
