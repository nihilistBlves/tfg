

        $(document).ready(function () {

            $("#perfil").on("click", function () {

                $("#opciones").load("/opcionesPerfil/editarPerfil");

                
            });

            $("#gestionPass").on("click", function () {

                $("#opciones").load("/opcionesPerfil/gestionarCorreoPass");

            });

            $("#seg").on("click", function () {

                $("#opciones").load("/opcionesPerfil/seguidoresSeguidos");

            });

            $("#waves").on("click", function () {

                $("#opciones").load("/opcionesPerfil/tasaWaves");

            });

            $("#favs").on("click", function () {

                $("#opciones").load("/opcionesPerfil/publicacionesFavoritas");

            });

            $("#tipoCuenta").on("click", function () {

                $("#opciones").load("/opcionesPerfil/seleccionTipoCuenta");

            });

            $("#not").on("click", function () {

                $("#opciones").load("/opcionesPerfil/notificaciones");

            });

            $("#desactivar").on("click", function () {

                $("#opciones").load("/opcionesPerfil/desactivarCuenta");

            });

            $("#eliminar").on("click", function () {

                $("#opciones").load("eliminarCuenta");

            });


        });

