package edu.uoregon.richie.tidev3;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SOAPHandler extends AsyncTask<String, Void, SoapObject> {
    // web service information
    private static final String ENDPOINT_URL = "http://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred",
            WSDL_URL = "http://opendap.co-ops.nos.noaa.gov/axis/services/highlowtidepred?wsdl",
            SERVICE_NAME = "getHLPredAndMetadata",
            STATION_ID = "stationId",
            STATION_NAME = "stationName",
            STATION_STATE = "state",
            DATE_BEGIN = "beginDate",
            DATE_END = "endDate",
            TIDE_TIME = "time",
            TIDE_VALUE = "pred",
            TIDE_TYPE = "type",
            DATUM = "datum",
            UNIT = "unit", // 0 = feet, 1 = meters
            TIMEZONE = "timeZone"; // 0 = local, 1 = UTC
    // hardcoded defaults
    private static final String DATUM_DEFAULT = "MLLW";
    private static final int UNIT_DEFAULT = 0,
            TIMEZONE_DEFAULT = 0;
    // TODO better solution than referencing Main
    private MainActivity mainActivity;

    public SOAPHandler(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected SoapObject doInBackground(String... strings) {
        // setup request object
        SoapObject request = new SoapObject(WSDL_URL, SERVICE_NAME);
        // TODO validate strings array
        request.addProperty(STATION_ID, strings[0]);
        request.addProperty(DATE_BEGIN, strings[1]);
        request.addProperty(DATE_END, strings[2]);
        request.addProperty(DATUM, DATUM_DEFAULT);
        request.addProperty(UNIT, UNIT_DEFAULT);
        request.addProperty(TIMEZONE, TIMEZONE_DEFAULT);
        // setup soap envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // send request to server
        HttpTransportSE transport = new HttpTransportSE(ENDPOINT_URL);
        transport.debug = true;
        try {
            // return response
            transport.call(ENDPOINT_URL + "/" + SERVICE_NAME, envelope);
            return (SoapObject) envelope.bodyIn;
        } catch (Exception e) {
            Log.e("SOAPHandler", e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(SoapObject response) {
        Log.e("!!!!!!!!!", response.toString());
        // TODO build station data object
        StationData stationData = new StationData();
        stationData.setStationName(response.getProperty(STATION_NAME).toString());
        stationData.setStationState(response.getProperty(STATION_STATE).toString());
        stationData.setStationId(response.getProperty(STATION_ID).toString());
        Log.e("!!!!!!!!!", Integer.toString(response.getAttributeCount()));
        mainActivity.soapFinished();
    }
}