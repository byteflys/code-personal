package com.easing.commons.android.gis.kml;

import com.easing.commons.android.code.Console;
import com.easing.commons.android.data.XML;
import com.easing.commons.android.lbs.LBSLocation;
import com.easing.commons.android.lbs.LBSLocationList;
import com.easing.commons.android.struct.Collections;
import com.easing.commons.android.time.Times;

import org.dom4j.Document;
import org.dom4j.Node;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class KmlHandler {

    //导入路线KML
    public static LBSLocationList importSingleRoute(String file) {
        try {
            Document doc = XML.fileToDoc(new File(file));
            Map namespace = new LinkedHashMap();
            namespace.put("default", "http://www.opengis.net/kml/2.2");
            namespace.put("gx", "http://www.google.com/kml/ext/2.2");
            String value = XML.getValueByPath(doc, "//default:LineString//default:coordinates", namespace);
            LBSLocationList list = new LBSLocationList();
            String[] array1 = value.split(" ");
            int index = 0;
            long now = Times.millisOfNow();
            for (String item : array1) {
                String[] array2 = item.split(",");
                LBSLocation location = new LBSLocation();
                location.longitude = Double.valueOf(array2[0]);
                location.latitude = Double.valueOf(array2[1]);
                location.altitude = Double.valueOf(array2[2]);
                location.time = Times.formatDate(now + index++);
                location.removeRedundantFloat();
                list.add(location);
            }
            return list;
        } catch (Throwable e) {
            Console.error(e);
            return null;
        }
    }

    //导入区域KML
    public static LBSLocationList importSingleZone(String file) {
        try {
            Document doc = XML.fileToDoc(new File(file));
            Map namespace = new LinkedHashMap();
            namespace.put("default", "http://www.opengis.net/kml/2.2");
            namespace.put("gx", "http://www.google.com/kml/ext/2.2");
            String value = XML.getValueByPath(doc, "//default:Polygon//default:coordinates", namespace);
            LBSLocationList list = new LBSLocationList();
            String[] array1 = value.split(" ");
            int index = 0;
            long now = Times.millisOfNow();
            for (String item : array1) {
                String[] array2 = item.split(",");
                LBSLocation location = new LBSLocation();
                location.longitude = Double.valueOf(array2[0]);
                location.latitude = Double.valueOf(array2[1]);
                location.altitude = Double.valueOf(array2[2]);
                location.time = Times.formatDate(now + index++);
                location.removeRedundantFloat();
                list.add(location);
            }
            return list;
        } catch (Throwable e) {
            Console.error(e);
            return null;
        }
    }

    //去除连续的空格为单空格
    public static String trimDoubleSpace(String text) {
        text = text.trim().replaceAll("\n", " ");
        while (text.contains("  "))
            text = text.replaceAll("  ", " ");
        return text;
    }

    //从KML导入全部路线
    public static List<LBSLocationList> importRoutes(String file) {
        List<LBSLocationList> list = Collections.emptyList();
        try {
            Document doc = XML.fileToDoc(new File(file));
            Map namespace = new LinkedHashMap();
            namespace.put("default", "http://www.opengis.net/kml/2.2");
            namespace.put("gx", "http://www.google.com/kml/ext/2.2");
            List<Node> nodes = XML.selectNodesByPath(doc, "//default:LineString//default:coordinates", namespace);
            for (Node node : nodes) {
                String value = node.getStringValue();
                value = trimDoubleSpace(value);
                LBSLocationList locationList = new LBSLocationList();
                String[] array1 = value.split(" ");
                int index = 0;
                long now = Times.millisOfNow();
                for (String item : array1) {
                    String[] array2 = item.split(",");
                    LBSLocation location = new LBSLocation();
                    location.longitude = Double.valueOf(array2[0]);
                    location.latitude = Double.valueOf(array2[1]);
                    location.altitude = Double.valueOf(array2[2]);
                    location.time = Times.formatDate(now + index++);
                    location.removeRedundantFloat();
                    locationList.add(location);
                }
                if (!locationList.isEmpty())
                    list.add(locationList);
            }
        } catch (Throwable e) {
            Console.error(e);
        }
        return list;
    }

    //从KML导入全部区域
    public static List<LBSLocationList> importZones(String file) {
        List<LBSLocationList> list = Collections.emptyList();
        try {
            Document doc = XML.fileToDoc(new File(file));
            Map namespace = new LinkedHashMap();
            namespace.put("default", "http://www.opengis.net/kml/2.2");
            namespace.put("gx", "http://www.google.com/kml/ext/2.2");
            List<Node> nodes = XML.selectNodesByPath(doc, "//default:Polygon//default:coordinates", namespace);
            for (Node node : nodes) {
                String value = node.getStringValue();
                value = trimDoubleSpace(value);
                LBSLocationList locationList = new LBSLocationList();
                String[] array1 = value.split(" ");
                int index = 0;
                long now = Times.millisOfNow();
                for (String item : array1) {
                    String[] array2 = item.split(",");
                    LBSLocation location = new LBSLocation();
                    location.longitude = Double.valueOf(array2[0]);
                    location.latitude = Double.valueOf(array2[1]);
                    location.altitude = Double.valueOf(array2[2]);
                    location.time = Times.formatDate(now + index++);
                    location.removeRedundantFloat();
                    locationList.add(location);
                }
                list.add(locationList);
            }
        } catch (Throwable e) {
            Console.error(e);
        }
        return list;
    }
}

