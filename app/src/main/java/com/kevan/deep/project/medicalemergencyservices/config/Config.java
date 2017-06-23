package com.kevan.deep.project.medicalemergencyservices.config;

import java.text.DecimalFormat;

public class Config {
	public static final String APP_NAME = "GpsDetailSampleApp";
	public static final String PACKAGE_NAME = "com.infinityinfoway.test_kajal";

	public static final String api_url = "http://10.0.2.2/app/service/";
	public static final String str_URL = "http://10.0.2.2/app/service/apidemo.php";
	public static final String str_SOAPActURL = "http://10.0.2.2/app/service/";

	public static final String AEH_URL = "http://aeh.infinity-travel-solutions.com/app-api/service/common-api.php";
	public static final String AEH_ActURL = "http://aeh.infinity-travel-solutions.com/app-api/service/";

	public static String AndroidId;
	public static int AppVersion;

	public static double roundTwoDecimals(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		return Double.valueOf(twoDForm.format(d));
	}
}
