

        function comprobar(obj,valido){

            if(valido){
                obj.className="form-control is-valid";
                obj.style.border="none";
            }else{
                obj.className="form-control is-invalid";
            }

        }

        function validarNombre(obj){

            var exp=/^(?![\s])([A-Z]|[a-z]|[áéíóúçñÁÉÍÓÚÇÑ]|[\s]){5,25}$/;

            if(exp.test(obj.value)){

                comprobar(obj,true);
                return true;
            }else{

                comprobar(obj,false);
                return false;

            }

        }

        function validarApellido(obj){

            var exp=/^(?![\s])([A-Z]|[a-z]|[áéíóúçñÁÉÍÓÚÇÑ]|[\s]){3,35}$/;

            if(exp.test(obj.value)){

                comprobar(obj,true);
                return true;
            }else{

                comprobar(obj,false);
                return false;

            }

        }

        function validarFecha(obj){

        var fecha=obj.value;
        var trozos=fecha.split("-");
        var fechaActual=new Date();
        var añoActual=fechaActual.getFullYear();
        var años=añoActual-trozos[0];
        
        console.log(años);

        if(fecha!=""){
            if(años<14){

                comprobar(obj,false);
                return false;
            



            }else{

                comprobar(obj,true);
                return true;
            }
        }else{
            comprobar(obj,false);

        }

        }

        function validarPssw(obj){

            //var exp=/^([A-Z]|[a-z]|[\!\"\#\$\%\&\'\(\)\*\+\,-\.\/\:\;\<\=\>\?\@\[\\\\]\^\_\`\{\|\}\~\]|[\d]|[áéíóúÁÉÍÓÚçñÑÇ]){8,20}$/;

            var mayus = /^(?=.*[A-Z])/;
            var spe = /^(?=.*[!@#$%&*])/;
            var num = /^(?=.*[0-9])/;
            var low = /^(?=.*[a-z])/;
            var len = /^(?=.{8,})/; 

            var seguridad;

            /*
            Seguridad:
                0:poco segura
                1:algo segura
                2:muy segura
            
            */
            
            var pssw=obj.value;

           // console.log(pssw);

            if(mayus.test(pssw) && spe.test(pssw) && num.test(pssw) && low.test(pssw) && len.test(pssw)){

                seguridad=2;
                comprobar(obj,true);

            }else if(mayus.test(pssw) && num.test(pssw) && low.test(pssw) && len.test(pssw) &&pssw.length>9){

                seguridad=1;
                comprobar(obj,false);
            }else{

                seguridad=0;
                comprobar(obj,false);
            }
            
            switch(seguridad){
                case 0: console.log("Contraseña POCO segura");obj.style.outlineColor="red";return false;break;
                case 1: console.log("Contraseña ALGO segura");obj.style.outlineColor="yellow"; return true;break;
                case 2: console.log("Contraseña MUY segura");obj.style.outlineColor="green";return true;break;
                default: console.log("ERROR"); break;
            }


        }

        function ver(obj){

            var valor=obj.value;
            obj.type="text";
            obj.value=valor;
            
        }

        function ocultar(obj){

            var valor=obj.value;
            obj.type="password";
            obj.value=valor;

        }

        function validarRepetir(obj,pass){

            if(obj.value==pass.value){

                comprobar(obj,true);
                return true;
            }else{

                comprobar(obj,false);
                return false;
            }


        }

        function validarEmail(obj){

            var exp=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;

            if(exp.test(obj.value)){

                comprobar(obj,true);
                return true;

            }else{

                comprobar(obj,false);
                return false;

            }
        }   

        function comprobarTodo(f){

            console.log(validarNombre(f.nombre));

            //if(validarNombre(f.nombre)){

                 if(validarNombre()&& validarApellido() && validarFecha() && validarPssw() && validarRepetir() && validarEmail()==true){

                //f.aceptar.disabled="true";
                f.aceptar.className="btn btn-success";

                var boton=document.formulario.aceptar;

                boton.onclick=function(){
                    document.getElementsByName("formulario")[0].submit();
                }
               

            }else{
                
                    f.aceptar.disabled="true";
                    f.aceptar.className="btn btn-danger";
            }
       

        }