//Aqui se obtendran los datos de los inputs para convertirlos a objeto JSON

//Alta de nuevo producto.
function capturaArticulo(){
    var URL = "/Servicio/rest/ws";
    var cliente = new WSClient(URL);
    // var fotografia = null;

    //Aqui ingresar todos los campos para el alta de un producto.
    var producto={
        Nombre: document.getElementById("Captura_nombre").value,
        Descripcion: document.getElementById("Captura_descripcion").value,
        Precio: document.getElementById("Captura_precio").value,
        Cantidad: document.getElementById("Captura_cantidad").value,
        Fotografia: Fotografia
    };

    cliente.postJson("Captura_usuario",
    {
        producto: producto
    },
    function(code,result)
    {
        if(code==200)
            alert("Producto agregado correctamente :)");
        else
            alert(JSON.stringify(result));
    });
}

//funcion para cargar imagen del producto.
//LEER FOTO
function readSingleFile(files,imagen)
{
	var file = files[0];
	if (!file) return;
	var reader = new FileReader();
	reader.onload = function(e)
	{
	imagen.src = reader.result;
	// reader.result incluye al principio: "data:image/jpeg;base64,"
	Fotografia = reader.result.split(',')[1];
	};
	reader.readAsDataURL(file);
}

//Aqui se realiza la consulta del producto primero
function comprarProducto(){
    var URL = "/Servicio/rest/ws";
    var cliente = new WSClient(URL);
    // var fotografia = null;

    cliente.postJson("buscar_producto",
    {
        Nombre:document.getElementById("Cap_nombre").value,
        Descripcion:document.getElementById("Captura_descripcion").value
    },

    function(code,result)
    {
        //Primero se comprueba que exista el produeto
        if(code==200){
            document.getElementById("Consulta_nombre").value=result.Nombre;
            document.getElementById("Consulta_precio").value=result.Descripcion;
            // document.getElementById("Producto_precio").value=result.Precio;
            // document.getElementById("Producto_cantidad").value=result.Cantidad;
            Fotografia=result.Fotografia;
            document.getElementById("Consulta_fotografia").src=Fotografia != null ? "data:image/jpeg; base64,"+Fotografia: "/usuario_sin_foto.pbg";

            oculta();
            muestra();
            muestra();
            oculta();
           
        }else{
            alert("El producto buscado no se encuentra disponible"+JSON.stringify(result));
            
        }
    });
}


//funciones para limpieza de resultados
function limpia_alta(){
	get("Captura_nombre").value = "";
    get("Captura_descripcion").value = "";
	get("Captura_precio").value = "";
	get("Captura_cantidad").value = "";
	get("Captura_fotografia").src = "/usuario_sin_foto.png";
	Fotografia = null;
}

function limpia_consulta(){
	get("Consulta_nombre").value = "";
	get("Consulta_precio").value = "";
	get("Consulta_fotografia").src = "/usuario_sin_foto.png";
}

//Funciones para ocultar y mostrar información.

function muestra(id){
	get(id).style.display = "block";
}

function oculta(id){
	get(id).style.display = "none";
}
			
function muestra_pantalla(id){
	oculta("menu");
	muestra(id);
}

function oculta_pantalla(id){
	oculta(id);
	muestra("menu");
}
