package Servicio;

package servicio_json;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.QueryParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Response;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.google.gson.*;

@Path("ws")
public class Servicio {
    static DataSource pool = null;
    static {
        try {
            Context ctx = new InitialContext();
            pool = (DataSource) ctx.lookup("java:comp/env/jdbc/datasource_Servicio");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static Gson j = new GsonBuilder().registerTypeAdapter(byte[].class, new AdaptadorGsonBase64())
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();

    // Primero va este bloque
    @POST
    @Path("Captura_articulos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response Captura_articulos(String json) throws Exception {

        ParametroCapturaArticulo CrearArt = (ParametroCapturaArticulo) j.fromJson(json, ParametroCapturaArticulo.class);
        Articulo articulo = CrearArt.articulo;

        // Se realiza la conexión
        Connection conexion = pool.getConnection();

        // Se realizan las validaciones de inputs como campos obligatorios
        if (articulo.Nombre.isEmpty()) {
            return Response.status(400).entity(j.toJson(new Error("El nombre del artículo es obligatorio."))).build();
        }

        if (articulo.Descripcion.isEmpty()) {
            return Response.status(400).entity(j.toJson(new Error("La descripción del artículo es obligatoria.")))
                    .build();
        }

        if (articulo.Precio <= 0.0 || articulo.Precio == null) {
            return Response.status(400)
                    .entity(j.toJson(new Error("El Precio del artículo es obligatorio o tiene que ser mayor a 0.")))
                    .build();
        }

        if (articulo.Cantidad <= 0) {
            return Response.status(400).entity(j.toJson(new Error("La cantidad del articulo debe ser mayor a 0")))
                    .build();
        }

        if (articulo.Fotografia == null) {
            return Response.status(400).entity(j.toJson(new Error("Ingrese una imagen del artículo."))).build();
        }

        // Se comienzan a realizar las operaciones
        try {
            conexion.setAutoCommit(false);

            // Se realiza el INSERT de los datos a la tabla "articulos"
            PreparedStatement insertarArticulo = conexion.prepareStatement(
                    "INSERT INTO articulos(ID,Nombre,Descripcion,Precio, Cantidad, Fotografia) VALUES (0,?,?,?,?,?)");

            try {
                insertarArticulo.setString(1, Nombre);
                insertarArticulo.setString(2, Descripcion);
                insertarArticulo.setInt(3, Precio);
                insertarArticulo.setFloat(4, Cantidad);
                insertarArticulo.setBytes(5, Fotografia);
                insertarArticulo.executeUpdate();
            }

            finally {
                // Se cierra la transacción
                insertarArticulo.close();
            }
            // Transacción realizada
            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
        return Response.ok().build();
    }

    // Operaciones para comprar el articulo
    // Primero se realiza la consulta del articulo ingresando la busqueda del Nombre
    // o Descripción
    @POST
    @Path("compra_articulos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response compra_articulos(String json) throws Exception {

        ParametroConsultaArticulo consultaArticulo = (ParametroConsultaArticulo) j.fromJson(json,
                ParametroConsultaArticulo.class);
        String busqueda1 = consultaArticulo.Nombre;
        String busqueda2 = consultaArticulo.Descripcion;

        Connection conexion = pool.getConnection();

        try {
            PreparedStatement inst1 = conexion.prepareStatement(
                    // Se realiza la consulta ya sea por nombre o Descripcion
                    "SELECT ID, Nombre, Descripcion, Precio, Fotografia FROM articulos WHERE Nombre LIKE '%?%' OR Descripcion LIKE '%?%'");
            try {
                inst1.setString(1, Nombre);
                inst1.setString(2, Descripcion);

                ResultSet operacion = inst1.executeQuery();
                try {
                    ArrayList<Articulo> listaArticulo = new ArrayList<Articulo>();

                    while (operacion.next()) {
                        Articulo operacion = new Articulo();
                        operacion.ID = operacion.getInt(1);
                        operacion.Nombre = operacion.getString(2);
                        operacion.Descripcion = operacion.getString(3);
                        operacion.Precio = operacion.getFloat(4);
                        operacion.Fotografia = operacion.getBytes(5);
                        listaArticulo.add(r);

                    }
                    return Response.ok().entity(j.toJson(listaArticulo.toArray())).build();
                } finally {
                    operacion.close();
                }
            } finally {
                inst1.close();
            }
        } catch (Exception e) {
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.close();
        }
    }

    // Al comprar un articulo se verifica la cantidad del mismo dentro de la tabla
    // articulos
    @POST
    @Path("Ver_articulo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ver_articulo(String json) throws Exception {
        ParemetroConsultaCantidad cantidadArticulo = (ParemetroConsultaCantidad) j.fromJson(json,
                ParemetroConsultaCantidad.class);
        String IDarticulo = cantidadArticulo.ID;

        Connection conexion = pool.getConnection();

        try {
            PreparedStatement inst = conexion.prepareStatement(
                    "SELECT Cantidad FROM articulos WHERE ID=?");
            try {
                inst.setInt(1, ID);

                ResultSet peracion = inst.executeQuery();
                try {
                    ArrayList<Articulo> listaCantidad = new ArrayList<Articulo>();

                    if (operacion.next()) {
                        Integer cantidad = operacion.getInt(1);
                        return Response.status(400).entity(j.toJson(cantidad)).build();

                    }
                    return Response.ok().entity(j.toJson(listaCantidad.toArray())).build();
                } finally {
                    operacion.close();
                }
            } finally {
                inst.close();
            }
        } catch (Exception e) {
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.close();
        }
    }

    // Aqui se realiza el Update de la cantidad del articulo de la tabla articulos y
    // el INSERT en la tabla carrito_compra
    @POST
    @Path("Ver_articulo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ver_articulo(String json) throws Exception {
        ParametroComprarArticulo comprarArticulo = (ParametroComprarArticulo) j.fromJson(json,
                ParametroComprarArticulo.class);
        int IDarticulo = comprarArticulo.ID;
        int cantidad = comprarArticulo.Cantidad;

        Connection conexion = pool.getConnection();
        conexion.setAutoCommit(false);

        try {
            // Primero se realiza el update
            PreparedStatement inst = conexion.prepareStatement("UPDATE articulos SET Cantidad=? WHERE ID=?");
            try {
                inst.setInt(1, Cantidad);
                inst.setInt(2, ID);

                inst.executeUpdate();
            } finally {
                inst.close();
            }

            // Aqui se agrega la segunda parte INSERT en la tabla carrito_compra
            PreparedStatement inst2 = conexion
                    .prepareStatement("INSERT INTO carrito_compra (ID,Cantidad)VALUES(?,?)");
            try {
                inst2.setInt(2, ID);
                inst2.setInt(1, Cantidad);

                inst2.executeUpdate();
            } finally {
                inst2.close();
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
        return Response.ok().build();
    }

    // Carrito
    @POST
    @Path("Articulos_carrito")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Articulos_carrito(String json) throws Exception {
        ParametroConsultaArticulo carritoArticulo = (ParametroConsultaArticulo) j.fromJson(json,
                ParametroConsultaArticulo.class);
        int IDarticulo = carritoArticulo.ID;
        String Nombre = carritoArticulo.Nombre;
        String Descripcion = carritoArticulo.Descripcion;
        float Precio = carritoArticulo.Precio;
        byte[] Fotografia = carritoArticulo.Fotografia;
        Connection conexion = pool.getConnection();
        ArrayList<Carrito> listaCarrito = new ArrayList<>();
        boolean dataAvailable = false;

        try {
            PreparedStatement inst1 = conexion.prepareStatement(
                    "SELECT b.IDCarrito, b.Cantidad, a.ID, a.Nombre, a.Descripcion, a.Precio, a.Existencia, a.Imagen from articulos a inner join carrito_compra b on b.ID = a.ID left outer join Fotografia b on a.ID = b.ID");
            try {
                ResultSet operacion = inst1.executeQuery();
                try {

                    while (operacion.next()) {
                        dataAvailable = true;
                        CarritoCompra a = new CarritoCompra();
                        a.IDCarrito = operacion.getInt(1);
                        a.Cantidad = operacion.getInt(2);
                        a.ID = operacion.getInt(3);
                        a.Nombre = operacion.getString(4);
                        a.Descripcion = operacion.getString(5);
                        a.Precio = operacion.getFloat(6);
                        a.Existencia = operacion.getInt(7);
                        a.Imagen = operacion.getBytes(8);
                        carrito_compra.add(a);

                    }
                    if (dataAvailable) {
                        return Response.ok().entity(j.toJson(carrito_compra)).build();
                    } else {
                        return Response.status(400).entity(j.toJson(new Error("El articulo no esta disponible")))
                                .build();
                    }

                } finally {
                    operacion.close();
                }
            } finally {
                inst1.close();
            }
        } catch (Exception e) {
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.close();
        }
    }

    // Borrar un solo articulo del carrito (Se realiza dentro de una transacción)

    @POST
    @Path("Articulos_carrito")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Articulos_carrito(String json) throws Exception {
        ParametroBorrarArticulo eliminarArticulo = (ParametroBorrarArticulo) j.fromJson(json,
                ParametroBorrarArticulo.class);
        String ID = eliminarArticulo.ID;
        String Cantidad = eliminarArticulo.Cantidad;

        Connection conexion = pool.getConnection();
        conexion.setAutoCommit(false);
        try {
            PreparedStatement inst1 = conexion.prepareStatement(
                    "UPDATE articulos SET Cantidad = Cantidad+(SELECT Cantidad FROM carrito_compra WHERE ID=?) WHERE ID=?");
            try {
                inst1.setInt(1, cantidad);
                inst1.setInt(2, ID);

                inst1.executeQuery();

            } finally {
                inst1.close();
            }

            PreparedStatement inst2 = conexion.prepareStatement("DELETE FROM carrito_compra WHERE ID =?");
            try {
                inst2.setInt(1, ID);
                inst2.setInt(2, Cantidad);

                inst2.executeUpdate();
            } finally {
                inst2.close();
            }

            conexion.commit();
        } catch (Exception e) {
            conexion.rollback();
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.setAutoCommit(true);
            conexion.close();
        }
        return Response.ok().build();
    }

    @POST
    @Path("articulos")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultaArticulos(String json) throws Exception {
        ParamConsultaArticulos p = (ParamConsultaArticulos) j.fromJson(json, ParamConsultaArticulos.class);
        int id_articulo = p.id_articulo;
        String nombre_articulo = p.nombre_articulo;
        String desc_articulo = p.desc_articulo;
        float precio = p.precio;
        byte[] foto = p.foto;
        Connection conexion = pool.getConnection();
        ArrayList<Articulo> artiulos = new ArrayList<Articulo>();
        boolean dataAvailable = false;

        try {
            PreparedStatement stmt_1 = conexion.prepareStatement(
                    "select id_articulo, nombre_articulo, desc_articulo, precio, cantidad, foto from articulos");

            try {
                ResultSet rs = stmt_1.executeQuery();
                try {

                    while (rs.next()) {
                        dataAvailable = true;
                        Articulo a = new Articulo();
                        a.id_articulo = rs.getInt(1);
                        a.nombre_articulo = rs.getString(2);
                        a.desc_articulo = rs.getString(3);
                        a.precio = rs.getFloat(4);
                        a.cantidad = rs.getInt(5);
                        a.foto = rs.getBytes(6);
                        articulos.add(a);

                    }
                    if (dataAvailable) {
                        return Response.ok().entity(j.toJson(carrito_compra)).build();
                    } else {
                        return Response.status(400).entity(j.toJson(new Error("No articulos disponibles"))).build();
                    }

                } finally {
                    rs.close();
                }
            } finally {
                stmt_1.close();
            }
        } catch (Exception e) {
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.close();
        }
    }

    // Aqui se muestra en el ventana la descripción de articulo elegido
    @POST
    @Path("Ver_articulo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response Ver_articulo(String json) throws Exception {
        ParametroConsultaDescripcion articuloDescripcion = (ParametroConsultaDescripcion) j.fromJson(json,
                ParametroConsultaDescripcion.class);
        String Descripcion = articuloDescripcion.Descripcion;
        Connection conexion = pool.getConnection();
        ArrayList<Articulo> listaDescripcion = new ArrayList<Articulo>();
        boolean dataAvailable = false;

        try {
            PreparedStatement inst1 = conexion.prepareStatement(
                    "SELECT ID, Descripcion from articulos");

            try {
                ResultSet operacion = inst1.executeQuery();
                try {

                    while (operacion.next()) {
                        dataAvailable = true;
                        Articulo a = new Articulo();
                        a.id_articulo = operacion.getInt(1);
                        a.desc_articulo = operacion.getString(2);

                        articulos.add(a);

                    }
                    if (dataAvailable) {
                        return Response.ok().entity(j.toJson(carrito_compra)).build();
                    } else {
                        return Response.status(400).entity(j.toJson(new Error("Articulo no disponible"))).build();
                    }

                } finally {
                    rs.close();
                }
            } finally {
                stmt_1.close();
            }
        } catch (Exception e) {
            return Response.status(400).entity(j.toJson(new Error(e.getMessage()))).build();
        } finally {
            conexion.close();
        }
    }

    // Clase Articulo

    static class Articulo {
        int ID;
        String Nombre;
        String Descripcion;
        double Precio;
        int Cantidad;
        byte[] Fotografia;
    }

    // Objeto de Articulo
    static class ParametroCapturaArticulo {
        Articulo articulo;
    }

    // Consulta de existencia de un Articulo
    static class ParametroConsultaArticulo {
        String Nombre;
        String Descripcion;
    }

    static class ParametroComprarArticulo {
        int ID;
        int Cantidad;
    }

    static class ParemetroConsultaCantidad {
        int ID;
    }

    static class CarritoCompra {
        int IDCarrito;
        int Cantidad;
        int ID;
        String Nombre;
        String Descripcion;
        double Precio;
        int Existencia;
        byte[] Imagen;
    }

    static class ParametroBorrarArticulo {
        int IDCarrito;
        int Cantidad;
    }

    static class ParametroConsultaDescripcion {
        String Descripcion;
    }
}