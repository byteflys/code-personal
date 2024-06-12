package com.easing.commons.android.data;

import com.easing.commons.android.format.Maths;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;

//TODO => 有空完善
public class XML {

    //创建XML文档
    @SneakyThrows
    public static Document createDocument() {
        Document document = DocumentHelper.createDocument();
        return document;
    }

    //添加子节点
    public static void addNode(Document document, Node node) {
        document.add(node);
    }

    //添加子节点
    public static void addNode(Document document, Node... nodes) {
        for (Node node : nodes)
            document.add(node);
    }

    //添加子节点
    public static void addNode(Element parent, Node node) {
        parent.add(node);
    }

    //添加子节点
    public static void addNode(Element parent, Node... nodes) {
        for (Node node : nodes)
            parent.add(node);
    }

    //从字符串创建XML对象
    @SneakyThrows
    public static Document stringToDoc(String xmlString) {
        return DocumentHelper.parseText(xmlString);
    }

    //从文件创建XML对象
    @SneakyThrows
    public static Document fileToDoc(File file) {
        return new SAXReader().read(file);
    }

    //从输入流创建XML对象
    @SneakyThrows
    public static Document streamToDoc(InputStream fis) {
        return new SAXReader().read(fis);
    }

    @SneakyThrows
    public static String getValueByPath(Document doc, String path) {
        Node node = doc.selectSingleNode(path);
        return node.getStringValue().trim();
    }

    @SneakyThrows
    public static String getValueByPath(Document doc, String path, Map namespace) {
        XPath x = doc.createXPath(path);
        x.setNamespaceURIs(namespace);
        return x.selectSingleNode(doc).getStringValue().trim();
    }

    @SneakyThrows
    public static String getValueByPath(Node node, String path, Map namespace) {
        try {
            XPath x = node.createXPath(path);
            x.setNamespaceURIs(namespace);
            Node selectedNode = x.selectSingleNode(node);
            return selectedNode.getStringValue().trim();
        } catch (Throwable e) {
            return null;
        }
    }

    @SneakyThrows
    public static List<Node> selectNodesByPath(Document doc, String path, Map namespace) {
        XPath x = doc.createXPath(path);
        x.setNamespaceURIs(namespace);
        return x.selectNodes(doc);
    }

    @SneakyThrows
    public static List<Node> selectNodesByPath(Node node, String path, Map namespace) {
        XPath x = node.createXPath(path);
        x.setNamespaceURIs(namespace);
        return x.selectNodes(node);
    }

    @SneakyThrows
    public static Node selectNodeByPath(Document doc, String path, Map namespace) {
        XPath x = doc.createXPath(path);
        x.setNamespaceURIs(namespace);
        return x.selectSingleNode(doc);
    }

    @SneakyThrows
    public static Node selectNodeByPath(Node node, String path, Map namespace) {
        XPath x = node.createXPath(path);
        x.setNamespaceURIs(namespace);
        return x.selectSingleNode(node);
    }

    //  从以下结构的XML模版中读取配置
    //  [config]
    //      [key1] value1 [key1]
    //      [key2] value2 [key2]
    //  [config]
    @SneakyThrows
    public static String readValue(Document doc, String key) {
        Node node = doc.selectSingleNode("/config/" + key);
        return node.getStringValue().trim();
    }


    @SneakyThrows
    public static String readString(File file, String key) {
        Document doc = new SAXReader().read(file);
        Node node = doc.selectSingleNode("/config/" + key);
        return node.getStringValue().trim();
    }

    public static boolean readBool(File file, String key) {
        return Boolean.parseBoolean(readString(file, key));
    }

    public static int readInt(File file, String key) {
        return Integer.parseInt(readString(file, key));
    }

    public static float readFloat(File file, String key) {
        return Float.parseFloat(readString(file, key));
    }

    public static double readDouble(File file, String key) {
        return Double.parseDouble(readString(file, key));
    }

    @SneakyThrows
    public static String readString(InputStream is, String key) {
        Document doc = new SAXReader().read(is);
        Node node = doc.selectSingleNode("/config/" + key);
        return node.getStringValue().trim();
    }

    public static boolean readBool(InputStream is, String key) {
        return Boolean.parseBoolean(readString(is, key));
    }

    public static int readInt(InputStream is, String key) {
        return Integer.parseInt(readString(is, key));
    }

    public static float readFloat(InputStream is, String key) {
        return Float.parseFloat(readString(is, key));
    }

    public static double readDouble(InputStream is, String key) {
        return Double.parseDouble(readString(is, key));
    }

    //  向以下结构的XML模版中写入配置
    //  [config]
    //      [key1] value1 [key1]
    //      [key2] value2 [key2]
    //  [config]
    @SneakyThrows
    public static void writeObject(File file, Object key, Object value) {
        Document doc = new SAXReader().read(file);
        Node oldNode = doc.selectSingleNode("./config/" + key);
        if (oldNode != null) {
            DefaultElement baseNode = (DefaultElement) doc.selectSingleNode("./config");
            baseNode.remove(oldNode);
        }
        DOMElement newNode = new DOMElement(key.toString());
        newNode.addText(value.toString());
        doc.getRootElement().add(newNode);

        OutputStream out = new FileOutputStream(file);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        out.close();
        writer.close();
    }

    // 从以下结构的XML模版中读取二维数组
    // [config]
    //     [key len1="5" len2="2"]
    //         [item1]
    //             [item2] value1 [/item2]
    //             [item2] value2 [/item2]
    //         [/item1]
    //     [/key]
    // [config]
    @SneakyThrows
    public static String[][] readArray(File file, String key) {
        Document doc = new SAXReader().read(file);
        Node rootNode = doc.selectSingleNode("/config/" + key);
        int count1 = Integer.parseInt(rootNode.selectSingleNode("./@len1").getStringValue());
        int count2 = Integer.parseInt(rootNode.selectSingleNode("./@len2").getStringValue());
        String[][] array = new String[count1][count2];
        List<Node> aNodes = rootNode.selectNodes("./item1");
        for (int i = 0; i < count1; i++) {
            List<Node> bNodes = aNodes.get(i).selectNodes("./item2");
            for (int j = 0; j < count2; j++)
                array[i][j] = bNodes.get(j).getStringValue();
        }
        return array;
    }

    @SneakyThrows
    public static String[][] readArray(InputStream is, String key) {
        Document doc = new SAXReader().read(is);
        Node rootNode = doc.selectSingleNode("/config/" + key);
        int count1 = Integer.parseInt(rootNode.selectSingleNode("./@len1").getStringValue());
        int count2 = Integer.parseInt(rootNode.selectSingleNode("./@len2").getStringValue());
        String[][] array = new String[count1][count2];
        List<Node> aNodes = rootNode.selectNodes("./item1");
        for (int i = 0; i < count1; i++) {
            List<Node> bNodes = aNodes.get(i).selectNodes("./item2");
            for (int j = 0; j < count2; j++)
                array[i][j] = bNodes.get(j).getStringValue();
        }
        return array;
    }

    @SneakyThrows
    public static void writeArray(File file, String key, Object[][] array2, int len1, int len2) {
        Document doc = new SAXReader().read(file);
        Node oldNode = doc.selectSingleNode("./config/" + key);
        if (oldNode != null) {
            DefaultElement baseNode = (DefaultElement) doc.selectSingleNode("./config");
            baseNode.remove(oldNode);
        }
        DOMElement rootNode = new DOMElement(key);
        rootNode.setAttribute("len1", len1 + "");
        rootNode.setAttribute("len2", len2 + "");
        doc.getRootElement().add(rootNode);

        for (int i = 0; i < len1; i++) {
            DOMElement aNode = new DOMElement("item1");
            rootNode.add(aNode);
            for (int j = 0; j < len2; j++) {
                DOMElement bNode = new DOMElement("item2");
                bNode.setText(array2[i][j].toString());
                aNode.add(bNode);
            }
            DOMElement nNode = new DOMElement("index");
            nNode.setText(Maths.keepInt(i + 1, 3));
            aNode.add(nNode);
        }

        OutputStream out = new FileOutputStream(file);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        out.close();
        writer.close();
    }

    //将XML文档写入文件
    @SneakyThrows
    public static String writeToFile(Document doc, String file) {
        OutputStream out = new FileOutputStream(file);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        XMLWriter writer = new XMLWriter(out, format);
        writer.write(doc);
        out.close();
        writer.close();
        return file;
    }
}

