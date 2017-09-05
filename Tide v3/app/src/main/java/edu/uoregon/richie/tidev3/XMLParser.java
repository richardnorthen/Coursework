package edu.uoregon.richie.tidev3;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.SAXParserFactory;

public class XMLParser extends DefaultHandler {
    // XML elements
    public static final String STATION_NAME = "stationname",
            STATION_STATE = "state",
            STATION_ID = "stationid",
            TIDE_DATA = "item",
            TIDE_DATE = "date",
            TIDE_TIME = "time",
            TIDE_VALUE_FT = "predictions_in_ft",
            TIDE_VALUE_CM = "predictions_in_cm",
            TIDE_TYPE = "highlow";
    // track current element
    private enum QType {
        OTHER,
        STATION_NAME,
        STATION_STATE,
        STATION_ID,
        TIDE_DATA,
        TIDE_DATE,
        TIDE_TIME,
        TIDE_VALUE_FT,
        TIDE_VALUE_CM,
        TIDE_TYPE
    }
    // XML reader
    private XMLReader reader;
    // build station data object
    private QType qType;
    private String dateString;
    private StationData stationData;
    private TideData tideData;

    public XMLParser() throws Exception {
        // create a SAX XML reader
        reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        // use custom handler as defined below
        reader.setContentHandler(this);
    }

    public StationData getStationData(InputStream stream) throws Exception  {
        // parse given input stream
        InputSource source = new InputSource(stream);
        reader.parse(source);
        // return parsed data
        return stationData;
    }

    @Override
    public void startDocument() throws SAXException {
        // initialize variables
        qType = QType.OTHER;
        stationData = new StationData();
        tideData = new TideData();
        dateString = " ";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // set current element
        if (qName.equals(STATION_NAME)) {
            qType = QType.STATION_NAME;
        } else if (qName.equals(STATION_STATE)) {
            qType = QType.STATION_STATE;
        } else if (qName.equals(STATION_ID)) {
            qType = QType.STATION_ID;
        } else if (qName.equals(TIDE_DATA)) {
            qType = QType.TIDE_DATA;
        } else if (qName.equals(TIDE_DATE)) {
            qType = QType.TIDE_DATE;
        } else if (qName.equals(TIDE_TIME)) {
            qType = QType.TIDE_TIME;
        } else if (qName.equals(TIDE_VALUE_FT)) {
            qType = QType.TIDE_VALUE_FT;
        } else if (qName.equals(TIDE_VALUE_CM)) {
            qType = QType.TIDE_VALUE_CM;
        } else if (qName.equals(TIDE_TYPE)) {
            qType = QType.TIDE_TYPE;
        } else {
            qType = QType.OTHER;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // add tide data to station data
        if (qName.equals(TIDE_DATA)) {
            // parse date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());
            Calendar date = Calendar.getInstance(Locale.getDefault());
            try {
                date.setTime(sdf.parse(dateString));
            } catch (ParseException e) {
                Log.e("XMLParser", e.toString());
            }
            tideData.setDate(date);
            // save tide data
            stationData.getTideData().add(tideData);
            // reset
            tideData = new TideData();
            dateString = " ";
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // save data to relevant location
        String data = new String(ch, start, length);
        switch (qType) {
            case STATION_NAME:
                stationData.setStationName(data);
                break;
            case STATION_STATE:
                stationData.setStationState(data);
                break;
            case STATION_ID:
                stationData.setStationId(data);
                break;
            case TIDE_DATE:
                dateString = data + dateString;
                break;
            case TIDE_TIME:
                dateString = dateString + data;
                break;
            case TIDE_VALUE_FT:
                try {
                    tideData.setValueFt(Double.parseDouble(data));
                } catch (NumberFormatException e) {
                    Log.e("XMLParser", e.toString());
                }
                break;
            case TIDE_VALUE_CM:
                try {
                    tideData.setValueCm(Double.parseDouble(data));
                } catch (NumberFormatException e) {
                    Log.e("XMLParser", e.toString());
                }
                break;
            case TIDE_TYPE:
                if (data.equals("H")) {
                    tideData.setTideType(TideData.TideType.HIGH);
                } else if (data.equals("L")) {
                    tideData.setTideType(TideData.TideType.LOW);
                }
                break;
        }
        qType = QType.OTHER;
    }
}