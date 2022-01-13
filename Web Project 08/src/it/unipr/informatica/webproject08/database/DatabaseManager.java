package it.unipr.informatica.webproject08.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.unipr.informatica.webproject08.database.DatabaseStudent;
import it.unipr.informatica.webproject08.model.Student;

public class DatabaseManager {
private String url;
	
	private String driverClassName;
	
	public DatabaseManager() {
		try {
			ResourceBundle configuration = ResourceBundle.getBundle("configuration");

			driverClassName = configuration.getString("it.unipr.informatica.webproject08.database.driver");
		
			url = configuration.getString("it.unipr.informatica.webproject08.database.url");

			Class.forName(driverClassName); //consente di caricare in memoria la classe del driver e, per convenzione di JDBC, attivarla (sfrutta la Reflection, introspezione)
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
	
	public List<Student> getAllStudents() throws SQLException {
		try (
			Connection connection = DriverManager.getConnection(url);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT * FROM Studenti ORDER BY cognome ASC")
		) {
			List<Student> result = new ArrayList<>();
			
			while(resultSet.next()) {
				int id = resultSet.getInt("matricola");
				
				String familyName = resultSet.getString("cognome");

				String name = resultSet.getString("nome");

				Student student = new DatabaseStudent(id, familyName, name);
				
				result.add(student);
			}
			
			return result;
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			
			throw sqlException;
		}
	}
	
	public List<Student> getStudents(String familyName, String name) throws SQLException { //ritorna una lista di tutti gli studenti il cui nome o cognome contiene le lettere in input
		try (
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement preparedStatement = connection.prepareStatement("SELECT matricola, cognome, nome FROM Studenti WHERE UPPER(cognome) LIKE ? AND UPPER(nome) LIKE ? ORDER BY cognome ASC")
		) {
			if(familyName == null)
				familyName = "";

			familyName = familyName.trim();
			
			familyName = "%" + familyName.toUpperCase() + "%"; // % per dire qualsiasi sottostringa arbitrariamente lunga

			if(name == null)
				name = "";

			name = name.trim();
						
			name = "%" + name.toUpperCase() + "%";

			preparedStatement.setString(1, familyName);

			preparedStatement.setString(2, name);

			ResultSet resultSet = preparedStatement.executeQuery();
			
			List<Student> result = new ArrayList<>();
			
			while(resultSet.next()) {
				int id = resultSet.getInt("matricola");
				
				familyName = resultSet.getString("cognome");
				
				name = resultSet.getString("nome");
				
				Student student = new DatabaseStudent(id, familyName, name);
				
				result.add(student);
			}
			
			return result;
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			
			throw sqlException;
		}
	}

	public Student addStudent(String familyName, String name) throws SQLException {
		try (
			Connection connection = DriverManager.getConnection(url);
			PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Studenti(cognome, nome) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)
		) {
			if(familyName == null)
				throw new SQLException("invalid familyName");

			familyName = familyName.trim();
			
			if(familyName.length() == 0 || familyName.length() >= 50)
				throw new SQLException("invalid familyName");

			if(name == null)
				throw new SQLException("invalid name");

			name = name.trim();
			
			if(name.length() == 0 || name.length() >= 50)
				throw new SQLException("invalid name");
			
			preparedStatement.setString(1, familyName);

			preparedStatement.setString(2, name);

			preparedStatement.execute();
			
			ResultSet keys = preparedStatement.getGeneratedKeys();
			
			keys.next();
			
			int id = keys.getInt(1);
			
			return new DatabaseStudent(id, familyName, name);
		} catch (SQLException sqlException) {
			sqlException.printStackTrace();
			
			throw sqlException;
		}
	}
}

