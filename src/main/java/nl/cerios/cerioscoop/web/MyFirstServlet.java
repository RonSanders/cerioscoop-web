package nl.cerios.cerioscoop.web;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cerios.cerioscoop.domain.Film;
import nl.cerios.cerioscoop.service.MotionpictureService;
import nl.cerios.cerioscoop.util.DateUtils;

/**
 * Servlet implementation class MyFirstServlet
 * 
 * http://stackoverflow.com/questions/2349633/doget-and-dopost-in-servlets
 */
@WebServlet("/MyFirstServlet")
public class MyFirstServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static DateUtils DU = new DateUtils();
	private static MotionpictureService MPS = new MotionpictureService();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * R40><link href='/cerioscoop-web/cerioscoop.css' type='text/css' rel='stylesheet' />
	 * LET OP:		projectNaam/cssFileNaam.css		zo verwijs je naar de juiste locatie!
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Film> items = MPS.getFilms();
		Film items2 = MPS.getFirstFilmAfterCurrentDate();
		java.util.Date premMoment = null;
		String premMomentDB = null;
		
		items.sort(new Comparator<Film>() {

			@Override
			public int compare(Film itemL, Film itemR) {  //is itemL groter dan itemR? anders bovenaan
				if (itemL.getPremiereDatum().before(itemR.getPremiereDatum())) {
					return -1;
				} else if (itemL.getPremiereDatum().after(itemR.getPremiereDatum())) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		StringBuilder html = new StringBuilder("<!DOCTYPE html>")
			        .append("<html>")
			        .append("<head><title>Filmagenda</title><link href='/cerioscoop-web/cerioscoop.css' type='text/css' rel='stylesheet' /></head>")
			        .append("<body><h1>Voorstellingen</h1>")
			        .append("<table>")
			        .append("<thead><th>Filmtitel</th><th>speelt op:</th><th>tijd</th></thead>")
			        .append("<tbody>");
			    for (Film item : items) {
				html.append("<tr><td>")
					.append(item.getNaam())
					.append("</td><td>")
					.append(DU.format(item.getPremiereDatum()))
					.append("</td><td>")
					.append(DU.formatTijd(item.getPremiereTijd()))
					.append("</td></tr>");
					}
			    html.append("</tbody>")
					.append("</table>");
			   // for (Film item : items) {
			    	premMomentDB = DU.formatHeidi(items2.getPremiereDatum())+" "+DU.formatTijd(items2.getPremiereTijd());
					try {
						premMoment = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(premMomentDB);
					} catch (ParseException e) {
						throw new MyFirstServletException("Something went wrong while parsing premiere datum.", e);
					}
			    html.append("</tbody>")
					.append("</table>")
					.append("<h1></h1>")
					.append("<p>Vandaag is het " +DU.getDate())
					.append("<br />De eerstvolgende film: "+items2.getNaam() +" is op "+DU.format2(items2.getPremiereDatum())+" om "+DU.formatTijd(items2.getPremiereTijd()))
					.append("<br />Dat is over "+ DU.calculateTime(DU.getSecondsBetween(premMoment, DU.getCurrentDate())) +"</p>");
			//    	}
			    html.append("</body>")
			        .append("</html>");

			    response.setContentType("text/html;charset=UTF-8");
			    response.getWriter().write(html.toString());		    
	}
	
	/**
	 *	Posts the film opslaan form in the database. 
	 *
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("submitit").equals("Submit")) {
			String filmname = (request.getParameter("filmname"));
			int minutes = Integer.parseInt(request.getParameter("minutes"));
			int type = Integer.parseInt(request.getParameter("type"));
			String language = request.getParameter("language");		
			MPS.registerFilm(filmname, minutes, type, language);
		}
		doGet(request, response);
	}
}