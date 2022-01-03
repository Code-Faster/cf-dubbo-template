package com.createTemplate.api.util;

public class RandScope {
	private double gteVal;
	private double lteVal;
	private long length;
	private int limit;

	public RandScope(long length, int limit) {
		double rndVal = Math.random() * length;
		// 根据要获取的记录的条数，对范围值进行修改
		gteVal = rndVal - limit;
		lteVal = rndVal + limit;
		if (gteVal < 0) {
			lteVal += Math.abs(gteVal);
			gteVal = 0;
		} else if (lteVal > length) {
			lteVal -= (length - lteVal);
			gteVal = length;
		}
		gteVal = gteVal / length;
		lteVal = lteVal / length;
	}

	/**
	 * @return the gteVal
	 */
	public double getGteVal() {
		return gteVal;
	}

	/**
	 * @return the lteVal
	 */
	public double getLteVal() {
		return lteVal;
	}
	public static void main(String[] args) {
		RandScope randScope = new RandScope(2000, 7);
		System.out.println(randScope.getGteVal());
		System.out.println(randScope.getLteVal());
	}
}