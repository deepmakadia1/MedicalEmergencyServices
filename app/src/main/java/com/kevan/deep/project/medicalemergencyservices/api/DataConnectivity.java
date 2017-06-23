package com.kevan.deep.project.medicalemergencyservices.api;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.kevan.deep.project.medicalemergencyservices.config.Config;

@SuppressWarnings("deprecation")
@SuppressLint("NewApi")
public class DataConnectivity 
{
	private String str_URL = Config.str_URL;
	private String str_SOAPActURL = Config.str_SOAPActURL;
	private String AEH_URL = Config.AEH_URL;
	private String AEH_ActURL = Config.AEH_ActURL;

	String aID,str_Random,str_Ran,P1,P2,FinalP;
	String android_id;
	private long elapsedTime;
	private int seconds;
	private long startTime;

//=====================================================================================
// Call Web Service
//=====================================================================================
	public String callWebService(String str_SoapAction, String str_Envelope) {

	//-------------------------------------------------------------------------------
		char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 8; i++) 
		{
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		str_Random = sb.toString();
		aID = Config.AndroidId;
		
		char[] chars2 = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
		StringBuilder sb2 = new StringBuilder();
		Random random2 = new Random();
		for (int i = 0; i < 16; i++) 
		{
		    char c = chars2[random2.nextInt(chars2.length)];
		    sb2.append(c);
		}
		android_id = sb2.toString();	
		if (aID == null || aID.isEmpty() || aID.equals(""))
		{
			aID = android_id;
		}
		
		str_Ran = "4082RWk8h271" + "" + str_Random;

		str_Ran = str_Ran.toString().replaceAll("9", "j");
		str_Ran = str_Ran.toString().replaceAll("0", "Y");
		str_Ran = str_Ran.toString().replaceAll("5", "p");
		str_Ran = str_Ran.toString().replaceAll("8", "B");
		str_Ran = str_Ran.toString().replaceAll("7", "m");
		str_Ran = str_Ran.toString().replaceAll("4", "c");
		str_Ran = str_Ran.toString().replaceAll("1", "g");
		str_Ran = str_Ran.toString().replaceAll("3", "F");
		str_Ran = str_Ran.toString().replaceAll("6", "U");
		str_Ran = str_Ran.toString().replaceAll("2", "t");

		aID = aID.toString().replaceAll("9", "jEr");
		aID = aID.toString().replaceAll("0", "Ysd");
		aID = aID.toString().replaceAll("5", "pre");
		aID = aID.toString().replaceAll("8", "Bde");
		aID = aID.toString().replaceAll("7", "msd");
		aID = aID.toString().replaceAll("4", "cvr");
		aID = aID.toString().replaceAll("1", "gaw");
		aID = aID.toString().replaceAll("3", "FsA");
		aID = aID.toString().replaceAll("6", "UEr");
		aID = aID.toString().replaceAll("2", "tQS");

		P1 = str_Ran;
		P2 = aID;
		FinalP = str_Ran + "" + aID;

		Date today = new Date();
		SimpleDateFormat curFormaterdate = new SimpleDateFormat("ddMMyyyy",java.util.Locale.getDefault());
		SimpleDateFormat curFormateryear = new SimpleDateFormat("dd",java.util.Locale.getDefault());
		String currentday = curFormateryear.format(today);
		int cday = Integer.parseInt(currentday);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -cday);
		Date yesterday = calendar.getTime();
		SimpleDateFormat curFormateryesterday = new SimpleDateFormat("dd",java.util.Locale.getDefault());
		String current_date = curFormaterdate.format(today);
		String current_day = curFormateryear.format(today);
		String yesterday_day = curFormateryesterday.format(yesterday);
		long hname = Integer.parseInt(current_date)* Integer.parseInt(yesterday_day);
		long hvalue = Integer.parseInt(current_date)* Integer.parseInt(current_day);
		String strhname = Long.toString(hname);
		String strhvalue = Long.toString(hvalue);

//-------------------------------------------------------------------------------
		final DefaultHttpClient httpClient1 = new DefaultHttpClient();
		HttpParams params1 = httpClient1.getParams();
		HttpConnectionParams.setConnectionTimeout(params1, 50000);
		HttpConnectionParams.setSoTimeout(params1, 50000);
		HttpProtocolParams.setUseExpectContinue(httpClient1.getParams(), true);
		HttpPost httpPost1 = new HttpPost(str_URL);
		httpPost1.setHeader("soapaction", str_SOAPActURL + str_SoapAction);
		httpPost1.setHeader("Content-Type", "text/xml; charset=utf-8");
		httpPost1.setHeader(strhname, strhvalue);
		httpPost1.setHeader("Private1", FinalP);

// -------------------------------------------------------------------------------------------------------------------
		String respString1 = "";
		try 
		{
			if (android.os.Build.VERSION.SDK_INT > 9) 
			{
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				HttpEntity entity1 = new StringEntity(str_Envelope);
				httpPost1.setEntity(entity1);
				
				startTime = System.currentTimeMillis();
				System.out.println("<< Request : "+str_SoapAction+" >> \n"+str_Envelope);
				
				ResponseHandler<String> rh1 = new ResponseHandler<String>() 
				{
					@Override
					public String handleResponse(HttpResponse response)throws ClientProtocolException, IOException 
					{
						HttpEntity entity = response.getEntity();
						StringBuffer out1 = new StringBuffer();
						byte[] b = EntityUtils.toByteArray(entity);
						out1.append(new String(b, 0, b.length));
						return out1.toString();
					}
				};
				respString1 = httpClient1.execute(httpPost1, rh1);
				elapsedTime = System.currentTimeMillis() - startTime;
				seconds = (int) ((elapsedTime / 1000) % 60);
				
				System.out.println("<< Response : "+str_SoapAction+"("+elapsedTime+" milis,"+seconds+" sec ) >> \n"+respString1);
			}
		} 
		catch (Exception e) 
		{
			//System.out.println("exception :"+"("+elapsedTime+" milis,"+seconds+" sec ) >> " + e);
		}
		httpClient1.getConnectionManager().shutdown();
		return respString1;
	}
	
	public String callCommonWebService(String str_SoapAction, String str_Envelope) {

		//-------------------------------------------------------------------------------
			char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
			StringBuilder sb = new StringBuilder();
			Random random = new Random();
			for (int i = 0; i < 8; i++) 
			{
				char c = chars[random.nextInt(chars.length)];
				sb.append(c);
			}
			str_Random = sb.toString();
			aID = Config.AndroidId;
			
			char[] chars2 = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
			StringBuilder sb2 = new StringBuilder();
			Random random2 = new Random();
			for (int i = 0; i < 16; i++) 
			{
			    char c = chars2[random2.nextInt(chars2.length)];
			    sb2.append(c);
			}
			android_id = sb2.toString();	
			if (aID == null || aID.isEmpty() || aID.equals(""))
			{
				aID = android_id;
			}
			
			str_Ran = "4082RWk8h271" + "" + str_Random;

			str_Ran = str_Ran.toString().replaceAll("9", "j");
			str_Ran = str_Ran.toString().replaceAll("0", "Y");
			str_Ran = str_Ran.toString().replaceAll("5", "p");
			str_Ran = str_Ran.toString().replaceAll("8", "B");
			str_Ran = str_Ran.toString().replaceAll("7", "m");
			str_Ran = str_Ran.toString().replaceAll("4", "c");
			str_Ran = str_Ran.toString().replaceAll("1", "g");
			str_Ran = str_Ran.toString().replaceAll("3", "F");
			str_Ran = str_Ran.toString().replaceAll("6", "U");
			str_Ran = str_Ran.toString().replaceAll("2", "t");

			aID = aID.toString().replaceAll("9", "jEr");
			aID = aID.toString().replaceAll("0", "Ysd");
			aID = aID.toString().replaceAll("5", "pre");
			aID = aID.toString().replaceAll("8", "Bde");
			aID = aID.toString().replaceAll("7", "msd");
			aID = aID.toString().replaceAll("4", "cvr");
			aID = aID.toString().replaceAll("1", "gaw");
			aID = aID.toString().replaceAll("3", "FsA");
			aID = aID.toString().replaceAll("6", "UEr");
			aID = aID.toString().replaceAll("2", "tQS");

			P1 = str_Ran;
			P2 = aID;
			FinalP = str_Ran + "" + aID;

			Date today = new Date();
			SimpleDateFormat curFormaterdate = new SimpleDateFormat("ddMMyyyy",java.util.Locale.getDefault());
			SimpleDateFormat curFormateryear = new SimpleDateFormat("dd",java.util.Locale.getDefault());
			String currentday = curFormateryear.format(today);
			int cday = Integer.parseInt(currentday);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -cday);
			Date yesterday = calendar.getTime();
			SimpleDateFormat curFormateryesterday = new SimpleDateFormat("dd",java.util.Locale.getDefault());
			String current_date = curFormaterdate.format(today);
			String current_day = curFormateryear.format(today);
			String yesterday_day = curFormateryesterday.format(yesterday);
			long hname = Integer.parseInt(current_date)* Integer.parseInt(yesterday_day);
			long hvalue = Integer.parseInt(current_date)* Integer.parseInt(current_day);
			String strhname = Long.toString(hname);
			String strhvalue = Long.toString(hvalue);

	//-------------------------------------------------------------------------------
			
			final DefaultHttpClient httpClient1 = new DefaultHttpClient();
			HttpParams params1 = httpClient1.getParams();
			HttpConnectionParams.setConnectionTimeout(params1, 50000);
			HttpConnectionParams.setSoTimeout(params1, 50000);
			HttpProtocolParams.setUseExpectContinue(httpClient1.getParams(), true);
			HttpPost httpPost1 = new HttpPost(AEH_URL);
			httpPost1.setHeader("soapaction", AEH_ActURL + str_SoapAction);
			httpPost1.setHeader("Content-Type", "text/xml; charset=utf-8");
			httpPost1.setHeader(strhname, strhvalue);
			httpPost1.setHeader("Private1", FinalP);

	// -------------------------------------------------------------------------------------------------------------------
			String respString1 = "";
			try 
			{
				if (android.os.Build.VERSION.SDK_INT > 9) 
				{
					StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
					StrictMode.setThreadPolicy(policy);
					HttpEntity entity1 = new StringEntity(str_Envelope);
					httpPost1.setEntity(entity1);
					//long startTime = System.currentTimeMillis();
					//System.out.println("<< Request : "+str_SoapAction+" >> \n"+str_Envelope);
					ResponseHandler<String> rh1 = new ResponseHandler<String>() 
					{
						@Override
						public String handleResponse(HttpResponse response)throws ClientProtocolException, IOException 
						{
							HttpEntity entity = response.getEntity();
							StringBuffer out1 = new StringBuffer();
							byte[] b = EntityUtils.toByteArray(entity);
							out1.append(new String(b, 0, b.length));
							return out1.toString();
						}
					};
					respString1 = httpClient1.execute(httpPost1, rh1);
					//long elapsedTime = System.currentTimeMillis() - startTime;
					//int seconds = (int) ((elapsedTime / 1000) % 60);
					//System.out.println("<< Response : "+str_SoapAction+"("+elapsedTime+" milis,"+seconds+" sec ) >> \n"+respString1);
				}
			} 
			catch (Exception e) 
			{
				//System.out.println("exception :" + e);
			}
			httpClient1.getConnectionManager().shutdown();
			return respString1;
		}
	
//=====================================================================================
// XML Document Builder
//=====================================================================================
	public Document XMLfromString(String str_ResponseXML) 
	{
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try 
		{
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(str_ResponseXML));
			doc = db.parse(is);
		} 
		catch (ParserConfigurationException e) 
		{
			//System.out.println("XML parse error: " + e.getMessage());
			return null;
		} 
		catch (SAXException e) 
		{
			//System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} 
		catch (IOException e) 
		{
			//System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
		return doc;
	}
}