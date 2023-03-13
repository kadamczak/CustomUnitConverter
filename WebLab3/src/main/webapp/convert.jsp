<!DOCTYPE html>
<html>
    <head>
        <title>Unit Converter</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/styleconvert.css">
    </head>
    <body>
        <h1>Unit Converter</h1>

        <div id="main-row">
          <div id="result-flex">
            <p id="result-label">Result:</p>
            <p id="result">${conversionresult}</p>
            
            <form action="/WebLab3" method="POST">
            <button id="back-button" type="submit">Go back</button> 
            </form>
            
          </div> 
        </div>

        <script src="js/scriptconvert.js"></script>
    </body>
</html>
