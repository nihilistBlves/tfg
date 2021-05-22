function validarLimite(txtArea) {

	var longitud = txtArea.value.length;
	var contador = txtArea.nextSibling.nextSibling;

	if (longitud === 0) {


		var txtCont = document.createTextNode("0/150");

	} else {

		var txtCont = document.createTextNode(longitud + "/150");

	}


	/*console.log(txtArea);
	console.log("contador:"+contador);
	console.log(txtCont);*/
	if (longitud <= 150) {

		contador.removeChild(contador.childNodes[0]);
		contador.appendChild(txtCont);

	}

}

