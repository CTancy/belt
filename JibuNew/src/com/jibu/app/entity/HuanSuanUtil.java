package com.jibu.app.entity;

public class HuanSuanUtil {

	
	public static float getLengthByStep(int step){
		
		return (float)((float)step)*(float)0.5/1000;
	}
	
	public static int getCalByStep(int step){
		
		return (int)(step*(float)0.09);
	}
	
	/*
	�˵ĳ�ų�Լ����ߵ�1/7
		  
	��������166cm���ϵ�һ��Ϊ�߸�  
	��ߣ���������1/3�㼣�� 
	 
	��������148cm--166cm���ϵ�һ��Ϊ�и�  
	��ߣ���������1/2�㼣�� 

	��������140cm���µ�һ��Ϊ����  
	��ߣ���������2/3�㼣��  
	*/
	
	public static float getBuchang(float height) {
		return height * 11 / 28;
	}
}
