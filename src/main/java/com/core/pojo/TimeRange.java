package com.core.pojo;

import java.util.Date;

public class TimeRange {

	private Date startTime;
	private Date endTime;

	public TimeRange(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public TimeRange() {
	}

	public Date getStartTime() {
		return startTime;
	}

	public TimeRange setStartTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}

	public Date getEndTime() {
		return endTime;
	}

	public TimeRange setEndTime(Date endTime) {
		this.endTime = endTime;
		return this;
	}
}
