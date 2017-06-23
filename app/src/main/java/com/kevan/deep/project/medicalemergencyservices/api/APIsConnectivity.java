package com.kevan.deep.project.medicalemergencyservices.api;

import com.kevan.deep.project.medicalemergencyservices.config.Config;

public class APIsConnectivity {
    private String api_url = Config.api_url;
    private String aeh_url = Config.AEH_ActURL;

//==============================================================================
    // XMLEnvelope
//==============================================================================
    public String xmlEnvelope(String params) {
        return ""
                + "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
                + "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
                + "<soap:Body>" + params + "</soap:Body>" + "</soap:Envelope>";

    }


//==============================================================================
    // searchhospitalcity
//==============================================================================
    public String searchhospitalcity(String H_city) {
        return xmlEnvelope(""
                + "<searchhospitalcity xmlns=\"" + api_url + "\">"
                + "<H_city>" + H_city + "</H_city>"
                + "</searchhospitalcity>");
    }
    //==============================================================================
    // searchhospitalcity
//==============================================================================
    public String searchhospitalnmae(String H_name) {
        return xmlEnvelope(""
                + "<searchhospitalnmae xmlns=\"" + api_url + "\">"
                + "<H_name>" + H_name  + "</H_name>"
                + "</searchhospitalnmae>");
    }
    //==============================================================================
    // hospitalfacility
//==============================================================================
    public String hospitalfacility(String H_id) {
        return xmlEnvelope(""
                + "<hospitalfacility xmlns=\"" + api_url + "\">"
                + "<H_id>" + H_id + "</H_id>"
                + "</hospitalfacility>");
    }


    //==============================================================================
    // hospitaldoctor
//==============================================================================
    public String gethospitalcity(String H_id) {
        return xmlEnvelope(""
                + "<gethospitalcity xmlns=\"" + api_url + "\">"
                + "<H_id>" + H_id + "</H_id>"
                + "</gethospitalcity>");
    }

    //==============================================================================
    // hospitaldoctor
//==============================================================================
    public String hospitaldoctor(String H_id) {
        return xmlEnvelope(""
                + "<hospitaldoctor xmlns=\"" + api_url + "\">"
                + "<H_id>" + H_id + "</H_id>"
                + "</hospitaldoctor>");
    }
    //==============================================================================
    // searchhospitalalldata
//==============================================================================
    public String searchhospitalalldata(String H_id) {
        return xmlEnvelope(""
                + "<searchhospitalalldata xmlns=\"" + api_url + "\">"
                + "<H_id>" + H_id + "</H_id>"
                + "</searchhospitalalldata>");
    }


    //==============================================================================
    // searchhospitalname
//==============================================================================
    public String searchhospitalname(String H_name) {
        return xmlEnvelope(""
                + "<searchhospitalname xmlns=\"" + api_url + "\">"
                + "<H_name>" + H_name + "</H_name>"
                + "</searchhospitalname>");
    }




//==============================================================================
    // insertException
//==============================================================================
    public String insertException(String app_name, String app_version, String exception_datetime, String public_ip, String android_id,
                                  String activity_name, String username, String exception_stack, String device_info) {
        return xmlEnvelope(""
                + "<insertException xmlns=\"" + aeh_url + "\">"
                + "<app_name>" + app_name + "</app_name>"
                + "<app_version>" + app_version + "</app_version>"
                + "<exception_datetime>" + exception_datetime + "</exception_datetime>"
                + "<public_ip>" + public_ip + "</public_ip>"
                + "<android_id>" + android_id + "</android_id>"
                + "<activity_name>" + activity_name + "</activity_name>"
                + "<username>" + username + "</username>"
                + "<exception_stack>" + exception_stack + "</exception_stack>"
                + "<device_info>" + device_info + "</device_info>"
                + "</insertException>");
    }

}