package com.datamelt.util;

import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepMeta;

public class PDITransformation 
{
	public static final String RULE_ENGINE_PLUGIN_ID = "Jare_Rule_Engine_Plugin";
	
	private String filename;
	
	public PDITransformation(String filename) throws KettleException
	{
		this.filename = filename;
		KettleEnvironment.init();
	}
	
	
	public List <StepMeta> getRuleEngineSteps() throws KettleException
	{
		//String x = org.pentaho.di.core.Const.DEFAULT_PLUGIN_BASE_FOLDERS;
		KettleEnvironment.init();
		TransMeta transMeta;
		try
		{
			transMeta = new TransMeta(filename);

			String [] steps = transMeta.getStepNames();
			List <StepMeta> ruleEngineSteps = new ArrayList<StepMeta>();
			for(int i=0;i<steps.length;i++)
			{
				StepMeta sm = transMeta.getStep(i);
				String id = sm.getStepID();
				if(id.equals(RULE_ENGINE_PLUGIN_ID))
				{
					ruleEngineSteps.add(sm);
				}
			}
			return ruleEngineSteps;
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	public List <ValueMetaInterface> getRuleEngineStepFields(String stepName) throws KettleException
	{
		List <StepMeta> steps = getRuleEngineSteps();
		int found=-1;
		for(int i=0;i< steps.size();i++)
		{
			StepMeta sm = steps.get(i);
			if(sm.getName().equals(stepName))
			{
				found=i;
				break;
			}
		}
		if (found>-1)
		{
			return getStepPreviousFields(steps.get(found));
		}
		else
		{
			return null;
		}
	}
	
	private List <ValueMetaInterface> getStepPreviousFields(StepMeta sm)
	{
		TransMeta transMeta;
		try
		{
			transMeta = new TransMeta(filename);
	
			RowMetaInterface row = transMeta.getPrevStepFields(sm);
			List <ValueMetaInterface> fields = row.getValueMetaList();
			return fields;
			
		}
		catch(Exception ex)
		{
			return null;
		}
	}
	
	public ValueMetaInterface getField(List <ValueMetaInterface> fields,String fieldname)
	{
		int found=-1;
		for(int i=0;i<fields.size();i++)
		{
			ValueMetaInterface vm = fields.get(i);
			if(vm.getName().equals(fieldname))
			{
				found=i;
				break;
			}
		}
		if(found>-1)
		{
			return fields.get(found);
		}
		else
		{
			return null;
		}
	}
}
