

let acc = 0;
function switchLog(nbCheveuxDeValentin){
    let login = document.getElementById("login");
    let inscription = document.getElementById("inscription");

    if(acc == 0){
        login.style.display = "none"
        inscription.style.display = "block"
        acc = 1;
    }
    else{
        login.style.display = "block"
        inscription.style.display = "none"
        acc = 0;
    }
}

switchLog(1);