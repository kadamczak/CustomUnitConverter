package pl.pols.lab.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pl.polsl.lab.model.Conversion;
import pl.polsl.lab.model.ConversionContainer;
import pl.polsl.lab.model.Converter;
import pl.polsl.lab.model.Unit;
        

//http://localhost:8080/WebLab3/showunit

/**
 * Displays a page with data associated with a given unit.
 * Shows the unit's symbol, name, measuring system and a list of direct conversions
 * available for it.
 * 
 * @author Kinga Adamczak
 * @version 1.0
 */
public class ShowUnitServlet extends HttpServlet {
   
    /**
     * Get unit with a specified symbol from list of units
     * 
     * @param unitList selected list of units
     * @param symbol searched for name
     * @return Unit type object with the selected symbol, null if none found
     */
    private Unit getUnit(List<Unit> unitList, String symbol){
        for(Unit u : unitList){
            if(u.getSymbol().equals(symbol))
                return u;
        }
        return null;
    }
    
    /**
     * Processes a user's request to show detailed information about an unit.
     * 
     * @param request sends the symbol of searched for unit
     * @param response returns the unit's symbol, name, measuring system and a list of direct conversions.
     * @throws ServletException
     * @throws IOException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Get context and session
        ServletContext context = getServletContext();
        HttpSession httpSession = request.getSession();
        
        //Get user input
        String selectedUnit = request.getParameter("showunit");
        
        //If no units selected, return with a message
        if(selectedUnit == null || selectedUnit.equals("")){
            httpSession.setAttribute("exception", "Invalid unit symbol");
            RequestDispatcher view = request.getRequestDispatcher("/showunit.jsp");
            view.forward(request,response);
            return;
        }
        
        //Prepare empty output if user data incorrect
        httpSession.setAttribute("exception", "[Based on current database state]");
        httpSession.setAttribute("symbol", "");
        httpSession.setAttribute("name", "");
        httpSession.setAttribute("system", "");
        
        //Get current data
        loadDatabase();
        
        //Load data to containers and create a converter
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");
        Converter converter = new Converter(container);
        List<Unit> units = (List<Unit>) context.getAttribute("unitList");
        
        //Get unit with specified name from unit list
        Unit unit = getUnit(units, selectedUnit);
        
        //If unit with this name didn't exist, return with a message
        if(unit == null){
            httpSession.setAttribute("exception", "Unit doesn't exist in the database.");
            RequestDispatcher view = request.getRequestDispatcher("/showunit.jsp");
            view.forward(request,response);
            return;
        }
        
        //If unit selection valid, fill screen data
        httpSession.setAttribute("symbol", unit.getSymbol());
        httpSession.setAttribute("name", unit.getFullName());
        httpSession.setAttribute("system", unit.getSystem());
        
        //Search for direct conversions for the given unit
        ArrayList<String> excludedUnits = new ArrayList();
        ArrayList<String> foundUnits = container.findAllDirectConversions(selectedUnit, excludedUnits);
        
        //Display direct conversions in a table
        String tableContent = "";
        for(String foundUnit : foundUnits){
            tableContent += "<tr>";       
            tableContent += "<td>" + foundUnit + "</td>";           
            tableContent += "</tr>";
        }
        
        //Save table content
        request.setAttribute("showunittablecontent", tableContent);   
        
        //Return succesfully
        RequestDispatcher view = request.getRequestDispatcher("/showunit.jsp");
        view.forward(request,response);
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
    }

}
