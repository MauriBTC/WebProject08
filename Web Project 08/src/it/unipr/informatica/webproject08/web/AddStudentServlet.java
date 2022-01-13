package it.unipr.informatica.webproject08.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.unipr.informatica.webproject08.database.DatabaseManager;

/**
 * Servlet implementation class AddStudentServlet
 */
@WebServlet("/add_student")
@SuppressWarnings("serial")
public class AddStudentServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {			
			HttpSession session = request.getSession();

			DatabaseManager databaseManager = null;
			
			synchronized(session) {
				databaseManager = (DatabaseManager)session.getAttribute("database_manager");
			
				if(databaseManager == null) {
					databaseManager = new DatabaseManager();
				
					session.setAttribute("database_manager", databaseManager);
				}
			}
			
			String familyName = request.getParameter("family_name");

			String name = request.getParameter("name");

			databaseManager.addStudent(familyName, name);
			
			request.getRequestDispatcher("index.jsp").forward(request, response);
		} catch(Throwable throwable) {
			throwable.printStackTrace();
			
			request.setAttribute("error_message", "Impossibile aggiungere lo studente");
			
			request.getRequestDispatcher("add_student.jsp").forward(request, response);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
