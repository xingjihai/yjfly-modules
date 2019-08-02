package cn.ejfy.web.tools.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLUtils2 {

    // XML loading and saving methods for module

    // The required DTD URI for exported module
    private static final String MODULE_DTD_URI = "http://java.sun.com/dtd/module.dtd";

    private static final String MODULE_DTD =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<!-- DTD for module -->"                +
        "<!ELEMENT module (name,description,version, configuration) >" +
        "<!ELEMENT name (#PCDATA) >" +
        "<!ELEMENT description (#PCDATA) >" +
        "<!ELEMENT version (#PCDATA) >" +
        "<!ELEMENT configuration (packages)>" +
        "<!ELEMENT packages (package*)>" +
        "<!ELEMENT package (#PCDATA) >";


    /**
     * Version number for the format of exported properties files.
     */

    public static Map<String, Object> load(InputStream in)
        throws IOException
    {
        Document doc = null;
        try {
            doc = getLoadingDoc(in);
        } catch (SAXException saxe) {
            throw new IOException(saxe);
        }
        Element propertiesElement = doc.getDocumentElement();
//        String xmlVersion = propertiesElement.getAttribute("version");
//        if (xmlVersion.compareTo(EXTERNAL_XML_VERSION) > 0)
//            throw new InvalidModuleFormatException(
//                "Exported Module file format version " + xmlVersion +
//                " is not supported. This java installation can read" +
//                " versions " + EXTERNAL_XML_VERSION + " or older. You" +
//                " may need to install a newer version of JDK.");
        Map<String, Object> props = new HashMap();

        importProperties(null, props, propertiesElement);

        return props;
    }

    static Document getLoadingDoc(InputStream in)
        throws SAXException, IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        dbf.setValidating(false);
        dbf.setCoalescing(true);
        dbf.setIgnoringComments(true);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(new Resolver());
            db.setErrorHandler(new EH());
            InputSource is = new InputSource(in);
            return db.parse(is);
        } catch (ParserConfigurationException x) {
            throw new Error(x);
        }
    }

    private static boolean isList(String key){
        if(key == null){
            return false;
        }
        String ks[] = key.split("[.]");
        int length = ks.length;
        if(length > 1 && ks[length - 2].equals(ks[length - 1]+"s")){
            return true;
        }

        return false;
    }


    @SuppressWarnings("unchecked")
	static void importProperties(String parentName, Map<String, Object> props, Node element) {

        String thisName = element.getNodeName();

        NodeList list = element.getChildNodes();
        for(int i = 0; i < list.getLength(); i++){
            Node node = list.item(i);

            Node first = node.getFirstChild();
            if(first != null && "#text".equals(first.getNodeName())){
                String key = thisName + "." +  node.getNodeName();
                if(parentName != null){
                    key = parentName + "." + key;
                }
                Object obj = props.get(key);
                if(isList(key)){
                    List<String> tmp = (List<String>)obj;
                    if(tmp == null){
                        tmp = new ArrayList();
                    }
                    tmp.add(node.getTextContent());

                    props.put(key, tmp);

                }else{
                    props.put(key, node.getTextContent());
                }

            }else{
                if(parentName == null){
                    importProperties(thisName,props,  node);
                }else{
                    importProperties(parentName + "."+ thisName,props,  node);
                   // props.put(parentName + "." + thisName + "." + node.getNodeName(), node.getTextContent());
                }
            }
        }


    }

    /*
    static void save(Map<String, Object> props, OutputStream os, String comment,
                     String encoding)
        throws IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            assert(false);
        }
        Document doc = db.newDocument();
        Element properties =  (Element)
            doc.appendChild(doc.createElement("sys"));

        synchronized (props) {
            for (String key : props.keySet()) {
                Element entry = (Element)properties.appendChild(
                    doc.createElement(key));
//                entry.setAttribute("key", key);
                Object obj = props.get(key);
                if(obj instanceof String){

                }else if(obj instanceof Map){

                }else if(obj instanceof List){

                }
                entry.appendChild(doc.createTextNode(props.get(key)));
            }
        }
        emitDocument(doc, os, encoding);
    }*/

    /*
    static void emitDocument(Document doc, OutputStream os, String encoding)
        throws IOException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, PROPS_DTD_URI);
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, encoding);
        } catch (TransformerConfigurationException tce) {
            assert(false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(os);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            IOException ioe = new IOException();
            ioe.initCause(te);
            throw ioe;
        }
    }
    */

    private static class Resolver implements EntityResolver {
        public InputSource resolveEntity(String pid, String sid)
            throws SAXException
        {
            if (sid.equals(MODULE_DTD_URI)) {
                InputSource is;
                is = new InputSource(new StringReader(MODULE_DTD));
                is.setSystemId(MODULE_DTD_URI);
                return is;
            }
            throw new SAXException("Invalid system identifier: " + sid);
        }
    }

    private static class EH implements ErrorHandler {
        public void error(SAXParseException x) throws SAXException {
            throw x;
        }
        public void fatalError(SAXParseException x) throws SAXException {
            throw x;
        }
        public void warning(SAXParseException x) throws SAXException {
            throw x;
        }
    }

    public static void main(String[] args) {
        try {
            File file=new File("C:\\develop\\workspace_idea\\ejfyweb\\module-tools\\src\\main\\java\\cn\\ejfy\\web\\tools\\xml\\test\\test3.xml");
            InputStream inputStream=new FileInputStream(file);
            Map<String, Object> map=load(inputStream);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}