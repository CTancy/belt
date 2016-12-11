package com.jibu.app.entity;

import com.jibu.app.R;

public class SportEvent {
	
	private int sportIcon;
	private int sportType;
	private String sportPeriod;
	private float sportTotalTime;
	private int sportTimeUnit;
	private int type;
	private int steps;
	
	private static final int B_SIT_GAP = 4;
	private static final int M_SIT_GAP = 2;
	private static final int S_SIT_GAP = 1;
	
	public SportEvent(int startTime, int endTime, int type, int steps){
		switch(type) {
		case MoveData.NO_MOVE:
			if(endTime - startTime >= B_SIT_GAP) {
				this.sportIcon = R.drawable.b_sit;
				this.sportType = R.string.b_sit;
			} else if(endTime - startTime >= M_SIT_GAP) {
				this.sportIcon = R.drawable.m_sit;
				this.sportType = R.string.m_sit;
			} else if(endTime - startTime >= S_SIT_GAP) {
				this.sportIcon = R.drawable.s_sit;
				this.sportType = R.string.s_sit;
			} 
			break;
		case MoveData.NO_WEAR:
			break;
		case MoveData.S_SPORT:
			this.sportIcon = R.drawable.s_sport;
			this.sportType = R.string.s_sport;
			break;
		case MoveData.M_SPORT:
			this.sportIcon = R.drawable.m_sport;
			this.sportType = R.string.m_sport;
			break;
		case MoveData.B_SPORT:
			this.sportIcon = R.drawable.b_sport;
			this.sportType = R.string.b_sport;
			break;
			
		}
		this.sportPeriod = NoMoveData.getTimeDesc(startTime) + "~" 
				+ NoMoveData.getTimeDesc((endTime + 1) % 48);
		this.sportTotalTime = (endTime - startTime + 1) * 0.5f;
		this.sportTimeUnit = R.string.hour;
		this.type = type;
		this.steps= steps;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getSteps() {
		return steps;
	}
	public int getSportIcon() {
		return sportIcon;
	}

	public void setSportIcon(int sportIcon) {
		this.sportIcon = sportIcon;
	}

	public int getSportType() {
		return sportType;
	}

	public void setSportType(int sportType) {
		this.sportType = sportType;
	}

	public String getSportPeriod() {
		return sportPeriod;
	}

	public void setSportPeriod(String sportPeriod) {
		this.sportPeriod = sportPeriod;
	}

	public float getSportTotalTime() {
		return sportTotalTime;
	}

	public void setSportTotalTime(float sportTotalTime) {
		this.sportTotalTime = sportTotalTime;
	}

	public int getSportTimeUnit() {
		return sportTimeUnit;
	}

	public void setSportTimeUnit(int sportTimeUnit) {
		this.sportTimeUnit = sportTimeUnit;
	}
	
	


}
