package com.datamelt.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.datamelt.db.RuleGroup;

public class DateUtility
{
	/**
	 * checks if the rulegroup valid from and until is within
	 * the valid from/until of the rulegroup it depends on.
	 * 
	 * it is valid if the validFrom is greater or equal the other groups validFrom
	 * and if the validUntil is smaller or equal the other groups validUntil
	 */
	public static boolean isWithinValidFromUntil(RuleGroup ruleGroup, RuleGroup dependentRuleGroup) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar dependentRuleGroupDateFrom = Calendar.getInstance();
		dependentRuleGroupDateFrom.setTime(sdf.parse(dependentRuleGroup.getValidFrom()));
		dependentRuleGroupDateFrom.set(Calendar.HOUR_OF_DAY, 0);
		dependentRuleGroupDateFrom.set(Calendar.MINUTE, 0);
		dependentRuleGroupDateFrom.set(Calendar.SECOND, 0);
		dependentRuleGroupDateFrom.set(Calendar.MILLISECOND, 0);
		
		Calendar dependentRuleGroupDateUntil = Calendar.getInstance();
		dependentRuleGroupDateUntil.setTime(sdf.parse(dependentRuleGroup.getValidUntil()));
		dependentRuleGroupDateUntil.set(Calendar.HOUR_OF_DAY, 0);
		dependentRuleGroupDateUntil.set(Calendar.MINUTE, 0);
		dependentRuleGroupDateUntil.set(Calendar.SECOND, 0);
		dependentRuleGroupDateUntil.set(Calendar.MILLISECOND, 0);

		Calendar ruleGroupDateFrom = Calendar.getInstance();
		ruleGroupDateFrom.setTime(sdf.parse(ruleGroup.getValidFrom()));
		ruleGroupDateFrom.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateFrom.set(Calendar.MINUTE, 0);
		ruleGroupDateFrom.set(Calendar.SECOND, 0);
		ruleGroupDateFrom.set(Calendar.MILLISECOND, 0);
		
		Calendar ruleGroupDateUntil = Calendar.getInstance();
		ruleGroupDateUntil.setTime(sdf.parse(ruleGroup.getValidUntil()));
		ruleGroupDateUntil.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateUntil.set(Calendar.MINUTE, 0);
		ruleGroupDateUntil.set(Calendar.SECOND, 0);
		ruleGroupDateUntil.set(Calendar.MILLISECOND, 0);
		
		
		// from javadoc:
		// ... the value 0 if the time represented by the argument is equal to the time represented by this Calendar
		// a value less than 0 if the time of this Calendar is before the time represented by the argument
		// and a value greater than 0 if the time of this Calendar is after the time represented by the argument.
		
		// we check if this rulegroup lies within the timerange of the rulegroup passed to this method
		return ruleGroupDateFrom.compareTo(dependentRuleGroupDateFrom)>=0 && ruleGroupDateUntil.compareTo(dependentRuleGroupDateUntil)<=0;
	}
	
	public static boolean isValidFromBeforeValidUntil(RuleGroup ruleGroup) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar ruleGroupDateFrom = Calendar.getInstance();
		ruleGroupDateFrom.setTime(sdf.parse(ruleGroup.getValidFrom()));
		ruleGroupDateFrom.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateFrom.set(Calendar.MINUTE, 0);
		ruleGroupDateFrom.set(Calendar.SECOND, 0);
		ruleGroupDateFrom.set(Calendar.MILLISECOND, 0);
		
		Calendar ruleGroupDateUntil = Calendar.getInstance();
		ruleGroupDateUntil.setTime(sdf.parse(ruleGroup.getValidUntil()));
		ruleGroupDateUntil.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateUntil.set(Calendar.MINUTE, 0);
		ruleGroupDateUntil.set(Calendar.SECOND, 0);
		ruleGroupDateUntil.set(Calendar.MILLISECOND, 0);
		
		// from javadoc:
		// ... the value 0 if the time represented by the argument is equal to the time represented by this Calendar
		// a value less than 0 if the time of this Calendar is before the time represented by the argument
		// and a value greater than 0 if the time of this Calendar is after the time represented by the argument.

		return ruleGroupDateFrom.compareTo(ruleGroupDateUntil)<=0;
	}
	
	public static boolean isValidFromBeforeValidUntil(String validFrom, String validUntil) throws Exception
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar ruleGroupDateFrom = Calendar.getInstance();
		ruleGroupDateFrom.setTime(sdf.parse(validFrom));
		ruleGroupDateFrom.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateFrom.set(Calendar.MINUTE, 0);
		ruleGroupDateFrom.set(Calendar.SECOND, 0);
		ruleGroupDateFrom.set(Calendar.MILLISECOND, 0);
		
		Calendar ruleGroupDateUntil = Calendar.getInstance();
		ruleGroupDateUntil.setTime(sdf.parse(validUntil));
		ruleGroupDateUntil.set(Calendar.HOUR_OF_DAY, 0);
		ruleGroupDateUntil.set(Calendar.MINUTE, 0);
		ruleGroupDateUntil.set(Calendar.SECOND, 0);
		ruleGroupDateUntil.set(Calendar.MILLISECOND, 0);
		
		// from javadoc:
		// ... the value 0 if the time represented by the argument is equal to the time represented by this Calendar
		// a value less than 0 if the time of this Calendar is before the time represented by the argument
		// and a value greater than 0 if the time of this Calendar is after the time represented by the argument.

		return ruleGroupDateFrom.compareTo(ruleGroupDateUntil)<=0;
	}
}
