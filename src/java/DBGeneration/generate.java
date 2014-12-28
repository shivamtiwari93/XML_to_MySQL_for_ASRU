/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DBGeneration;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
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
     * @throws java.lang.ClassNotFoundException
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException, NullPointerException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            
            String urlOfXML = request.getParameter("urlOfXML");
            int startNum = (int) Long.parseLong(request.getParameter("startNum"));
            int endNum = (int) Long.parseLong(request.getParameter("endNum"));
            String nameOfDB = request.getParameter("nameOfDB");
            int i;
            
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
            String connectionURL = "jdbc:mysql://localhost:3306/?";
            Connection conn = DriverManager.getConnection (connectionURL,"root","");
            Statement stmt = conn.createStatement();
            int r = stmt.executeUpdate("CREATE DATABASE "+ nameOfDB);
            
            out.println("Database created successfully...");
            out.println("<br>");
            
            
            
            Class.forName("com.mysql.jdbc.Driver");
            connectionURL = "jdbc:mysql://localhost:3306/" + nameOfDB;
            conn = DriverManager.getConnection (connectionURL,"root","");
            stmt = conn.createStatement();
            ResultSet rs;
            
            out.println("Database connected successfully...");
            out.println("<br><br>");
            
            String line = null;
            String createTableQuery = null;
            String tableName = null;
            String insertIntoQuery = null;
            int switchNumber;
            
            Pattern patternObj;
            Pattern patternObj2;
            Pattern patternObj3;
            Pattern patternObj4;
            
            Matcher matcherObj;
            Matcher matcherObj2;
            Matcher matcherObj3;
            Matcher matcherObj4;
            
            RandomAccessFile[] fileArray = new RandomAccessFile[endNum - startNum + 1];
            
            //out.println(startNum + "," + endNum);
            
            for(i = startNum;i <= endNum; i++){
                                
                fileArray[i - startNum] = new RandomAccessFile(urlOfXML + "\\" + i + ".xml", "r");
                
                while((line = fileArray[i - startNum].readLine()) != null){
                    
                    patternObj = Pattern.compile("BaseID");
                    matcherObj = patternObj.matcher(line);
                    
                    patternObj2 = Pattern.compile("/BaseID");
                    matcherObj2 = patternObj2.matcher(line);
                    
                    if(matcherObj.find()){
            
                        createTableQuery = "CREATE TABLE " + "File" + i + " (SwitchID varchar(23)";
                        
                        break;
                    }
                }
                
                for(int a=1;a<=60;a++){
                    
                    createTableQuery = createTableQuery + ", Time" + a + "min" + " varchar(15)";
                }
                
                createTableQuery += ", PRIMARY KEY (SwitchID));";
                
                //createTableQuery = "CREATE TABLE hello (SwitchID varchar(15), rating varchar(15), PRIMARY KEY(SwitchID));";
                
                out.println(createTableQuery + "<br>");
                
                //out.println("<br><br>Check<Br><br>");

                
                if((stmt.executeUpdate(createTableQuery)) != 0){
                    
                    out.println("Table creation failed");
                    out.println("<br>");
                }
                else{
                    
                    out.println("Table creation successful");
                    out.println("<br>");
                }

                /*fr2[i - (int) startNum] = new FileReader(urlOfXML + "/" + i + ".xml");
                br2[i - (int) startNum] = new BufferedReader(fr2[i - (int) startNum]);*/
                
                fileArray[i - startNum].seek(0);
                
                
                switchNumber = 1;

                while((line = URLEncoder.encode(fileArray[i - startNum].readLine())) != null){
                
                //while((line = file.readLine()) != null){
                    
                    //out.println("<p>" + line + "</p>");
                    

                    patternObj = Pattern.compile("3C");
                    matcherObj = patternObj.matcher(line);
                    
                    patternObj2 = Pattern.compile("Switch");
                    matcherObj2 = patternObj2.matcher(line);
                    
                    patternObj3 = Pattern.compile("/Base");
                    matcherObj3 = patternObj3.matcher(line);
                    
                    patternObj4 = Pattern.compile("%3C%2FBaseID%3A");
                    matcherObj4 = patternObj4.matcher(line);
                    
                    if(matcherObj4.find()){
                        
                        break;
                    }
                    
                    else if(matcherObj2.find()){
                        
                        switchNumber++;
                        if(switchNumber % 2 == 0){
                            
                            insertIntoQuery = "INSERT INTO File" + i + " VALUES ( 'Switch" + switchNumber/2 + "'";
                        }
                        else{
                            
                            insertIntoQuery += ");";
                            
                            out.println("<br><br>" +insertIntoQuery + "<br><br>");
                            
                            if(stmt.executeUpdate(insertIntoQuery) != 0){
                                out.println("Record inserted successfully.<br>");
                                //break;
                            }
                            else{
                                out.println("Record insertion failed.<br>");
                            }
                        }                      
                    }
                    
                    else if(matcherObj.find()){
                    //nothing
                    }
                    
                    else{
                        insertIntoQuery += ", " + line;
                    }
                   
                }
               
                fileArray[i - startNum].close();
            }
            
            out.println("All queries attempted.");
            out.println("<br>");
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