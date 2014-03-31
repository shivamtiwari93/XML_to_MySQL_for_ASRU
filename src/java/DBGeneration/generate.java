/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBGeneration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Shivam Tiwari
 */
@WebServlet(name = "generate", urlPatterns = {"/DBGeneration/generate"})
public class generate extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String urlOfXML = request.getParameter("urlOfXML");
            long startNum = Long.parseLong(request.getParameter("startNum"));
            long endNum = Long.parseLong(request.getParameter("endNum"));
            String nameOfDB = request.getParameter("nameOfDB");
            
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet generate</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h3>Servlet generate at " + request.getContextPath() + "</h3>");
            out.println("<br>");
            out.println("<br>");
            
            Class.forName("com.mysql.jdbc.Driver");
            String connectionURL = "jdbc:mysql://localhost:3306/xml_data_builder_db";
            Connection conn = DriverManager.getConnection (connectionURL,"root","");
            Statement stmt = conn.createStatement();
            ResultSet rs;
            
            out.println("Database created successfully...");
            out.println("<br>");
            
            String line;
            //String tableName;
            String createTableQuery = null;
            
            FileReader fr;
            BufferedReader br;
            
            Pattern patternObj;
            Matcher matcherObj;
            Matcher matcherObj2;
            
            for(long i=startNum;i<=endNum;i++){
                
                fr = new FileReader(urlOfXML + "/" + i + ".xml");
                br = new BufferedReader(fr);
                
                while((line = br.readLine()) != null){
                    patternObj = Pattern.compile(line);
                    matcherObj = patternObj.matcher("BaseID");
                    matcherObj2 = patternObj.matcher("/BaseID");
                    
                    if(matcherObj.find() && !(matcherObj2.find())){
                        
                        line = line.replace("<", "");
                        line = line.replace(">", "");
                        
                        createTableQuery = "CREATE TABLE " + line + " (Time varchar(255)";
                    }
                    
                    patternObj = Pattern.compile(line);
                    matcherObj = patternObj.matcher("Switch");
                    matcherObj2 = patternObj.matcher("/Switch");
                    
                    if(matcherObj.find() && !(matcherObj2.find())){
                        
                        line = line.replace("<", "");
                        line = line.replace(">", "");
                        
                        createTableQuery = createTableQuery + ", " +line + " varchar(15)";
                    }
                }
                
                createTableQuery = createTableQuery + ");";
                
                if(!(stmt.execute(createTableQuery))){
                    out.println("Table creation failed.");
                }
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(generate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(generate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
