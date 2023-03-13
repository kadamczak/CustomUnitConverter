function changeVisibility(visibleForm){
    document.getElementById("Add").style.display = (visibleForm === "Add") ? "block" : "none";
    document.getElementById("Delete").style.display = (visibleForm === "Delete") ? "block" : "none";
    
    document.getElementById("Addunit").style.display = (visibleForm === "Addunit") ? "block" : "none";
    document.getElementById("Updateunit").style.display = (visibleForm === "Updateunit") ? "block" : "none";
    document.getElementById("Deleteunit").style.display = (visibleForm === "Deleteunit") ? "block" : "none";
  
}

function displayForm(title, formName){
    document.getElementById("form-title").textContent = title;
    changeVisibility(formName);
}

document.getElementById("addButton").addEventListener("click", function(){ displayForm("Add conversion", "Add"); } );
document.getElementById("deleteButton").addEventListener("click", function(){ displayForm("Delete conversion", "Delete"); } );

document.getElementById("unitaddButton").addEventListener("click", function(){ displayForm("Add unit", "Addunit"); } );
document.getElementById("unitupdateButton").addEventListener("click", function(){ displayForm("Update unit", "Updateunit"); } );
document.getElementById("unitdeleteButton").addEventListener("click", function(){ displayForm("Delete unit", "Deleteunit"); } );