<%-- 
    Document   : generate
    Created on : Mar 29, 2014, 11:09:55 AM
    Author     : Shivam Tiwari
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1 align="center">Fill details of XML files</h1>
        <p>
            <form action="generate" method="post">
                URL of the XML folder       :   <input type="text" name ="urlOfXML"><br><br>
                Starting file number        :   <input type="text" name="startNum"><br><br>
                Ending file number          :   <input type="text" name="endNum"><br><br>
                Name of DataBase to make    :   <input type="text" name="nameOfDB"><br><br>
                <input type="submit" value="Confirm">
            </form>
        </p>
    </body>
</html>
