function confirmarEnviarPedido(){
    var respuesta = confirm("¿Estas seguro que quieres enviar el pedido?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}

function confirmarCancelarPedido(){
    var respuesta = confirm("¿Estas seguro que quieres cancelar el pedido?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}