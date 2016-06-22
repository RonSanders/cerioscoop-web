package nl.cerios.cerioscoop.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import nl.cerios.cerioscoop.domain.Film;
import nl.cerios.cerioscoop.domain.Showing;
import nl.cerios.cerioscoop.util.DateUtils;

public class ShowingService {

	private static final  Connection CONNECTION = DatabaseConnection.connectionDatabase();
	
	/*TODO rename the SQL field names to full caps names
	 * Example:
	 * film_id 
	 * 
	 * should be:
	 * FILM_ID
	 * 
	 * But the names in the database have to match the new full-caps names to work.
	 * 
	 * 
	 * TODO replace the "System.out.println" with an other alternative.
	 * Once this application is on a server there is no way the client could see those comments
	 * since those will be printed on the server not the client side.
	 * */
	
	public List<Film> getFilms(){
		try {
			final List<Film> films = new ArrayList<>();
			final Statement statement = CONNECTION.createStatement();
			final ResultSet resultSet = statement.executeQuery("select film_id, name, minutes, type, language from film");
			while (resultSet.next()) {
	        	final int filmId = resultSet.getInt("film_id");
	        	final int minutes = resultSet.getInt("minutes");
	        	final int movieType = resultSet.getInt("type");
	        	final String movieName = resultSet.getString("name");
	        	final String language = resultSet.getString("language");
	        	
	        	films.add(new Film(filmId, movieName, minutes, movieType, language));
			}
			return films;
	    }catch (final SQLException e) {
	    	throw new ShowingServiceException("Something went terribly wrong while retrieving the first date.", e);
	    }
	}
	
	public List<Showing> getShowings(){
		final List<Showing> showings = new ArrayList<>();
		try {
			final Statement statement = CONNECTION.createStatement();
			final ResultSet resultSet = statement.executeQuery("select showing_id, film_id, room_id, premiere_date, premiere_time, last_showing_date, last_showing_time from showing"); { 
			while (resultSet.next()) {
				final int showingId = resultSet.getInt("showing_id");
				final int filmId = resultSet.getInt("film_id");
				final int roomId = resultSet.getInt("room_id");
				final Date premiereDate = resultSet.getDate("premiere_date");
				final Time premiereTime = resultSet.getTime("premiere_time");
				final Date lastShowingDate = resultSet.getDate("last_showing_date");
				final Time lastShowingTime = resultSet.getTime("last_showing_time");
				
	        	showings.add(new Showing(showingId, filmId, roomId, premiereDate, premiereTime, lastShowingDate, lastShowingTime));
	        	}
	        return showings;
	      }
	    }catch (final SQLException e) {
	    	throw new ShowingServiceException("Something went terribly wrong while retrieving the first date.", e);
	    }
	}
	
	/**
	 * Returns a first showing record.
	 * 
	 * @return firstShowing
	 */
	public Showing getFirstShowingAfterCurrentDate(){
		final List<Showing> showings = getShowings();
		final DateUtils dateUtils = new DateUtils();
		Showing firstShowing = null;
		
		for (final Showing showing : showings) {
			if(showing.getPremiereDate().after(dateUtils.getCurrentSqlDate())){	
				if(firstShowing == null){			//hier wordt voor 1x eerstVolgendeFilm gevuld					
					firstShowing = showing;
				}
				else if(showing.getPremiereDate().before(firstShowing.getPremiereDate())){
					firstShowing = showing;			
				}
			}
		}
		System.out.println(firstShowing);
		return firstShowing;
	}
		
	public Film getFilmByFilmId(final int filmId) throws FilmNotFoundException {
		final List<Film> films = getFilms();
		Film filmByFilmId = null;
		
		for (final Film filmItem : films){
			if(filmItem.getFilmId() == filmId){
				filmByFilmId = filmItem;
			}
		}
		return filmByFilmId;
	}
	
	
	
	public void addFilm(final String newFilmName, final int minutes, final int movieType, final String language) {
		try {
			final PreparedStatement preparedStatement = CONNECTION.prepareStatement(
					"INSERT INTO film (name, minutes, type, language) values (?,?,?,?);");
			preparedStatement.setString(1, newFilmName);
			preparedStatement.setInt(2, minutes);
			preparedStatement.setInt(3, movieType);
			preparedStatement.setString(4, language);
			preparedStatement.executeUpdate();
			
			System.out.println("Data inserted.");
	    }catch (final SQLException e) {
	    	throw new ShowingServiceException("Something went wrong while inserting the filmagenda items.", e);
	    }
	}
	
	public void addShowing(final int filmId, final int roomId, final Date premiereDate, final Time premiereTime, final Date lastShowingDate, final Time lastShowingTime){		
		try {
			final PreparedStatement preparedStatement = CONNECTION.prepareStatement(
					"INSERT INTO showing (film_id, room_id, premiere_date, premiere_time, last_showing_date, last_showing_time) values (?,?,?,?,?,?);");
        	preparedStatement.setInt(1, filmId);
        	preparedStatement.setInt(2, roomId);
        	preparedStatement.setDate(3, premiereDate);
        	preparedStatement.setTime(4, premiereTime);
        	preparedStatement.setDate(5, lastShowingDate);
        	preparedStatement.setTime(6, lastShowingTime);
        	preparedStatement.executeUpdate();
        	
        	System.out.println("Data inserted.");
	    }catch (final SQLException e) {
	    	throw new ShowingServiceException("Something went wrong while inserting the filmagenda items.", e);
	    }
	}
}

