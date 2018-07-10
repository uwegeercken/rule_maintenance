package com.datamelt.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.datamelt.db.RuleGroupTestData;

public class RuleGroupTestDataChecker
{
	public boolean compareTestData(HashMap<String, String> ruleGroupTestData,ArrayList<RuleGroupTestData> existingRuleGroupTestData)
	{
		long equalValuesCounter=0;
		for(int i=0;i<existingRuleGroupTestData.size();i++)
		{
			String testDataValue = existingRuleGroupTestData.get(i).getKeyValuePairs().toString();
			String ruleGroupTestDataValue = ruleGroupTestData.toString();
			if(testDataValue.equals(ruleGroupTestDataValue))
			{
				equalValuesCounter++;
			}
		}
		
		return equalValuesCounter>0;
	}
}
