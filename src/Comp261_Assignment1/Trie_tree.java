package Comp261_Assignment1;
import java.util.*;

public class Trie_tree {

	private List<Create_Roads> roads = new ArrayList<Create_Roads>();
	private Map<Character,Trie_tree> RoadNames = new HashMap<Character, Trie_tree>();
	
	public Trie_tree addNode(Character character, Trie_tree node){ // Add a new node to the children list
		Trie_tree newNode = new Trie_tree();
		RoadNames.put(character, newNode);
		return newNode;
		}
	
	
	public void addRoad(Create_Roads road){//loops through the string and adds node as required
		Trie_tree node = this;
		for (Character c : road.getName().toCharArray()){
			if (node.RoadNames.containsKey(c)){
				node = node.RoadNames.get(c);
			} else {
				node = node.addNode(c, node);
			}
		}
		node.roads.add(road);
	}
	 
	public List<Create_Roads> getRoads(){//gets all the children of the current node
		List<Create_Roads> RoadSubList = this.roads;
		for (Trie_tree road : RoadNames.values()){
			roads.addAll(road.getRoads());
		}
		return RoadSubList;
	}

	public Trie_tree FindRoad(String name){//finds roads containing matching characters in the correct order and position
		Trie_tree road = this;
		for (Character c : name.toCharArray()){
			if (road.RoadNames.containsKey(c)){
				road = road.RoadNames.get(c);
			} else {
				return null;
			}
		}
		return road;
	}
}