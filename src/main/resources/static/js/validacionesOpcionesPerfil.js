function validarLimite(txtArea){

	var longitud= txtArea.value.length;
	//var texto= txtArea.value;
	var  contador= txtArea.nextSibling.nextSibling;

	if(longitud===0){

		var txtCont=document.createTextNode("0/300");
		
	}else{

		var txtCont=document.createTextNode(longitud+"/300");

	}
	
	
	console.log(txtArea);
	console.log("contador:"+contador);
	console.log(txtCont);
	if(longitud<=300){

		contador.removeChild(contador.childNodes[0]);
		
		txtArea.className="form-control is-valid";
		txtArea.style.color="none";
		contador.appendChild(txtCont);

	}else{

		txtArea.className="form-control is-invalid";

	}

}