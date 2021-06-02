

$(document).ready(function() {

$("#opciones").load("/editarPerfil");

	$("#perfil").on("click", function() {

		$("#opciones").load("/editarPerfil");


	});

	$("#pass").on("click", function() {

		$("#opciones").load("/editarPass");

	});

	$("#correo").on("click", function() {

		$("#opciones").load("/editarCorreo");

	});
	$("#seg").on("click", function() {

		$("#opciones").load("/seguidoresSeguidos");

	});

	$("#waves").on("click", function() {

		$("#opciones").load("/tasaWaves");

	});

	$("#favs").on("click", function() {

		$("#opciones").load("/publicacionesFavoritas");

	});

	$("#cuenta").on("click", function() {

		$("#opciones").load("/editarCuenta");

	});

	$("#notificaciones").on("click", function() {

		$("#opciones").load("/notificaciones");

	});



});

