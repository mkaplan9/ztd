package mapbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class parses the two types of xml we use for the application: address xml and box xml.
 * As such, it has two main methods, parseBox and parseAddress. It has several helper methods
 * as well, plus getters to access the parsed info. Note that in this class it is important
 * to differentiate between xml nodes, OSM nodes, and MapNodes
 * @author mmkaplan
 *
 */
public class XmlParser {
	
	private List<MapNode> mapnodes;
	private List<MapWay> mapways;
	private List<MapWay> highways;
	private HashMap<String, MapNode> mnhash;
	private boolean doneparsebox;
	
	public XmlParser() {
		mapnodes = new ArrayList<MapNode>(0);
		mapways = new ArrayList<MapWay>(0);
		mnhash = new HashMap<String, MapNode>();
		highways = new ArrayList<MapWay>(0);
		doneparsebox = false;
	}
	
	/**
	 * This method parses the box info that OSM returns into MapNodes and MapWays
	 * @param xmlfile All info contained in a box defined by lat and lon bounds
	 */
	public void parseBox(File xmlfile) {
		try {
			//Start up the DOM
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			
			NodeList nnodes = doc.getElementsByTagName("node"); //Make a list of all elements marked "node"
			//For each xml node
			for (int i = 0; i < nnodes.getLength(); i++) {
				Node node = nnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) node;
					MapNode mn = new MapNode(el.getAttribute("id"), Double.parseDouble(el.getAttribute("lat")),
							Double.parseDouble(el.getAttribute("lon"))); //Pull out and save desired attributes
					mnhash.put(mn.id, mn);
					mapnodes.add(mn);
				}
			}
			
			
			NodeList wnodes = doc.getElementsByTagName("way");  //Make a list of all elements marked "way"
			//For each xml way
			for (int i = 0; i < wnodes.getLength(); i++) {
				Node node = wnodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) node;
					MapWay mw = new MapWay(el.getAttribute("id"));

					List<String> mapnodeids = XmlParser.getMapNodes("nd", el); //Get the nodes on this way
					for (String id: mapnodeids) {
						MapNode n = mnhash.get(id);
						if ((n = mnhash.get(id)) == null) {
							System.out.println("ERROR: Ways and nodes do not match in input file");
						}
						else { //Add node to way and way to node
							mw.addNode(n);
							n.addWay(mw);
						}
					}
					
					mapways.add(mw);
					
					//Add to highways if it is a highway
					if (XmlParser.isHighway("tag", el) == true) {
						highways.add(mw);
					}
					
				}
			}
			
			System.out.println("Node hashmap size " + mnhash.size());
			System.out.println("Node list size " + mapnodes.size());
			System.out.println("Way list size " + mapways.size());
			System.out.println("Highways size " + highways.size());
			doneparsebox = true;
		
		}
		catch (NumberFormatException nfe) {
			System.out.println("ERROR: Bad input file");
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseBox");
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * This helper method returns the map nodes associated with a given way
	 * @param tag In this case it is "nd"
	 * @param e The way we wish to examine
	 * @return A list of nodes for the given way
	 */
	private static List<String> getMapNodes(String tag, Element e) {
		List<String> res = new ArrayList<String>(0);
		NodeList nl = e.getElementsByTagName(tag);
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				res.add(el.getAttribute("ref"));
			}
		}
		return res;
	}
	
	/**
	 * This helper method returns true if the given way is a highway, else false. note
	 * that a "highway" for OSM purposes is defined as any type of road, street, or path
	 * @param tag In this case it is "tag"
	 * @param e The way we wish to examine
	 * @return True if the way is a highway, else false
	 */
	private static boolean isHighway(String tag, Element e) {
		boolean markhighway = false;
		List<String> res = new ArrayList<String>(0);
		NodeList nl = e.getElementsByTagName(tag);
		//Add all "tag" attributes
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) n;
				res.add(el.getAttribute("k"));
			}
		}
		//Iterate through tags searching for highway, mark if found
		for (String s: res) {
			if (s.equals("highway")) {
				markhighway = true;
			}
		}
		return markhighway;
	}
	
	
	

	public MapNode parseAddress(File xmlfile) {
		
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xmlfile);
			doc.getDocumentElement().normalize();
			NodeList nnodes = doc.getElementsByTagName("searchresults");
			
			Node node = nnodes.item(0);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element) node;
				String place_id = getAddInfo("place", "place_id", el);
				double lat = Double.parseDouble(getAddInfo("place", "lat", el));
				double lon = Double.parseDouble(getAddInfo("place", "lon", el));
				MapNode mn = new MapNode(place_id, lat, lon);
				System.out.println("Node ID: " + mn.id);
				System.out.println("Centlat: " + mn.lat);
				System.out.println("CentLon: " + mn.lon);
				return mn;
			}
			else {
				//Dialogue box needed
				System.out.println("ERROR: Could not find address");
			}
		}
		catch (Exception e) {
			System.out.println("ERROR: Problem in parseAddress");
			e.printStackTrace();
		}
		return null;
		
	}
	
	private static String getAddInfo(String tag, String att, Element e) {
		NodeList nl = e.getElementsByTagName(tag);
		Node n = nl.item(0);
		String res = null;
		if (n.getNodeType() == Node.ELEMENT_NODE) {
			Element el = (Element) n;
			res = el.getAttribute(att);
		}
		return res;
	}
	
	
	
	//TODO Make these contingent on doneparsing
	
	public List<MapWay> getWays() {
		return mapways;
	}
	
	public List<MapWay> getHighs() {
		return highways;
	}
	
	public List<MapNode> getNodes() {
		return mapnodes;
	}
	
	public HashMap<String, MapNode> getNodesHash() {
		return mnhash;
	}

}
