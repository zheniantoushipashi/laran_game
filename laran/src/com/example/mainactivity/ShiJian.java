package com.example.mainactivity;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;
 public class ShiJian{
	 String name;
   public ShiJian(String name){
	   
	   this.name=name;
   }
  
@SuppressLint("SimpleDateFormat")
public String getName() {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss"); 
	Date curDate = new Date(System.currentTimeMillis()); 
	name = formatter.format(curDate); 
	return name;
}

@SuppressLint("SimpleDateFormat")
public void setName(String name) {
	
	this.name = name;
}	

}