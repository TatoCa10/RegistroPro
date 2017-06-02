package com.crunchify.jsp.servlet;

import edu.co.sergio.mundo.dao.ServiciosDAO;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class HSSFCreate extends HttpServlet {

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    public void destroy() {
    }

    /**
     * Processes requests for both HTTP GET and POST methods.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        ServiciosDAO dao = new ServiciosDAO();
        response.setContentType("application/vnd.ms-excel");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("new sheet");
        ArrayList Users = new ArrayList();
        Users = dao.ArrayUsers();
        ArrayList Lotes = new ArrayList();
        Lotes = dao.ArrayLotes();
        ArrayList Prod = new ArrayList();
        Prod = dao.ArrayProd();

        Map<String, Object[]> data = new HashMap<String, Object[]>();
        data.put("1", new Object[]{"", "", "Usuarios", "", "", "", "", "Lotes", "", "", "", "", "Productos"});
        data.put("2", new Object[]{"", "ID", "", "Nombre", "", "", "Id Lote", "", "Nom.Lote", "", "", "Id Prod.", "Cantidad", "Nombre Producto"});

        for (int i = 0; i < Prod.size(); i++) {
            data.put(""+i+"", new Object[]{"", Users.get(i), "", Users.get(i), "", "", Lotes.get(i), "", Lotes.get(i), "", "", Prod.get(i), Prod.get(i), Prod.get(i)});
        }
        for (int x = Prod.size(); x < Lotes.size(); x++) {
            data.put(""+x+"", new Object[]{"", Users.get(x), "", Users.get(x), "", "", Lotes.get(x), "", Lotes.get(x), "", "", "", "", ""});
        }
        for (int y = Prod.size()+Lotes.size(); y < Lotes.size(); y++) {
            data.put(""+y+"", new Object[]{"", Users.get(y), "", Users.get(y), "", "", "", "", "", "", "", "", "", ""});
        }

        data.put("4", new Object[]{3d, "Dean", 700000d});

        Set<String> keyset = data.keySet();
        int rownum = 0;
        for (String key : keyset) {
            Row row = sheet.createRow(rownum++);
            Object[] objArr = data.get(key);
            int cellnum = 0;
            for (Object obj : objArr) {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof Date) {
                    cell.setCellValue((Date) obj);
                } else if (obj instanceof Boolean) {
                    cell.setCellValue((Boolean) obj);
                } else if (obj instanceof String) {
                    cell.setCellValue((String) obj);
                } else if (obj instanceof Double) {
                    cell.setCellValue((Double) obj);
                }
            }
        }

        // Write the output 
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.close();
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP POST method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Example to create a workbook in a servlet using HSSF";
    }
}
