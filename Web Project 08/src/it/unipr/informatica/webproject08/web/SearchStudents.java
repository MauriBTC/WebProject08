package it.unipr.informatica.webproject08.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unipr.informatica.webproject08.database.DatabaseManager;
import it.unipr.informatica.webproject08.model.Student;

/*
 * questa servlet è una piccola variante della primissima servlet che produceva html a forza di print, ma questa volta
 * non produce html ma del JSON e per questo la eval() ha senso, permette di trasformare ciò che è stato ottenuto
 * in dati da manipolare all'interno di javascript.
 */
@SuppressWarnings("serial")
@WebServlet("/search_students")
public class SearchStudents extends HttpServlet {	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		DatabaseManager databaseManager = null;
		
		/*
		 * la sincro sulla richiesta è troppo poco, possono esserci più richieste per la stessa sessione
		 * quindi faccio sincro sulla sessione che è unica per tutta la sessione (almeno finchè ho un'unica sessione per uno stesso webserver)
		 */
		synchronized(session) {
			databaseManager = (DatabaseManager)session.getAttribute("database_manager");
		
			if(databaseManager == null) {
				databaseManager = new DatabaseManager();
			
				session.setAttribute("database_manager", databaseManager);
			}
		}
		
		response.setContentType("text/plain");

		PrintWriter out = new PrintWriter(response.getOutputStream());

		out.println("{");

		try {						
			String familyName = request.getParameter("family_name");

			String name = request.getParameter("name");
			
			List<Student> students = databaseManager.getStudents(familyName, name);
			
			out.println("\"length\" : " + students.size() + ", ");
			
			out.println("\"students\" : [");
			
			for(int i = 0; i < students.size(); i++) {
				out.println("{");

				Student student = students.get(i);
				
				out.println("\"id\" : " + student.getId() + ", ");

				out.println("\"familyName\" : \"" + student.getFamilyName().replace("\"", "\\\"") + "\", ");

				out.println("\"name\" : \"" + student.getName().replace("\"", "\\\"") + "\" ");

				out.println("}");
				
				if(i != students.size() - 1)
					out.println(", ");
			}
				
			out.println("]");
		} catch(Throwable throwable) {
			out.println("\"error\" : \"Errore interno, prego riprovare\"");

			throwable.printStackTrace();
		}
		
		out.println("}");

		out.flush();
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}

