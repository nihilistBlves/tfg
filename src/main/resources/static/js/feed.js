function wave(w) {

	var idPublicacion = w.id.slice(5,);

	var badge = w.previousSibling;

	if (w.style.color == "black") {
		w.style.color = "#f90d4e";
		badge.removeAttribute("class");
		badge.setAttribute("class", "badge badge-danger");
		w.previousSibling.innerText = parseInt(w.previousSibling.innerText) + 1;

		var parametros = {
			"idPublicacion": idPublicacion,
		}

		$(document).ready(function() {
			$.ajax({
				data: parametros,
				url: '/crearWave', //archivo que recibe la peticion
				type: 'post', //método de envio

			});
		});

	} else {
		w.style.color = "black";
		badge.removeAttribute("class");
		badge.setAttribute("class", "badge badge-secondary")
		w.previousSibling.innerText = parseInt(w.previousSibling.innerText) - 1;

		var parametros = {
			"idPublicacion": idPublicacion,
		}

		$(document).ready(function() {
			$.ajax({
				data: parametros, //datos que se envian a traves de ajax
				url: '/borrarWave', //archivo que recibe la peticion
				type: 'post', //método de envio

			});
		});

	}

}

function llamada(divsComentarios, idPublicacion, contador) {
	var parametros = {
		"idPublicacion": idPublicacion,
	}
	$(document).ready(function() {
		$.ajax({
			async: "true",
			data: parametros,
			url: "/verComentarios",
			type: "get",
			success: function(comentarios) {
				if (comentarios == "") {
					divsComentarios[contador].style.height = "70px";
					divsComentarios[contador].innerHTML = "No hay comentarios en esta publicación";
				} else {
					divsComentarios[contador].style.height = "210px";
					divsComentarios[contador].innerHTML = comentarios;
				}


			}

		});
	});
}

window.onload = function() {
	var divsComentarios = document.getElementsByClassName("comentarioss");
	for (var i = 0; i < divsComentarios.length; i++) {
		var idPublicacion = divsComentarios[i].parentNode.children[0].children[2].value;
		llamada(divsComentarios, idPublicacion, i);
	}
}



function comentar(btn) {
	var comentario = btn.previousSibling.previousSibling.value;
	console.log(comentario);
	btn.previousSibling.previousSibling.value = "";

	var idPublicacion = btn.previousSibling.id;

	var divReal = btn.parentNode.parentNode.lastElementChild;
	var parametros = {
		"comentario": comentario,
		"idPublicacion": idPublicacion,
	}
	console.log(divReal);

	$(document).ready(function() {

		$.ajax({
			data: parametros,
			url: "/crearComentario",
			type: "post",
			success: function(comentarios) {
				console.log(comentarios);
				divReal.innerHTML = comentarios;

			}

		});
	});
}

