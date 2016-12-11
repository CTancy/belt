package com.jibu.app.entity;

public class HuanSuanUtil {

	
	public static float getLengthByStep(int step){
		
		return (float)((float)step)*(float)0.5/1000;
	}
	
	public static int getCalByStep(int step){
		
		return (int)(step*(float)0.09);
	}
	
	/*
	人的赤脚长约是身高的1/7
		  
	单步长在166cm以上的一般为高个  
	身高＝单步长＋1/3足迹长 
	 
	单步长在148cm--166cm以上的一般为中个  
	身高＝单步长＋1/2足迹长 

	单步长在140cm以下的一般为矮个  
	身高＝单步长＋2/3足迹长  
	*/
	
	public static float getBuchang(float height) {
		return height * 11 / 28;
	}
}
