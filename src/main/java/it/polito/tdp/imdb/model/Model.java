package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;


import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private SimpleWeightedGraph<Actor,DefaultWeightedEdge>grafo;
	private ImdbDAO dao;
	private Map<Integer,Actor>idMap;
	
	public Model() {
		dao=new ImdbDAO();
		idMap=new HashMap<>();
		dao.listAllActors(idMap);
	}
	
	public List<String>generi(){
		return dao.allGeneri();
	}
	
	public void creaGrafo(String genere) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		//vertici
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,genere));
		//archi
		for(Insieme i:dao.getArchi(genere,idMap)) {
			if(grafo.containsVertex(i.getA1())&&grafo.containsVertex(i.getA2())) {
				DefaultWeightedEdge e=grafo.getEdge(i.getA1(), i.getA2());
				if(e==null) {
					Graphs.addEdgeWithVertices(grafo,i.getA1(), i.getA2(), i.getPeso());
				}
			}
		}
	}
	public int getNVertici() {
		if(grafo != null)
		return grafo.vertexSet().size();
		return 0;}
					
		public int getNArchi() {
		if(grafo != null)
		return grafo.edgeSet().size();
		return 0;}
		
		// vertici per la comboBox
				public Set <Actor>getCombo(){
					return grafo.vertexSet();
				}
				
		//verifico se esiste un grafo

				public SimpleWeightedGraph<?,DefaultWeightedEdge>getGrafo(){
				return grafo;}
				
		//raggiungibili
				public List<Actor>raggiungibili(Actor partenza){
					List<Actor>stampa=new ArrayList<>();
					BreadthFirstIterator<Actor,DefaultWeightedEdge>bfv=new BreadthFirstIterator<>(grafo,partenza);
					while(bfv.hasNext()) {
						Actor a=bfv.next();
						stampa.add(a);
						a=bfv.getParent(a);
					}
					Collections.sort(stampa);
					return stampa;
				}

		}
	

