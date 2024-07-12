function confirmarEliminacion(){
    var respuesta = confirm("¿Estas seguro que quieres cancelar el pedido?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}

function confirmarEliminacionCliente(){
    var respuesta = confirm("¿Estas seguro que quieres eliminar el cliente?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}

function confirmarEliminacionProducto(){
    var respuesta = confirm("¿Estas seguro que quieres eliminar el producto?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}

function confirmarEliminacionEmpleado(){
    var respuesta = confirm("¿Estas seguro que quieres eliminar el empleado?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}

function confirmarEliminacionAdministrador(){
    var respuesta = confirm("¿Estas seguro que quieres eliminar el administrador?");
    if(respuesta == true){
        return true;
    }else{
        return false;
    }
}