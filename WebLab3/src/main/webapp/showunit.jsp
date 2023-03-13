<!DOCTYPE html>
<html>
    <head>
        <title>Unit Converter</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/styleshowunit.css">
    </head>
    <body>
        <h1>Unit Converter</h1>

        <div id="main-row">
          <div id="result-flex">
            <p class="result-label">${exception}</p>
            <p class="result-label" id="symbol">Symbol: ${symbol}</p>
            <p class="result-label">Name: ${name}</p>
            <p class="result-label">System: ${system}</p>
            <p class="result-label" id="conv">Includes direct conversions to:</p>
            
            <div id="table-container">
                <div id="table-flex">
                    <div id="conversions-table" style="overflow-y:auto;">
                        <table class="table">
                        <tr>
                            <th>Unit symbol</th>
                        </tr>
                        ${showunittablecontent}
                    </table> 
                    </div>
                </div>
            </div>
            
            <form action="/WebLab3" method="POST">
            <button id="back-button" type="submit">Go back</button> 
            </form>
            
          </div> 
        </div>

        <script src="js/scriptshowunit.js"></script>
    </body>
</html>
