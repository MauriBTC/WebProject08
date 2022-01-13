<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="style.css" type="text/css" media="all" />
<title>Example 08</title>
<script>
function fillTable(response) {
	var table = document.getElementById("table"); //table è l'id del tag della tbody infondo

	var html = "";

	for (var i = 0; i < response.length; i++) { //ogg response è stato metavalutato, ovvero costruito tramite la stringa che rappresenta il sorgente javascript
		var student = response.students[i];

		html += "<tr>";

		html += "<td>" + student.id + "</td>";

		html += "<td>" + student.familyName + "</td>";

		html += "<td>" + student.name + "</td>";

		html += "</tr>";
	}

	table.innerHTML = html; //innerHTML rappresenta tutto il testo contenuto in un tag, compresi altri sottotag e sottoforma di stringa
}

/*
 * javascript è un ling fuzionale quindi offre il supporto alla metaprogrammazione: posso passargli una stringa che rappresenta del codice
 * ed eseguirla come se fosse del codice.
 * La differenza rispetto gli altri ling funzionali sta nel fatto che nella metaprogrammazione di Javascript vengono usate proprio le stringhe:
 * data una stringa si può chiedere all'interprete di javascript di eseguirla (e va avanti finchè trova testo corretto) --> questo è l'unico 
 * motivo del formato di dati JSON, siccome è un formato che viene capito dagli interpreti javascript il quale supporta la metaprogrammazione
 * partendo da delle stringhe, posso prendere il testo della risposta (request.responseText) scrivergli davanti "var response =", fare eval()
 * che prende una stringa e la valuta come se fosse un sorgente e questo mi fa comparire la variabile response (ovvero l'ogg formato
 * dall'insieme di coppie chiave-valore). 
 * filtable riempie il testo html con i valori della tabella. 
 * In questo modo riempio il tag body con id="table" (infondo) con del nuovo codice html.
 * Inoltre javascript nel browser naviga, per così dire, in una sandbox, nel senso che non può leggere cose su disco però può leggere dei cookie,
 * quindi fare attenzione che il proprio server dichiari un valore e non esegue del codice altrimenti rischiamo che l'utente veda cose strane.
 * 
 */

function sendRequest(familyName, name) {
	var request = new XMLHttpRequest(); //costruisce l'ogg XMLHttpRequest (fingo esistano le classi) che serve per fare richiesta ASINCRONA sul canale http

	request.onreadystatechange = function() { //uso funzione come ogg --> viene dalla programmazione funzionale (javascript ling funzionale)
		if (request.readyState == 4 && request.status == 200) { //readyState=4 se a buon fine (ultimo stato raggiunto), status=ritornato dalla chiamata http, 200 sta per ok nel prot http
			var text = request.responseText;

			text = "var response = " + text;

			eval(text);

			fillTable(response);
		}
	};

	request.open("post", "search_students", true); //arg: metodo http post; indico la url a cui inviare i dati; true=la richiesta è asincrona, non mi blocco in attesa della risp

	request.setRequestHeader("Content-type", //nell'header della richiesta metto content-type + quella cosa
		"application/x-www-form-urlencoded"); //se non si specifica nulla, la form invia i dati con questo formato

	request.send("family_name=" + encodeURI(familyName) + "&name="
		+ encodeURI(name));
}

function fieldChanged() {
	var node = document.getElementById("family_name");

	var familyName = node.value;

	node = document.getElementById("name");

	var name = node.value;

	sendRequest(familyName, name); //la sendrequest userà le stringhe come filtri
}

function initialize() {
	sendRequest("", "");
}
</script>
</head>
<body onload="initialize()">
	<h1>Esempio 08</h1>
	<h2>Elenco degli Studenti</h2>
	<p>
		<a href="index.jsp">Indietro</a>
	</p>
	<div id="container">
		<form id="form" action="" method="post">
			<table class="details_table">
				<tbody>
					<tr>
						<td>Cognome:</td>
					</tr>
					<tr>
						<td><input id="family_name" name="family_name" type="text"
							value="" oninput="fieldChanged()" /></td>
					</tr>
					<tr>
						<td>Nome:</td>
					</tr>
					<tr>
						<td><input id="name" name="name" type="text" value=""
							oninput="fieldChanged()" /></td>
					</tr>
					<tr><td>&nbsp;</td></tr>
				</tbody>
			</table>
		</form>
		<br />
		<table class="student_table">
			<thead>
				<tr>
					<th class="column33">Matricola</th>
					<th class="column33">Cognome</th>
					<th class="column33">Nome</th>
				</tr>
			</thead>
			<tbody id="table">
			</tbody>
		</table>
	</div>
</body>
</html>
