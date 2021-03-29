

        $(document).ready(function () {

            $("#perfil").on("click", function () {

                $("#opciones").load("./opciones/editarPerfil.html");

            });

            $("#gestionPass").on("click", function () {

                $("#opciones").load("./opciones/gestionarCorreoPass.html");

            });

            $("#seg").on("click", function () {

                $("#opciones").load("./opciones/seguidoresSeguidos.html");

            });

            $("#waves").on("click", function () {

                $("#opciones").load("./opciones/tasaWaves.html");

            });

            $("#favs").on("click", function () {

                $("#opciones").load("./opciones/publicacionesFavoritas.html");

            });

            $("#tipoCuenta").on("click", function () {

                $("#opciones").load("./opciones/seleccionTipoCuenta.html");

            });

            $("#not").on("click", function () {

                $("#opciones").load("./opciones/notificaciones.html");

            });

            $("#desactivar").on("click", function () {

                $("#opciones").load("./opciones/desactivarCuenta.html");

            });

            $("#eliminar").on("click", function () {

                $("#opciones").load("./opciones/eliminarCuenta.html");

            });


        });

