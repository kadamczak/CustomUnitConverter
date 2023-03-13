package pl.pols.lab.services;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Query;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import pl.polsl.lab.model.Conversion;
import pl.polsl.lab.model.ConversionContainer;
import pl.polsl.lab.model.OperationFormat;
import pl.polsl.lab.model.OperationSequence;
import pl.polsl.lab.model.Unit;

/**
 * Responsible for loading and reloading information from database.
 * 
 * @author Kinga Adamczak
 */
public class DataServlet extends HttpServlet {   
    
    /**
     * Loads database information and saves it in application context.
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
        } finally {          
            //Save containers (even empty ones) into application context
            context.setAttribute("unitList", unitList);          
            context.setAttribute("conversionContainer", container);                   
        }
        
    }
    
    /**
     * Load database for the first time during launch
     */
    @Override
    public void init(){             
        //Create context to store entity manager
        ServletContext context = getServletContext(); 
        
        //Create a connection with database for the first time and save it for future uses
        EntityManagerFactory entityFactory = Persistence.createEntityManagerFactory("UnitConverterDB");
        EntityManager entityManager = entityFactory.createEntityManager();
        
        context.setAttribute("manager", entityManager);
        
        //Load current database contents to containers
        loadDatabase();
        
    }
    
    /**
     * Adds the specified conversion to the database
     * 
     * @param req contains original unit symbol, target unit symbol and operations
     */
    private void addConversion(HttpServletRequest req){
        //Get user input
        String originalUnit = req.getParameter("originalUnit");
        String targetUnit = req.getParameter("targetUnit");
        String operations = req.getParameter("operations");
        
        //If any of the input is empty, show message and return
        if(originalUnit.isEmpty() || targetUnit.isEmpty() || operations.isEmpty()){
            req.setAttribute("exceptionmessage", "Empty data field.");
            return;
        }
        
        //If any of the input is empty, show message and return
        if(originalUnit.length() > 15 || targetUnit.length() > 15 || operations.length() > 30){
            req.setAttribute("exceptionmessage", "Input too long.");
            return;
        }
        
        //Check operation sequence
        OperationFormat format = new OperationFormat();
        Pattern sequenceRegex = format.getOpSequenceRegex();
        Matcher matcher = sequenceRegex.matcher(operations);
        if(!matcher.matches())
        {
            req.setAttribute("exceptionmessage", "Invalid operation sequence.");
            return;
        }
       
        //Get conversion container
        ServletContext context = getServletContext();
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");
        List<Unit> unitList = (List<Unit>) context.getAttribute("unitList");
        
        //If the conversion already exists, show message and return
        if(container.exists(originalUnit, targetUnit)){
            req.setAttribute("exceptionmessage", "Conversion already exists.");
            return;
        }
                
        //Get entity manager
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");
        
        //Begin transaction
        entityManager.getTransaction().begin();
        
        //If included units didn't exist already, add them with "Unknown" additional fields
        if(!unitAlreadyExists(unitList, originalUnit)){
            Unit newUnit = new Unit(originalUnit, "Unknown", "Unknown");
            entityManager.persist(newUnit);
        }
        if(!unitAlreadyExists(unitList, targetUnit)){
            Unit newUnit = new Unit(targetUnit, "Unknown", "Unknown");
            entityManager.persist(newUnit);
        }
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        try{            
            //Add new conversion
            Conversion newConversion = new Conversion(originalUnit, targetUnit, operations);
            entityManager.persist(newConversion);

            entityManager.getTransaction().commit();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            req.setAttribute("exceptionmessage", "A problem occured during transaction.");
        }
        
        req.setAttribute("exceptionmessage", "Conversion succesfully added.");
    }
    
    /**
     * Delete conversion between the specified two units.
     * 
     * @param req contains symbols of both units.
     */
    private void deleteConversion(HttpServletRequest req){ 
        //Get user input
        String originalUnit = req.getParameter("originalUnit");
        String targetUnit = req.getParameter("targetUnit");
        
        //If input empty, show message and return
        if(originalUnit.isEmpty() || targetUnit.isEmpty()){
            req.setAttribute("exceptionmessage", "Empty unit name.");
            return;
        }
        
        //Get container from context
        ServletContext context = getServletContext(); 
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");

        //If container doesn't exist, show message and return
        if(!container.exists(originalUnit, targetUnit)){
            req.setAttribute("exceptionmessage", "Conversion doesn't exist.");
            return;
        }
        
        //Create Conversion object
        Conversion conversionToBeDeleted = container.returnConversion(originalUnit, targetUnit);
        
        //Get entity menager
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");

        //Begin transaction
        entityManager.getTransaction().begin();
        try {
            //Remove conversion
            conversionToBeDeleted = entityManager.merge(conversionToBeDeleted);
            entityManager.remove(conversionToBeDeleted);
            entityManager.getTransaction().commit();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            req.setAttribute("exceptionmessage", "A problem occured during transaction.");
        } finally {
            //entityManager.close();
        }
        
        //Display success message
        req.setAttribute("exceptionmessage", "Conversion succesfully deleted.");      
    }
    
    /**
     * Check if unit of the same symbol already exists in array
     * 
     * @param symbol searched for symbol
     * @return true if unit with the symbol is in the array
     */
    private Boolean unitAlreadyExists(List<Unit> unitList, String symbol){
        for(Unit u : unitList){
            if(u.getSymbol().equals(symbol))
                return true;
        }
        return false;
    }
    
    /**
     * Adds unit to the database
     * 
     * @param req contains unit symbol, name and system
     */
    private void addUnit(HttpServletRequest req){
        //Get user input
        String unitSymbol = req.getParameter("symbol");
        String unitName = req.getParameter("name");
        String unitSystem = req.getParameter("system");
        
        //If entered symbol is empty, show message and return
        if(unitSymbol.isEmpty()){
            req.setAttribute("exceptionmessage", "Empty unit symbol.");
            return;
        }
        
        //If entered symbol is empty, show message and return
        if(unitSymbol.length() > 15 || unitName.length() > 15 || unitSystem.length() > 15){
            req.setAttribute("exceptionmessage", "Input too long.");
            return;
        }
        
        //Get unit list from context
        ServletContext context = getServletContext();
        List<Unit> unitList = (List<Unit>) context.getAttribute("unitList");
        
        //If unit with the same symbol already exists, show message and return
        if(unitAlreadyExists(unitList, unitSymbol)){
            req.setAttribute("exceptionmessage", "Unit already exists.");
            return;
        }

        //If full name or system are empty, fill space with "Unknown"
        unitName = (unitName.isEmpty()) ? "Unknown" : unitName;
        unitSystem = (unitSystem.isEmpty()) ? "Unknown" : unitSystem;
        
        //Create a new Unit object
        Unit newUnit = new Unit(unitSymbol, unitName, unitSystem);
        
        //Get entity manager
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");

        //if(!entityManager.getTransaction().isActive())
        
        //Begin transaction
        entityManager.getTransaction().begin();
        
        try {
            //Add unit to database
            entityManager.persist(newUnit);
            entityManager.getTransaction().commit();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            req.setAttribute("exceptionmessage", "A problem occured during transaction.");
        } finally {
            //entityManager.close();
        }
        
        //Inform that the unit has been succesfully added
        context.setAttribute("exceptionmessage", "Unit succesfully added.");
    }
    
    /**
     * Updates an unit's full name and/or system.
     * 
     * @param req contains unit symbol, name and system
     */
    private void updateUnit(HttpServletRequest req){ 
        //Get uset input
        String unitSymbol = req.getParameter("symbol");
        String unitName = req.getParameter("name");
        String unitSystem = req.getParameter("system");
        
        //If symbol isn't specified, show message and return
        if(unitSymbol.isEmpty()){
            req.setAttribute("exceptionmessage", "Empty unit symbol.");
            return;
        }
        
        //Get unit list from context
        ServletContext context = getServletContext();
        List<Unit> unitList = (List<Unit>) context.getAttribute("unitList");
        
        //If unit with this symbol doesn't exist, show message and return
        if(!unitAlreadyExists(unitList, unitSymbol)){
            req.setAttribute("exceptionmessage", "Unit doesn't exist.");
            return;
        }
        
        //If full name or system empty, fill them with "Unknown"
        unitName = (unitName.isEmpty()) ? "Unknown" : unitName;
        unitSystem = (unitSystem.isEmpty()) ? "Unknown" : unitSystem;
        
        //Create a new Unit object
        Unit updatedUnit = new Unit(unitSymbol, unitName, unitSystem);

        //Get entity manager
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");

        //Perform transaction
        entityManager.getTransaction().begin();
        try {
            //Merge new Unit object with old Unit object
            entityManager.merge(updatedUnit);
            entityManager.getTransaction().commit();
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            req.setAttribute("exceptionmessage", "A problem occured during transaction.");
        } finally {
            //entityManager.close();
        }
        
        //Inform that the unit has been succesfully modified
        req.setAttribute("exceptionmessage", "Unit succesfully updated.");
    }
    
    /**
     * Get Unit object with the specified symbol from list.
     * 
     * @param symbol symbol of the searched for Unit.
     * @return Unit object if found, null if not.
     */
    private Unit getUnitFromList(List<Unit> unitList, String symbol){
        for(Unit u : unitList){
            if(u.getSymbol().equals(symbol)){
                return u;
            }
        }
        return null;
    }    
    
    /**
     * Delete unit with the specified symbol.
     * 
     * @param req contains unit symbol.
     */
    private void deleteUnit(HttpServletRequest req){ 
        //Get uset input
        String unitSymbol = req.getParameter("symbol");
        
        //If symbol empty, show message and return
        if(unitSymbol.isEmpty()){
            req.setAttribute("exceptionmessage", "Empty unit symbol.");
            return;
        }
        
        //Get unit list from context
        ServletContext context = getServletContext();
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");
        List<Unit> unitList = (List<Unit>) context.getAttribute("unitList");
        
        //If unit doesn't exist, show message and return
        if(!unitAlreadyExists(unitList, unitSymbol)){
            req.setAttribute("exceptionmessage", "Unit doesn't exist.");
            return;
        }
        
        //Get Unit object of the specified symbol
        Unit unitToBeRemoved = getUnitFromList(unitList, unitSymbol);
        
        //Get entity manager
        EntityManager entityManager = (EntityManager) context.getAttribute("manager");
        
        //Begin transaction
        entityManager.getTransaction().begin();      
        try {       
            //Delete unit
            unitToBeRemoved = entityManager.merge(unitToBeRemoved);
            entityManager.remove(unitToBeRemoved);
            
            for(Conversion conversion : container)
            {
                if(conversion.getTargetUnit().equals(unitToBeRemoved.getSymbol()))
                {
                    Conversion conv = entityManager.merge(conversion);
                    entityManager.remove(conv);
                }
            }
            
            entityManager.getTransaction().commit();
            
        } catch (javax.persistence.PersistenceException e) {
            e.printStackTrace();
            entityManager.getTransaction().rollback();
            req.setAttribute("exceptionmessage", "A problem occured during transaction.");
        } finally {
            //entityManager.close();
        }
        
        //Show success message
        req.setAttribute("exceptionmessage", "Unit succesfully deleted.");
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        //Update database
        loadDatabase();
        //Get name of pressed button
        String name = request.getParameter("button");
        
        //Perform requested action
        if(name != null){
            switch(name){
            case "Add":
                addConversion(request);
                break;
            case "Delete":
                deleteConversion(request);
                break;
            case "Add unit":
                addUnit(request);
                break;
            case "Update unit":
                updateUnit(request);
                break;
            case "Delete unit":
                deleteUnit(request);
                break;
            }
        }
        
        //Load database after operation
        loadDatabase();
        
        //Get application context
        ServletContext context = getServletContext(); 

        //Load containers from application context
        ConversionContainer container = (ConversionContainer) context.getAttribute("conversionContainer");
        List<Unit> unitList = (List<Unit>) context.getAttribute("unitList");
               
        String tableContent = "";     
        for(Conversion conv : container){
            tableContent += "<tr>";
            
            tableContent += "<td>" +conv.getOriginalUnit() + "</td>";
            tableContent += "<td>" +conv.getTargetUnit() + "</td>";
            tableContent += "<td>" +conv.getOperations() + "</td>";
            
            tableContent += "</tr>";
        }
        
        String unitTableContent = "";   
        for(Unit unit : unitList){
            unitTableContent += "<tr>";
            
            unitTableContent += "<td>" + unit.getSymbol() + "</td>";
            unitTableContent += "<td>" + unit.getFullName()+ "</td>";
            unitTableContent += "<td>" + unit.getSystem() + "</td>";
            
            unitTableContent += "</tr>";
        }
        
        Boolean sessionQueueEmpty = (Boolean) request.getSession().getAttribute("firstEntry");
        sessionQueueEmpty = (sessionQueueEmpty == null) ? true : sessionQueueEmpty;
        
        String previousResults = "";
        if(sessionQueueEmpty){
            request.setAttribute("resulttitle", "Previous session (data from cookies):");
            Cookie[] cookies = request.getCookies();     
            if(cookies != null){
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals("pastSessionResults"))
                        previousResults = cookies[i].getValue();
                }
            }          
        }else{
            request.setAttribute("resulttitle", "Current session:");
            previousResults = (String) request.getSession().getAttribute("sessionresults");
        }        

        request.getSession().setAttribute("firstEntry", sessionQueueEmpty);
        
        request.setAttribute("tablecontent", tableContent);
        request.setAttribute("unittablecontent", unitTableContent);
        request.setAttribute("sessionresults", previousResults);
        
        RequestDispatcher view = request.getRequestDispatcher("/index.jsp");
        view.forward(request,response);
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
