function validarLimite(txtArea) {

	var longitud = txtArea.value.length;
	var contador = txtArea.nextSibling.nextSibling;

	if (longitud === 0) {


		var txtCont = document.createTextNode("0/150");

	} else {

		var txtCont = document.createTextNode(longitud + "/150");

	}

	if (longitud <= 150) {

		contador.removeChild(contador.childNodes[0]);
		contador.appendChild(txtCont);

	}

}

//Cambio de email
function opcionesCorreo(f) {

	//var exp = /^(([^<>()[\]\.,;:\s@\"]+(\.[^<>()[\]\.,;:\s@\"]+)*)|(\".+\"))@(([^<>()[\]\.,;:\s@\"]+\.)+[^<>()[\]\.,;:\s@\"]{2,})$/i;
	var exp=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;


	if (exp.test(f.value)) {
		console.log("OK")
		return true;

	} else {
		return false;
	}
}   

//Cambio de contraseña

function opcionesPssw(obj){
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

        function opcionesRepetir(obj,pass){
          
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
        
        function comprobarPass(act,pass,repass){
	
		if(act.value != pass.value && opcionesPssw(pass)){
			console.log("PRimer");
			if(opcionesRepetir(repass,pass)){
				console.log("OK");
				return true;
			}else
			console.log("NO");
				return false;
				
			}else{
				console.log("NO");
			return false;
		}
			
			
		}
	
        
        

