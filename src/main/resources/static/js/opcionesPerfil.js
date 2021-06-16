

$(document).ready(function() {
	

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

	$("#cuenta").on("click", function() {

		$("#opciones").load("/editarCuenta");

	});

	$("#notificaciones").on("click", function() {

		$("#opciones").load("/notificaciones");

	});



});

