function validarPassword(nombre, password){
	var expresionRegularNombre = /^[a-zA-ZÑñÁáÉéÍíÓóÚúÜü\s]+$/;
	var expresionRegularPassword = /^[a-zA-Z0-9]+$/;
	var nombreValidar = document.getElementById("userName");
	var passwordValidar = document.getElementById("userPass");
	var campoErrorNombre = document.getElementById("errorNombre");
	var textoError = document.createElement("a");
	var mensajeError = document.getElementById("mensajeError");
	
	if(mensajeError != null){
		mensajeError.parentNode.removeChild(mensajeError);
	}
	if(nombre === "" || password === "" || repetirPassword === ""){
		textoError.innerHTML = "Hay campos vacios.";
		textoError.setAttribute("id", "mensajeError")
		campoErrorNombre.appendChild(textoError);
		return false;
	}
	if(!expresionRegularNombre.exec(nombreValidar.value)){
		textoError.innerHTML = "El campo nombre solo admite letras y espacios.";
		textoError.setAttribute("id", "mensajeError")
		campoErrorNombre.appendChild(textoError);
		return false;
	}
	if(!expresionRegularPassword.exec(passwordValidar.value)){
		textoError.innerHTML = "El campo contraseña solo admite numeros y letras.";
		textoError.setAttribute("id", "mensajeError");
		campoErrorNombre.appendChild(textoError);
		return false;
	}
}
function validarLogin(){
	var nombre = document.getElementById("userName").value;
    var password = document.getElementById("userPass").value;
    return validarPassword(nombre, password);
}