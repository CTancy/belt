package com.jibu.app.entity;

import java.util.Calendar;

public class UserPersonalInfo {
	
	public String userId;
	public long lastDate ;
	public long longestSitDay ;
	public long longestSportDay ;
	public int sportDistanceTotal ;
	public float sitTimeTotal ;
	
	public int longestSportDayStep;
	public float longestSitDayTime;
	
	public int dabiaotianshu;
	public int lianxudabiao;
	
	
	public UserPersonalInfo() {
		
	}
	
	public UserPersonalInfo(String userId) {
		this.userId = userId;
		lastDate = 0;
		longestSitDay = 0;
		longestSportDay = 0;
		sportDistanceTotal = 0;
		sitTimeTotal = 0;
		longestSportDayStep = 0;
		longestSitDayTime = 0;
		dabiaotianshu = 0;
		lianxudabiao = 0;
	}
	
}
