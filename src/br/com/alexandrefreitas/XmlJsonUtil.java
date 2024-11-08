package br.com.alexandrefreitas;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class XmlJsonUtil {

    public static String xmlToJson(XmlObject xmlObject) throws Exception {

        String xml = xmlObject.toString();

        System.out.println(xml);

        XMLSerializer ser = new XMLSerializer();
        JSONObject json = (JSONObject) ser.read(xml);

        System.out.println(json);

        return json.toString();
    }

    public static String xmlToJsonArray(XmlObject xmlObject) throws Exception {

        String xml = xmlObject.toString();

        System.out.println(xml);

        XMLSerializer ser = new XMLSerializer();
        JSONArray json = (JSONArray) ser.read(xml);

        System.out.println(json);

        return json.toString();
    }

    public static XmlObject jsonToXml(String json) throws Exception {

        if (json == null || json.trim().length() == 0) {
            return null;
        }

        XMLSerializer ser = new XMLSerializer();

        String xml = "";

        if (json.startsWith("{")) {
            JSONObject jsonObject = JSONObject.fromObject(json);
            xml = ser.write(jsonObject);
        } else {
            JSONArray jsonArray = JSONArray.fromObject(json);
            xml = ser.write(jsonArray);
        }

        return XmlObject.Factory.parse(xml);
    }

    public static XmlObject jsonToGenericXml(String json) throws Exception {

        if (json == null || json.trim().length() == 0) {
            return null;
        }

        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("Documents");

        if (jsonArray == null || jsonArray.size() == 0) {
            return null;
        }

        JSONObject root = (JSONObject) jsonArray.get(0);

        String xml = "<jsonToXml><e></e></jsonToXml>";

        XmlObject xmlObject = XmlObject.Factory.parse(xml);
        XmlCursor xmlCursor = xmlObject.newCursor();
        xmlCursor.toChild("jsonToXml");
        xmlCursor.toChild("e");

        Set entrySet = root.entrySet();
        for (Iterator iter = entrySet.iterator(); iter.hasNext();) {
            Map.Entry element = (Map.Entry) iter.next();
            Object value = element.getValue();
            String valueAsString = "";

            if (value == null) {
                valueAsString = "";
            } else {
                valueAsString = value.toString();
            }

            if (value.equals("null")) {
                xmlCursor.insertElement((String) element.getKey());
            } else {
                xmlCursor.insertElementWithText((String) element.getKey(),
                        valueAsString);
            }

        }

        return xmlObject;
    }


    public static void main(String[] args) throws Exception {
        String json = "[{" +
                "\"verificador\":\"1\","+
                "\"tipo\":\"total\","+
                "\"valor_pago\":\"18.00\","+
                "\"data_pagamento\":\"2020-02-01\","+
                "\"data_transacao\":\"2020-02-02\"}," +
                "{"+
                "\"verificador\":\"2\","+
                "\"tipo\":\"parcial\","+
                "\"valor_pago\":\"30.00\","+
                "\"data_pagamento\":\"2020-02-18\","+
                "\"data_transacao\":\"2020-02-19\"}]";

        XmlObject xml = XmlJsonUtil.jsonToXml( json );

        System.out.println( xml.toString() );

        String jsonResult = XmlJsonUtil.xmlToJsonArray( xml );

        System.out.println( jsonResult );

    }
}
