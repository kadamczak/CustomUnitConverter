<!DOCTYPE html>
<html>
    <head>
        <title>Unit Converter</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/main.css">
    </head>
    <body>
        <h1>Unit Converter</h1>

        <div id="pages">
        <div id="main-row">
            <div id="converter-container">     
                    
                <form action="convert" method="GET">
                    <div id="converter-grid">

                        <h2 id="convert-title">Convert</h2>
                        
                        <p id="label-original-value">Value:</p>
                        <input class="field-calculate" id="field-original-value" type=text name=originalValue>
                        
                        <p id="label-original-unit">Original unit:</p>
                        <input class="field-calculate" id="field-original-unit" type=text name=originalUnit>
                        
                        <p id="label-target-unit">Target unit:</p>
                        <input class="field-calculate" id="field-target-unit" type=text name=targetUnit>

                        <input id="calculate-button" type="submit" value="Calculate"/>
                    
                    </div>
                </form>
                <div id="exception-message">${exceptionmessage}</div>
            </div>
            
            <div id="tables">
                <div id="table-container">
                    <div id="table-flex">
                        <div id="conversions-table" style="overflow-y:auto;">
                            <table class="table">
                                <tr>
                                <th>Original unit</th>
                                <th>Target unit</th>
                                <th>Operations</th>
                            </tr>
                            ${tablecontent}
                        </table> 
                        </div>
                        <div id="table-buttons">
                            <button type="button" id="addButton">Add</button>
                            <p id="database-info"> ${databasetablemessage} </p>
                            <button type="button" id="deleteButton">Delete</button>                    
                        </div>
                    </div>
                </div>
                
                <div id="unit-table-container">
                    <div id="unit-table-flex">
                        <div id="units-table" style="overflow-y:auto;">
                            <table class="unit">
                                <tr>
                                    <th>Symbol</th>
                                    <th>Full name</th>
                                    <th>System</th>
                                </tr>                           
                            ${unittablecontent}
                        </table> 
                        </div>
                        <div id="unit-table-buttons">
                            <button type="button" id="unitaddButton">Add</button>
                            <button type="button" id="unitupdateButton">Update</button>
                            <button type="button" id="unitdeleteButton">Delete</button>                    
                        </div>
                    </div>
                </div>
                        
                <div id="showunit">
                    <form id="showunit-form" action="showunit" method="GET">
                        <div id="showunit-grid">
                            <p id="label-showunit">Symbol:</p>
                            <input id="field-showunit" class="field-calculate" id="field-showunit" type=text name=showunit>

                            <input id="showButton" type="submit" value="Show unit data"/>

                        </div>
                    </form>            
                </div>
                        
            </div>    

            </div>

            <div id="recent">
              <div id="recent-flex">
                  <p id="label-past">
                      Previous results
                  </p>
                  <div id="session-tab">
                      <p id="session-results">${resulttitle}</p>
                      ${sessionresults}
                  </div>
              </div>
              <div id="table-form">
                <p id="form-title">Add conversion</p>
                <form id="Add" action="index" method="POST">
                        <p class="label-new">Original unit:</p>
                        <input class="field-new" id="field-add-original" type=text name=originalUnit>
                        
                        <p class="label-new">Target unit:</p>
                        <input class="field-new" id="field-add-target" type=text name=targetUnit>
                        
                        <p class="label-new">Operations:</p>
                        <input class="field-new" id="field-add-operations" type=text name=operations>

                        <input class="submit-button" name="button" type="submit" value="Add"/>
                </form>
                <form id="Metric" action="index" method="POST">
                        <p class="label-new">Base unit:</p>
                        <input class="field-new" id="field-metric-original" type=text name=originalUnit>
                        <input class="submit-button" name="button" type="submit" value="Metric"/>
                </form>
                <form id="Delete" action="index" method="POST">
                        <p class="label-new">Original unit:</p>
                        <input class="field-new" id="field-delete-original" type=text name=originalUnit>
                        <p class="label-new">Target unit:</p>
                        <input class="field-new" id="field-delete-target" type=text name=targetUnit>
                        
                        <input class="submit-button" name="button" type="submit" value="Delete"/>
                </form>                
                
                <form id="Addunit" action="index" method="POST">
                        <p class="label-new">Symbol:</p>
                        <input class="field-new" id="field-addunit-symbol" type=text name=symbol>
                        
                        <p class="label-new">Full name:</p>
                        <input class="field-new" id="field-addunit-target" type=text name=name>
                        
                        <p class="label-new">Measuring system:</p>
                        <input class="field-new" id="field-addunit-system" type=text name=system>

                        <input class="submit-button" name="button" type="submit" value="Add unit"/>
                </form>
                
                <form id="Updateunit" action="index" method="POST">      
                        <p class="label-new">Symbol:</p>
                        <input class="field-new" id="field-updateunit-symbol" type=text name=symbol>
                    
                        <p class="label-new">Full name:</p>
                        <input class="field-new" id="field-updateunit-target" type=text name=name>
                        
                        <p class="label-new">Measuring system:</p>
                        <input class="field-new" id="field-updateunit-system" type=text name=system>

                        <input class="submit-button" name="button" type="submit" value="Update unit"/>
                </form>
                
                <form id="Deleteunit" action="index" method="POST">                       
                        <p class="label-new">Symbol:</p>
                        <input class="field-new" id="field-deleteunit-symbol" type=text name=symbol>

                        <input class="submit-button" name="button" type="submit" value="Delete unit"/>
                </form>
                
             </div>
            </div>
        </div>

        
        <script src="js/script.js"></script>
    </body>
</html>
