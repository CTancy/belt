package com.jibu.app.entity;

public class CejuData {
	
	public String userId;
	/**
	 * ��¼��ֱ�Ϊʱ�����������ʱ������ע��Ϣ
	 */
	public long timestamp;
	public int step;
	public long duration;
	public String info;

	public CejuData() {
		this.userId       = "";
		this.timestamp    = 0;
		this.step         = 0;
		this.duration 	  = 0;
		this.info		  = "";
	}
	
	public CejuData(String id, long timestamp, int step, long duration, String info) {
		this.userId       = id;
		this.timestamp    = timestamp;
		this.step         = step;
		this.duration 	  = duration;
		this.info		  = info;
	}
	
	
}
