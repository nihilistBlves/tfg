

$(document).ready(function() {

	$("#perfil").on("click", function() {

		$("#opciones").load("/opcionesPerfil/editarPerfil");


	});

	$("#pass").on("click", function() {

		$("#opciones").load("/opcionesPerfil/editarPass");

	});

	$("#correo").on("click", function() {

		$("#opciones").load("/opcionesPerfil/editarCorreo");

	});
	$("#seg").on("click", function() {

		$("#opciones").load("/opcionesPerfil/seguidoresSeguidos");

	});

	$("#waves").on("click", function() {

		$("#opciones").load("/opcionesPerfil/tasaWaves");

	});

	$("#favs").on("click", function() {

		$("#opciones").load("/opcionesPerfil/publicacionesFavoritas");

	});

	$("#cuenta").on("click", function() {

		$("#opciones").load("/opcionesPerfil/editarCuenta");

	});

	$("#notificaciones").on("click", function() {

		$("#opciones").load("/opcionesPerfil/notificaciones");

	});



});

