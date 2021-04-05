function validarLimite(txtArea){

	var longitud= txtArea.value.length;
	var  contador= txtArea.nextSibling.nextSibling;

	if(longitud===0){

		
		var txtCont=document.createTextNode("0/300");
		
	}else{

		var txtCont=document.createTextNode(longitud+"/300");

	}
	
	
	/*console.log(txtArea);
	console.log("contador:"+contador);
	console.log(txtCont);*/
	if(longitud<=300){

		contador.removeChild(contador.childNodes[0]);
		contador.appendChild(txtCont);

	}

}