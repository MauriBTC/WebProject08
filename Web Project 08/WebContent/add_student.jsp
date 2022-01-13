<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="style.css" type="text/css" media="all" />
<title>Esempio 08</title>
<script>
function checkFields() {
	var familyName = document.getElementById("family_name");
	
	var familyNameText = familyName.value;
	
	if (familyNameText.length == 0 || familyNameText.length >= 50) {
		alert("Cognome non valido");
		
		return;
	}

	var name = document.getElementById("name");
	
	var nameText = name.value;
	
	if (nameText.length == 0 || nameText.length >= 50) {
		alert("Nome non valido");
		
		return;
	}
	
	var form = document.getElementById("form");
	
	form.submit();
}
</script>
</head>
<body>
	<h1>Esempio 08</h1>
	<h2>Aggiungi Studente</h2>
	<p>
		<a href="index.jsp">Indietro</a>
	</p>
	<div id="container">
		<form id="form" method="post" action="add_student">
			<table class="details_table">
				<tbody>
					<tr>
						<td>Cognome:</td>
					</tr>
					<tr>
						<td><input id="family_name" name="family_name" type="text" value="" /></td>
					</tr>
					<tr>
						<td>Nome:</td>
					</tr>
					<tr>
						<td><input id="name" name="name" type="text" value="" /></td>
					</tr>
					<tr>
						<td>
						<input type="button" value="Invia" onclick="checkFields()" />
						</td>
					</tr>
<%
	String errorMessage = (String) request.getAttribute("error_message");

	if (errorMessage != null && errorMessage.length() > 0) {
%>
					<tr>
					<td id="error_message"><%=errorMessage%></td>
					</tr>
<%
	} else {
%>
					<tr><td>&nbsp;</td></tr>
<%		
	}
%>					
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>
