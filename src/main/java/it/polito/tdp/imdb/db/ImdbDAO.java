package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Insieme;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public void listAllActors(Map<Integer, Actor> idMap){
		String sql = "SELECT * FROM actors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(actor.getId(), actor);
				}
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<String> allGeneri(){
		String sql="SELECT DISTINCT g.genre "
				+ "FROM movies_genres g "
				+ "ORDER BY g.genre ";
		List<String>stampa=new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				stampa.add(res.getString("genre"));
			}
			conn.close();
			return stampa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List< Actor> getVertici(Map<Integer, Actor> idMap, String genere) {
		List<Actor>stampa=new ArrayList<>();
		String sql="SELECT DISTINCT a.id "
				+ "FROM movies_genres g,actors a, roles r "
				+ "WHERE a.id=r.actor_id AND r.movie_id=g.movie_id AND g.genre=? "
				+"ORDER BY a.last_name ";
				
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
            stampa.add(idMap.get(res.getInt("id")));
			}
			conn.close();
			return stampa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	
	}

	public List<Insieme> getArchi(String genere, Map<Integer, Actor> idMap) {
		String sql="SELECT a.actor_id AS id1, a2.actor_id AS id2,COUNT(*) AS peso "
				+ "FROM roles a,roles a2,movies_genres g "
				+ "WHERE a.actor_id>a2.actor_id AND a.movie_id=a2.movie_id AND a.movie_id=g.movie_id AND g.genre=? "
				+ "GROUP BY a.actor_id,a2.actor_id ";
		List<Insieme>stampa=new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
            stampa.add(new Insieme(idMap.get(res.getInt("id1")),idMap.get(res.getInt("id2")),res.getDouble("peso")));
			}
			conn.close();
			return stampa;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	
	
	
	
	
}