

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
            
            var mensajeBueno=document.getElementsByClassName("passSuccess");
            var mensajeMalo=document.getElementsByClassName("passWrong");
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
              
                document.getElementById("progreso").style.width="100%";     
                document.getElementById("progreso").style.backgroundColor="rgb(92,184,92)"; 
                mensajeBueno[0].innerHTML="Segura";
                // document.getElementById("progreso").className = "progress-bar bg-success w-100"; 
                

            }else if(mayus.test(pssw) && num.test(pssw) && low.test(pssw) && len.test(pssw) &&pssw.length>9){

                seguridad=1;
                comprobar(obj,false);
              
                document.getElementById("progreso").style.width="50%";     
                document.getElementById("progreso").style.backgroundColor="rgb(240, 173, 78)";
                mensajeMalo[0].innerHTML="Poco segura"; 
             
            }else{

                seguridad=0;
                comprobar(obj,false);
              
                document.getElementById("progreso").style.width="25%";     
                document.getElementById("progreso").style.backgroundColor="rgb(217,83,79)"; 
                mensajeMalo[0].innerHTML="No segura";
                
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
            


            

          
            var mayus = /^(?=.*[A-Z])/;
            var spe = /^(?=.*[!@#$%&*])/;
            var num = /^(?=.*[0-9])/;
            var low = /^(?=.*[a-z])/;
            var len = /^(?=.{8,})/; 

            var seguridad;
            
            var mensajeBueno=document.getElementsByClassName("passSuccess");
            var mensajeMalo=document.getElementsByClassName("passWrong");
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
              
                document.getElementById("progresoRepass").style.width="100%";     
                document.getElementById("progresoRepass").style.backgroundColor="rgb(92,184,92)"; 
                mensajeBueno[1].innerHTML="Segura";

                if(obj.value==pass.value){

                    comprobar(obj,true);
                    return true;
                }else{
    
                    comprobar(obj,false);
                    return false;
                }
                

            }else if(mayus.test(pssw) && num.test(pssw) && low.test(pssw) && len.test(pssw) &&pssw.length>9){

                seguridad=1;
                comprobar(obj,false);
              
                document.getElementById("progresoRepass").style.width="50%";     
                document.getElementById("progresoRepass").style.backgroundColor="rgb(240, 173, 78)";
                mensajeMalo[1].innerHTML="Poco segura"; 
             
            }else{

                seguridad=0;
                comprobar(obj,false);
              
                document.getElementById("progresoRepass").style.width="25%";     
                document.getElementById("progresoRepass").style.backgroundColor="rgb(217,83,79)"; 
                mensajeMalo[1].innerHTML="No segura";
                
            }
            
            switch(seguridad){
                case 0: console.log("Contraseña POCO segura");obj.style.outlineColor="red";return false;break;
                case 1: console.log("Contraseña ALGO segura");obj.style.outlineColor="yellow"; return true;break;
                case 2: console.log("Contraseña MUY segura");obj.style.outlineColor="green";return true;break;
                default: console.log("ERROR"); break;
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
            //Comprobar datos
            /*console.log("Nombre="+validarNombre(f.nombre));
            console.log("Apellido="+ validarApellido(f.apellido));
            console.log("Fecha="+validarFecha(f.edad));
            console.log("Contraseña="+validarPssw(f.pass));
            console.log("Repetir="+validarRepetir(f.repass,f.pass));
            console.log("Correo="+validarEmail(f.email));
*/

            if(validarNombre(f.loginName)&& validarFecha(f.edad) &&  validarRepetir(f.passConfirm,f.password) && validarEmail(f.email)){
               f.aceptar.className="btn-hover btn-red";
               console.log("ok");
               f.aceptar.onclick=function(){
                    document.getElementsByName("formulario")[0].submit();
               }
                

            }else if(validarNombre(f.loginName)==false){

                f.loginName.focus();

            }/*else if(validarApellido(f.apellido)==false){

                f.apellido.focus();

            }*/else if(validarFecha(f.edad)==false || (typeof f.edad)==="undefined"){

                f.edad.focus();

            }else if(validarRepetir(f.passConfirm,f.password)==false){
                f.passConfirm.focus();
            }else if(validarEmail(f.email)==false){

                f.email.focus();
            }
       

        }