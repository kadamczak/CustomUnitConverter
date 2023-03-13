package pl.pols.lab.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import pl.polsl.lab.model.Conversion;
import pl.polsl.lab.model.ConversionContainer;
import pl.polsl.lab.model.Converter;
import pl.polsl.lab.model.Unit;
import pl.polsl.lab.model.ValueWithUnit;
        

//http://localhost:8080/WebLab3/Convert

/**
 * Servlet responsible for displaying a page with
 * result of desired conversion between units
 * 
 * @author Kinga Adamczak
 * @version 1.0
 */
public class ConvertServlet extends HttpServlet {
    
    /**
     * Updates history of past conversions that is displayed on the home page.
     * 
     * @param request contains session conversion history
     * @param response contains the complete, current conversion history
     * @param lastResultHtml new conversion currently added to history
     */
    private void updatePastList(HttpServletRequest request, HttpServletResponse response, String lastResultHtml){
        HttpSession session = request.getSession();
        
        //Get list of conversions made during current session
        Deque<String> sessionList = (Deque<String>) session.getAttribute("sessionresultsList");   
        String sessionHtml = "";
        
        //If list null, create a new, empty list
        if(sessionList == null){
            sessionList = new LinkedList<String>();
        }
      
        //Conversion performed just now
        sessionList.addFirst(lastResultHtml);
        
        //Make sure that the list doesn't go over 8 elements (so it can be displayed properly)
        if(sessionList.size() > 8){
            sessionList.removeLast();
        }
        
        //Create complete html from the session conversion history
        for(String p : sessionList){
            sessionHtml += p;
        }
        
        //Save complete html in cookies (for next session)
        Cookie resultsCookie = new Cookie( "pastSessionResults", sessionHtml );
        resultsCookie.setMaxAge(60*60*60);
        response.addCookie(resultsCookie);
        
        //Inform main page that session history contains elements, so it 
        //should display current session history instead of previous
        Boolean sessionQueueEmpty = false;
        session.setAttribute("firstEntry", sessionQueueEmpty);
  
        //Save session html in application context
        session.setAttribute("sessionresults", sessionHtml);
        session.setAttribute("sessionresultsList", sessionList);
    }
   
    /**
     * Processes user's request to perform a conversion between specified units.
     * 
     * @param request specifies original unit, target unit and value
     * @param response contains the conversion result
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = getServletContext();
        
        //Update local information
        loadDatabase();
        
        //Prepare containers
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");
        Converter converter = new Converter(container);
        
        try{             
            //Get user parameters
            Double originalValue = Double.valueOf(request.getParameter("originalValue")); 
            String originalUnit = request.getParameter("originalUnit");
            String targetUnit = request.getParameter("targetUnit");                       
            
            //Perform conversion
            ValueWithUnit input = new ValueWithUnit(originalValue, originalUnit);
            ValueWithUnit result = converter.convert(input, targetUnit);

            String resultText = "";
            
            //Change output depending on result
            if(result == null){
                resultText = "Units not in database.";
            }else{
                resultText = originalValue + " " + originalUnit + " = " + result.toString();
                String resultHtml = "<p>" + resultText + "</p>";
                
                //Add conversion to conversion history
                updatePastList(request, response, resultHtml);
            }

            context.setAttribute("conversionresult", resultText);        
          
        }catch(NumberFormatException e){
            context.setAttribute("conversionresult", "Wrong number format.");     
        } finally{
            RequestDispatcher view = request.getRequestDispatcher("/convert.jsp");
            view.forward(request,response);
        }

    }
    
    /**
     * Loads current state of database into application context.
     * Saved data is stored in "conversionContainer" and "unitList".
     */
    void loadDatabase()
    {
        //Get entity manager from application context
        ServletContext context = getServletContext();        
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");
        
        //Create new empty containers
        ConversionContainer container = new ConversionContainer();
        List<Unit> unitList = Collections.emptyList();
        
        entityManager.getTransaction().begin();
        //Load data from database to containers
        try {
            Query query = entityManager.createQuery("SELECT u FROM Unit u");
            unitList = query.getResultList();
            
            query = entityManager.createQuery("SELECT c FROM Conversion c");
            List<Conversion> conversions = query.getResultList();
            for(Conversion c : conversions){
                container.add(c);
            }
            
            entityManager.getTransaction().commit();
            context.setAttribute("databasetablemessage", "Data in tables comes from database.");    
        } catch (PersistenceException e) {
            //If exception occured - make loaded containers empty
            container = new ConversionContainer();
            unitList = Collections.emptyList();
                     
            entityManager.getTransaction().rollback();
            context.setAttribute("databasetablemessage", "Database not connected.");
            context.setAttribute("conversionresult", "Database not connected.");        
        } finally {           
            //Save containers (even empty ones) into application context
            context.setAttribute("unitList", unitList);          
            context.setAttribute("conversionContainer", container);          
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
