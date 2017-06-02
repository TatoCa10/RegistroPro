package edu.co.sergio.mundo.dao;

import edu.co.sergio.mundo.vo.Item;
import edu.co.sergio.mundo.vo.Lote;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import edu.co.sergio.mundo.vo.User;
//import java.awt.Font;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.swing.text.Document;
import com.itextpdf.text.BaseColor;
import java.util.logging.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ServiciosDAO {

    int Id_Global;
    Fecha date = new Fecha();
    String desc;

    public boolean LogIn(Connection connection, int user_id, String pass) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        String query = "select user_id,pass from Users where user_id=" + user_id + "";
        //String query2 = "select pass from Users where pass='"+pass+"'"; 

        PreparedStatement preparedStmt = null;

        int id;
        String password = "";

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            //ResultSet rs2 = st.executeQuery(query2);

            while (rs.next()) {
                id = rs.getInt(1);
                password = rs.getString(2);

                if (id == user_id && password.equals(pass)) {
                    System.out.println("Logeo promente promente");
                    Id_Global = user_id;
                    desc = "Inicio Sesion";
                    RegistroAct(connection, desc);
                    return true;
                }

            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to make insertion!");
            e.printStackTrace();
        }
        System.out.println("Error en el Logeo");
        return false;

    }

    public boolean LogOut(Connection connection) {
        String fecha = "";
        fecha = date.getDate().toString();
        int reg = 1;
        desc = "Cerro Sesion";
        RegistroAct(connection, desc);
        Id_Global = -1;
        return true;
    }

    public boolean insertarUser(int userId, String pass, String nombre, String apellido, String correo, String telefono) {
        Boolean b;
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = " insert into Users (user_id, pass, Nombre, Apellido, Correo, Telefono)"
                + " values (?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, userId);
            preparedStmt.setString(2, pass);
            preparedStmt.setString(3, nombre);
            preparedStmt.setString(4, apellido);
            preparedStmt.setString(5, correo);
            preparedStmt.setString(6, telefono);

            preparedStmt.executeUpdate();

            System.out.println("You made it, the insertion is ok!");
            desc = "Creo un nuevo usuario";
            RegistroAct(connection, desc);
            b = true;
            return b;

        } catch (SQLException e) {
            System.out.println("Failed to make insertion!");
            b = false;
            e.printStackTrace();
        }
        return b;
    }

    public boolean actUser(User user) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // the mysql select statement
        String query = "SELECT user_id from Users where user_id=" + user.getId_User() + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        int id;
        boolean check = true;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                id = rs.getInt(1);

                if (id == user.getId_User()) {
                    System.out.println("Entro promente");
                    check = false;
                    query = "UPDATE Users SET user_id=" + user.getId_User() + ", pass='" + user.getPass() + "', Nombre='" + user.getNombre() + "', Apellido='" + user.getApellido() + "', Correo='" + user.getCorreo() + "', Telefono='" + user.getTelefono() + "' where user_id=" + user.getId_User() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    desc = "Actualizo un Usuario";
                    RegistroAct(connection, desc);
                    b = true;
                }
            }
            if (check) {
                System.out.println("No se encontro ID del Usuario");
                b = false;
            }
            desc = "Actualizo Usuario";
            RegistroAct(connection, desc);
            return b;

        } catch (SQLException e) {
            System.out.println("Error Detectado");
            b = false;
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, e);

        }
        return b;
    }

    public boolean borrarUser(User user) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // the mysql select statement
        String query = "SELECT user_id from Users where user_id=" + user.getId_User() + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        boolean check = true;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);

                if (id[0] != null) {
                    System.out.println("Entro promente");
                    check = false;
                    query = "Delete From Users where user_id=" + user.getId_User() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    desc = "Elimino un Usuario";
                    RegistroAct(connection, desc);
                    b = true;
                }
            }
            if (check) {
                System.out.println("No se encontro ID del Usuario");
                b = false;
            }

            desc = "Elimino Usuario";
            RegistroAct(connection, desc);
            return b;
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    ///////////////////////////////////////////////////////     LOTES     \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public boolean inertarLote(int id, String nombre) {
        //Insertion 
        // create a sql date object so we can use it in our INSERT statement
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        // the mysql insert statement
        String query = " insert into Lote (IdLote, Nombre)"
                + " values (?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.setString(2, nombre);

            // execute the preparedstatement
            preparedStmt.executeUpdate();

            System.out.println("You made it, the insertion is ok!");
            b = true;
            desc = "Creo un nuevo Lote";
            RegistroAct(connection, desc);
            return b;

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to make insertion!");
            b = false;
            e.printStackTrace();
        }
        return b;
    }

    public boolean actLote(Lote lote) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql select statement
        String query = "SELECT IDLote from Lote where IDLote=" + lote.getIDLote() + "";

        // create the mysql update preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        boolean check = true;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);

                if (id[0] != null) {
                    System.out.println("Entro promente");
                    check = false;
                    query = "UPDATE Lote SET IDLote=" + lote.getIDLote() + ", Nombre='" + lote.getNombreLote() + "' where IDLote=" + lote.getIDLote() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    b = true;
                    //Revisar si esta vacio
//                    query = "SELECT IDLote from Productos where IDLote=" + lote.getIDLote() + "";
//                    rs = st.executeQuery(query);
//                    while (rs.next()) {
//                        id[0] = rs.getInt("IDLote");
//
//                        if (id[0] != null) {
//                            query = "UPDATE Productos SET IDLote=" + lote.getIDLote() + " where IDLote=" + lote.getIDLote() + "";
//                            preparedStmt = connection.prepareStatement(query);
//                            preparedStmt.executeUpdate();
//                            b = true;
//                        }
//                    }

                }
            }
            if (check) {
                System.out.println("No se encontro ID del Lote");
                b = false;
            }
            desc = "Actualizo Lote";
            RegistroAct(connection, desc);
            return b;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    ///////////////////////////////////////////////////////     ITEMS    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public boolean insertarItem(Item item) {
        //Insertion 
        // create a sql date object so we can use it in our INSERT statement
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql insert statement
        String query = " insert into Productos (IdItem ,IDLote, Cantidad, NombreProd, Proveedor, Precio, Razon)"
                + " values (?, ?, ?, ?, ?, ?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;

        try {

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, item.getIdItem());
            preparedStmt.setInt(2, item.getIDLote());
            preparedStmt.setInt(3, item.getCantidad());
            preparedStmt.setString(4, item.getNombreProd());
            preparedStmt.setString(5, item.getProveedor());
            preparedStmt.setInt(6, item.getPrecio());
            preparedStmt.setInt(7, item.getRazon());

            // execute the preparedstatement
            preparedStmt.executeUpdate();

            System.out.println("You made it, the insertion is ok!");
            desc = "Ingreso un producto a Bodega";
            RegistroAct(connection, desc);
            b = true;
            return b;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to make insertion!");
            b = false;
            e.printStackTrace();
        }
        return b;
    }

    public boolean actItem(Item item) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql select statement
        String query = "SELECT IdItem from Productos where IdItem=" + item.getIdItem() + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        boolean check = true;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);

                if (id[0] != null) {
                    System.out.println("Entro promente");
                    check = false;
                    query = "UPDATE Productos SET IdItem=" + item.getIdItem() + ", IDLote=" + item.getIDLote() + ", Cantidad=" + item.getCantidad() + ", NombreProd='" + item.getNombreProd() + "', Proveedor='" + item.getProveedor() + "', Precio=" + item.getPrecio() + ", Razon=" + item.getRazon() + " where IdItem=" + item.getIdItem() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    b = true;

                }
            }
            if (check) {
                System.out.println("No se encontro ID del Item");
                b = false;
            }
            desc = "Actualizo Item";
            RegistroAct(connection, desc);
            return b;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    public boolean agregarItem(Item item) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql select statement
        String query = "SELECT IdItem, Cantidad from Productos where IdItem=" + item.getIdItem() + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        int cant;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);
                cant = rs.getInt(2);
                if (id[0] != null) {
                    System.out.println("Entro promente");
                    cant = cant + item.getCantidad();
                    query = "UPDATE Productos SET Cantidad=" + cant + " where IdItem=" + item.getIdItem() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    desc = "Ingreso Items A Bodega";
                    RegistroAct(connection, desc);
                    b = true;
                } else {
                    System.out.println("No se encontro ID del Item");
                    b = false;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    public boolean sacarItem(Item item) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql select statement
        String query = "SELECT IdItem, Cantidad from Productos where IdItem=" + item.getIdItem() + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        int cant;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);
                cant = rs.getInt(2);
                if (id[0] != null) {
                    System.out.println("Entro promente");
                    cant = cant - item.getCantidad();
                    query = "UPDATE Productos SET Cantidad=" + cant + " where IdItem=" + item.getIdItem() + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    desc = "Ingreso Items A Bodega";
                    RegistroAct(connection, desc);
                    b = true;
                } else {
                    System.out.println("No se encontro ID del Item");
                    b = false;
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    public boolean vaciarLote(int IdLote) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());
        boolean b = true;

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        // the mysql select statement
        String query = "SELECT IdLote from Lote where IdLote=" + IdLote + "";

        // create the mysql update and insert preparedstatement
        PreparedStatement preparedStmt = null;

        Integer[] id = new Integer[1];
        boolean check = true;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                id[0] = rs.getInt(1);

                if (id[0] != null) {
                    System.out.println("Entro promente");
                    check = false;
                    query = "Delete From Productos where IdItem=" + IdLote + "";
                    preparedStmt = connection.prepareStatement(query);
                    preparedStmt.executeUpdate();
                    b = true;

                }
            }
            if (check) {
                System.out.println("No se encontro ID del Item");
                b = false;
            }
            desc = "Elimino Item";
            RegistroAct(connection, desc);
            return b;

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
            b = false;
        }
        return b;
    }

    ///////////////////////////////////////////////////////     CONSULTAS    \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public void RegistroAct(Connection connection, String desc) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        // the mysql insert statement
        String query = " insert into Registro (user_id ,descrip, fecha)"
                + " values (?, ?, ?)";

        // create the mysql insert preparedstatement
        PreparedStatement preparedStmt = null;

        String fecha = date.getDate().toString();
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, Id_Global);
            preparedStmt.setString(2, desc);
            preparedStmt.setString(3, fecha);

            // execute the preparedstatement
            preparedStmt.executeUpdate();

            System.out.println("You made it, the Register is ok!");

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println("Failed to make the Register!");
            e.printStackTrace();
        }

    }

    public ArrayList ListaGeneral(int x) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "";
        ArrayList Arreglo = new ArrayList();
        int i = 0;
        switch (x) {

            case 4:
                Arreglo.clear();
                query = "select * from Users";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(3));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }
                break;

            case 5:
                Arreglo.clear();
                query = "select * from Lote";
                i = 0;
                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(2));
                    }
                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }
                break;

            case 6:
                Arreglo.clear();
                query = "select * from Productos";
                i = 0;
                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(3));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }
                break;
        }
        return null;

    }

    public ArrayList BusquedaPersonalizada(int x, String Letra, int Num) {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "";
        ArrayList Arreglo = new ArrayList();
        int i = 0;
        switch (x) {
            /////////////////////////////////USERS//////////////////////////////////
            case 19:

                Arreglo.clear();
                query = "select * from Users where Nombre like '" + Letra + "%'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(3));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 20:

                Arreglo.clear();
                query = "select * from Users where Nombre ='" + Letra + "'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(3));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 21:

                Arreglo.clear();
                query = "select * from Users where user_id =" + Num + "";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(3));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;
            //////////////////////////////LOTE//////////////////////////////////////
            case 22:
                Arreglo.clear();
                query = "select * from Lote where Nombre like'" + Letra + "%'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(2));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;
            case 23:
                Arreglo.clear();
                query = "select * from Lote where Nombre ='" + Letra + "'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(2));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 24:
                Arreglo.clear();
                query = "select * from Lote where IdLote =" + Num + "";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getString(2));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            //////////////////////////////////ITEMS/////////////////////////////////
            case 25:
                Arreglo.clear();
                query = "select * from Productos where IdItem =" + Num + "";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(3));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 26:
                Arreglo.clear();
                query = "select * from Productos where NombreProd like '" + Letra + "%'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(3));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 27:
                Arreglo.clear();
                query = "select * from Productos where NombreProd ='" + Letra + "'";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(3));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 28:
                Arreglo.clear();
                query = "select * from Productos where Precio >" + Num + "";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(7));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;

            case 29:
                Arreglo.clear();
                query = "select * from Productos where Precio <" + Num + "";
                i = 0;

                try {

                    Statement st = connection.createStatement();
                    ResultSet rs = st.executeQuery(query);
                    while (rs.next()) {
                        Arreglo.add(rs.getInt(1));
                        Arreglo.add(rs.getInt(7));
                        Arreglo.add(rs.getString(4));
                    }

                    return Arreglo;

                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    System.out.println("Failed to make insertion!");
                    e.printStackTrace();
                }

                break;
        }

        return null;
    }

    public void GenerarPDF(Connection connection, ArrayList id, ArrayList nombre, int opc, String nombreArchivo) {

        ArrayList<Integer> IdsPDF = new ArrayList<Integer>();
        ArrayList<String> NombresPDF = new ArrayList<String>();
        IdsPDF = id;
        NombresPDF = nombre;

        Document document = new Document();

        try {

            PdfWriter.getInstance(document, new FileOutputStream(nombreArchivo + ".pdf"));
            document.open();
            System.out.println("Probo");
            PdfPTable table = new PdfPTable(2);
            if (opc == 4) {
                Font fuenteUsuario = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuario.setColor(BaseColor.WHITE);

                PdfPCell cellUser = new PdfPCell(new Paragraph("Id Usuario", fuenteUsuario));
                cellUser.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUser.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUser.setPaddingBottom(15);
                table.addCell(cellUser);

                Font fuenteUsuarioNombre = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuarioNombre.setColor(BaseColor.WHITE);

                PdfPCell cellUserNombre = new PdfPCell(new Paragraph("Nombre Usuario", fuenteUsuarioNombre));
                cellUserNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUserNombre.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUserNombre.setPaddingBottom(15);
                table.addCell(cellUserNombre);
            }
            if (opc == 5) {
                Font fuenteUsuario = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuario.setColor(BaseColor.WHITE);

                PdfPCell cellUser = new PdfPCell(new Paragraph("Id Lote", fuenteUsuario));
                cellUser.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUser.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUser.setPaddingBottom(15);
                table.addCell(cellUser);

                Font fuenteUsuarioNombre = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuarioNombre.setColor(BaseColor.WHITE);

                PdfPCell cellUserNombre = new PdfPCell(new Paragraph("Nombre Lote", fuenteUsuarioNombre));
                cellUserNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUserNombre.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUserNombre.setPaddingBottom(15);
                table.addCell(cellUserNombre);
            }
            if (opc == 6) {
                Font fuenteUsuario = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuario.setColor(BaseColor.WHITE);

                PdfPCell cellUser = new PdfPCell(new Paragraph("Cantidad", fuenteUsuario));
                cellUser.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUser.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUser.setPaddingBottom(15);
                table.addCell(cellUser);

                Font fuenteUsuarioNombre = new Font(Font.FontFamily.TIMES_ROMAN, 20);
                fuenteUsuarioNombre.setColor(BaseColor.WHITE);

                PdfPCell cellUserNombre = new PdfPCell(new Paragraph("Nombre Producto", fuenteUsuarioNombre));
                cellUserNombre.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUserNombre.setBackgroundColor(BaseColor.DARK_GRAY);
                cellUserNombre.setPaddingBottom(15);
                table.addCell(cellUserNombre);
            }
            for (int x = 0; x < IdsPDF.size(); x++) {
                String nombreCelda = IdsPDF.get(x).toString();

                PdfPCell cellUserNombreFor = new PdfPCell(new Paragraph(nombreCelda));
                cellUserNombreFor.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellUserNombreFor);

                PdfPCell cellUserNombre2For = new PdfPCell(new Paragraph(NombresPDF.get(x)));
                cellUserNombre2For.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cellUserNombre2For);
            }

            document.add(table);

            document.close();

        } catch (Exception e) {
            System.err.println("Ocurrio un error al crear el archivo");
            //System.exit(-1);
        }

    }

    public ArrayList EspacioDisp() {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "select COUNT(IDLote) from Lote";
        ArrayList Arreglo = new ArrayList();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                Arreglo.add(rs.getInt(1));
                Arreglo.add(100 - rs.getInt(1));
            }

        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Arreglo;

    }

    public ArrayList PrecioInv() {

        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        Connection connection = null;
        try {
            connection = Conexion.getConnection();
        } catch (URISyntaxException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        String query = "select SUM(Precio), SUM(Cantidad) from Productos";
        ArrayList Arreglo = new ArrayList();

        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(query);
            int x = 0;
            while (rs.next()) {
                x = x + (rs.getInt(1) * rs.getInt(2));

            }
            Arreglo.add(x);
        } catch (SQLException ex) {
            Logger.getLogger(ServiciosDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Arreglo;
    }
}
