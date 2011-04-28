package com.monitoring.munin_node.plugins;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;

import com.monitoring.munin_node.plugin_api.Plugin_API;

public class battery implements Plugin_API {
	ContextWrapper context = null;
	@Override
	public String getName() {
		return "Battery";
	}

	@Override
	public String getCat() {
		return "Android Phone";
	}
	public Boolean needsContext() {
		return true;
	}
	public Void setContext(Context newcontext) {
		context = new ContextWrapper(newcontext);
		return null;
	}
	@Override
	public String getConfig() {
		StringBuffer output = new StringBuffer();
		output.append("graph_title Battery Charge\n");
		output.append("graph_args --upper-limit 100 -l 0\n");
		output.append("graph_scale no\n");
		output.append("graph_vlabel %\n");
		output.append("graph_category Android Phone\n");
		output.append("graph_info This graph shows battery charge in %\n");
		output.append("battery.label Battery Charge\n");
		output.append("charge.label Charging\n");
		output.append("multigraph Battery_Temp\n");
		output.append("graph_title Battery Temp\n");
		output.append("graph_vlabel Temp\n");
		output.append("graph_category Android Phone\n");
		output.append("graph_info This graph shows battery Temp\n");
		output.append("temp.label Battery Temperature\n");
		output.append("multigraph Battery_Volt\n");
		output.append("graph_title Battery Voltage\n");
		output.append("graph_vlabel Voltage\n");
		output.append("graph_category Android Phone\n");
		output.append("graph_info This graph shows battery Voltage\n");
		output.append("volt.label Battery Voltage");
		return output.toString();
	}
	    
	@Override
	public String getUpdate() {
		StringBuffer output = new StringBuffer();
		Intent Battery = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		Float Battery_unscaled = new Float(Battery.getIntExtra("level", 0));
		Float Battery_scale = new Float(Battery.getIntExtra("scale", 0));
		Float Battery_value = (Battery_unscaled/Battery_scale)*100;
		output.append("battery.value "+Battery_value.toString()+"\n");
		if(Battery.getIntExtra("plugged", 0)>0){
			output.append("charge.value 100\n");
		}
		else{
			output.append("charge.value 0\n");
		}
		output.append("multigraph Battery_Temp\n");
		Integer temp = Battery.getIntExtra("voltage", 0);
		output.append("temp.value "+temp.toString()+"\n");
		output.append("multigraph Battery_Volt\n");
		Integer volt = Battery.getIntExtra("temperature",0);
		output.append("volt.value "+volt);
		return output.toString();
	}

}
