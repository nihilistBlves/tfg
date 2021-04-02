  $(document).ready(function () {

            $("#aparecer").hide();

            $("#boton").click(function () {

                $("#aparecer").show();

                $("#opcion").click(function () {

                    $("#aparecer").hide();

                });

            });
        });