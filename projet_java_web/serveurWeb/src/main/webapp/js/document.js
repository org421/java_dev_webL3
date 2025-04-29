

let chatTab = []
var urlParams = new URLSearchParams(window.location.search);

var dateRef = new Date();
dateRef.setDate(1);
var xhr = new XMLHttpRequest();

let inviteDoc;

function loadInviteDoc(role){
    if(role==="viewer"){
        inviteDoc = 1;
    }
    else{
        inviteDoc = 0;
    }
}

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function supprEvent(event,idEvent){
    if(inviteDoc === 0) {
    event.stopPropagation();
        var valeur = {
            "type": "suppr",
            "idEvent": idEvent.toString(),
        };
        socket.send(JSON.stringify(valeur));
    }
}
function formatDate(date){
    var jour = ('0' + date.getDate()).slice(-2);
    var mois = ('0' + (date.getMonth() + 1)).slice(-2);
    var annee = date.getFullYear();
    var heure = ('0' + date.getHours()).slice(-2);
    var minutes = ('0' + date.getMinutes()).slice(-2);
    var formattedDate = jour + '-' + mois + '-' + annee + ' ' + heure + ':' + minutes +":00";
    return formattedDate;
}

document.addEventListener('DOMContentLoaded', function() {
    connectWebSocket();

    var nextMonth = document.querySelector('[title="Next month"]');
    var prevMonth = document.querySelector('[title="Previous month"]');
    var editNomDoc = document.getElementById("nomDocument");




/*
    var nextMonth = document.getElementsByClassName("fc-next-button fc-button fc-button-primary");
*/
/*
    var prevMonth = document.getElementsByClassName("fc-prev-button");
*/


    nextMonth.addEventListener("click",function (event){
        console.log("Suivant");
        console.log("Bouton 'Next month' cliqué");
        console.log("Événement retourné:", event);
        console.log("Cible de l'événement:", event.target);
        var elements = document.querySelectorAll('.js');

        elements.forEach(function(element) {
            element.parentNode.removeChild(element);
        });

        if(dateRef.getMonth() == 12){
            dateRef.setMonth(1);
            dateRef.setFullYear(dateRef.getFullYear()+1);
        }
        else{
            dateRef.setMonth(dateRef.getMonth()+1);
        }
        dateFin = new Date(dateRef);
        dateFin.setMonth(dateFin.getMonth()+1);
        dateFin.setDate(0);
        console.log(dateFin);



        xhr.open('POST', 'http://localhost:8080/serveurWeb_war_exploded/RecuperationEvenement?id='+idDocument+"&dateDebut="+formatDate(dateRef)+"&dateFin="+formatDate(dateFin), true);

        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                var data = JSON.parse(xhr.responseText);

                console.log('Données de réponse2 : ', data);
                data.forEach(function (evenement){
                    let calendar = document.getElementById("calendar");
                    let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];
                    grid.childNodes.forEach(function (line) {
                        line.childNodes.forEach(function (day) {

                            if(day.dataset.date.localeCompare(evenement["date_debut"])==0){
                                console.log(day.dataset.date);
                                console.log(evenement["date_debut"]);
                                day.innerHTML += "<div style='height: auto; width: 100%; background-color: #0090ff; margin-top: -60%' id='divId_"+ evenement["id"] +"'><h6>"+ evenement["nom"] +"</h6>"+ evenement["description"] +"" +
                                    "<button type='button' style='z-index: 2000; position: relative' class='btn btn-danger' onclick='supprEvent(event,"+ evenement["id"] +")'>Supprimer</button></div>"                            }
                        });
                    });
                });
            } else {
                console.error('La requête a échoué avec un statut : ', xhr.status);
            }
        };

        xhr.onerror = function () {
            console.error('La requête a échoué');
        };

        xhr.send();
        console.log(dateRef);
    });

    prevMonth.addEventListener("click",function (event){
        console.log("Passé");
        console.log("Bouton 'Previous month' cliqué");
        console.log("Événement retourné:", event);
        console.log("Cible de l'événement:", event.target);
        var elements = document.querySelectorAll('.js');

        elements.forEach(function(element) {
            element.parentNode.removeChild(element);
        });

        if(dateRef.getMonth() == 1){
            dateRef.setMonth(12);
            dateRef.setFullYear(dateRef.getFullYear() - 1);
        }
        else{
            dateRef.setMonth(dateRef.getMonth() - 1);
        }
        dateFin = new Date(dateRef);
        dateFin.setMonth(dateFin.getMonth()+1);
        dateFin.setDate(0);
        console.log(dateFin);



        xhr.open('POST', 'http://localhost:8080/serveurWeb_war_exploded/RecuperationEvenement?id='+idDocument+"&dateDebut="+formatDate(dateRef)+"&dateFin="+formatDate(dateFin), true);

        xhr.onload = function () {
            if (xhr.status >= 200 && xhr.status < 300) {
                var data = JSON.parse(xhr.responseText);

                console.log('Données de réponse2 : ', data);
                data.forEach(function (evenement){
                    let calendar = document.getElementById("calendar");
                    let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];
                    grid.childNodes.forEach(function (line) {
                        line.childNodes.forEach(function (day) {

                            if(day.dataset.date.localeCompare(evenement["date_debut"])==0){
                                console.log(day.dataset.date);
                                console.log(evenement["date_debut"]);
                                day.innerHTML += "<div style='height: auto; width: 100%; background-color: #0090ff; margin-top: -60%' id='divId_"+ evenement["id"] +"'><h6>"+ evenement["nom"] +"</h6>"+ evenement["description"] +"" +
                                    "<button type='button' style='z-index: 2000; position: relative' class='btn btn-danger' onclick='supprEvent(event,"+ evenement["id"] +")'>Supprimer</button></div>"                             }
                        });
                    });
                });
            } else {
                console.error('La requête a échoué avec un statut : ', xhr.status);
            }
        };

        xhr.onerror = function () {
            console.error('La requête a échoué');
        };

        xhr.send();
        console.log(dateRef);
    });


    dateFin = new Date(dateRef);
    dateFin.setMonth(dateFin.getMonth()+1);
    dateFin.setDate(0);
    console.log(dateFin);



    xhr.open('POST', 'http://localhost:8080/serveurWeb_war_exploded/RecuperationEvenement?id='+idDocument+"&dateDebut="+formatDate(dateRef)+"&dateFin="+formatDate(dateFin), true);

    xhr.onload = function () {
        if (xhr.status >= 200 && xhr.status < 300) {
            var data = JSON.parse(xhr.responseText);

            console.log('Données de réponse2 : ', data);
            data.forEach(function (evenement){
                let calendar = document.getElementById("calendar");
                let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];
                grid.childNodes.forEach(function (line) {
                    line.childNodes.forEach(function (day) {

                        if(day.dataset.date.localeCompare(evenement["date_debut"])==0){
                            console.log(day.dataset.date);
                            console.log(evenement["date_debut"]);
                            day.innerHTML += "<div style='height: auto; width: 100%; background-color: #0090ff; margin-top: -60%' id='divId_"+ evenement["id"] +"'><h6>"+ evenement["nom"] +"</h6>"+ evenement["description"] +"" +
                                "<button type='button' style='z-index: 2000; position: relative' class='btn btn-danger' onclick='supprEvent(event,"+ evenement["id"] +")'>Supprimer</button></div>"                        }
                    });
                });
            });
        } else {
            console.error('La requête a échoué avec un statut : ', xhr.status);
        }
    };

    xhr.onerror = function () {
        console.error('La requête a échoué');
    };

    xhr.send();
    console.log(dateRef);


    editNomDoc.addEventListener("keypress",function (event){
        var valeur = {
            "type": "editNom",
            "nom": editNomDoc.value,
            "idDocument": idDocument.toString(),
        };
        socket.send(JSON.stringify(valeur));
    });
});

var idDocument = urlParams.get('id');
console.log(idDocument);

var socket = new WebSocket("ws://localhost:8080/serveurWeb_war_exploded/WebsocketDocument?idDocument="+idDocument);

function envoyerEvenement(){
    var nom = document.getElementById("eventName").value;
    var description = document.getElementById("eventDescription").value;
    var heureDebut = document.getElementById("eventBegin").value;
    var dateFin = document.getElementById("eventEnd").value;
    unloadForm();

    var valeur = {
        "type": "evenement",
        "nom": nom,
        "description": description,
        "heureDebut": heureDebut,
        "dateFin": dateFin,
        "idDocument": idDocument,
        "idEvenement": null,
    };

    if (socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(valeur));
    } else {
        connectWebSocket();
    }
}


function connectWebSocket() {
    var idDocument = urlParams.get('id');
    var storedState = JSON.parse(localStorage.getItem('websocketState'));

    if (storedState && storedState.url && storedState.idDocument === idDocument) {
        socket = new WebSocket(storedState.url);

        socket.onopen = function(event) {
            console.log("Connexion établie avec le serveur WebSocket.");
        };

        socket.onmessage = function(event) {
            console.log("Message reçu du serveur: " + event.data);
            var data = JSON.parse(event.data);
            if(data["type"] == "evenement"){
                var dateref2 = new Date(data["heureDebut"]);
                dateref2.setDate(1);
                if(dateref2.getTime() - dateRef.getTime() <= 86400000){
                    console.log("On affiche la date");
                    let calendar = document.getElementById("calendar");
                    let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];
                    grid.childNodes.forEach(function (line) {
                        line.childNodes.forEach(function (day) {
                            if(day.dataset.date.localeCompare(data["heureDebut"].split('T')[0])==0){
                                console.log(day.dataset.date);
                                console.log(data["heureDebut"].split('T')[0]);
                                day.innerHTML += "<div style='height: auto; width: 100%; background-color: #0090ff; margin-top: -60%' id='divId_"+ data["id"] +"'><h6>"+ data["nom"] +"</h6>"+ data["description"] +"" +
                                    "<button type='button' style='z-index: 2000; position: relative' class='btn btn-danger' onclick='supprEvent(event,"+ data["id"] +")'>Supprimer</button></div>"                            }
                        });
                    });
                }
                else{
                    console.log("On affiche pas la date");
                }
            }
            else if(data["type"] == "message"){
                chatTab.push(data);
            }
            else if(data["type"] == "suppr"){
                var element = document.getElementById("divId_"+data["idEvent"]);
                element.remove();
            }
            else if(data["type"] == "editNom"){
                var editNomDocAp = document.getElementById("nomDocument");
                editNomDocAp.value = data["nom"];
                var event = new Event('change');
                editNomDocAp.dispatchEvent(event);
            }
        };

        socket.onerror = function(error) {
            console.error("Erreur sur la connexion WebSocket: " + error.message);
        };

        socket.onclose = function(event) {
            if (event.wasClean) {
                console.log("Connexion WebSocket fermée proprement.");
            } else {
                console.log("Connexion WebSocket fermée avec erreur.");
                console.log("Code d'erreur: " + event.code + ", raison: " + event.reason);
            }
        };
    } else {
        socket = new WebSocket("ws://localhost:8080/serveurWeb_war_exploded/WebsocketDocument?idDocument=" + idDocument);

        socket.onopen = function(event) {
            console.log("Connexion établie avec le serveur WebSocket.");
            localStorage.setItem('websocketState', JSON.stringify({ url: socket.url, idDocument: idDocument }));
        };

        socket.onmessage = function(event) {
            console.log("Message reçu du serveur: " + event.data);
            var data = JSON.parse(event.data);
            if(data["type"] == "evenement"){
                var dateref2 = new Date(data["heureDebut"]);
                dateref2.setDate(1);
                if(dateref2.getTime() - dateRef.getTime() <= 86400000){
                    console.log("On affiche la date");
                    let calendar = document.getElementById("calendar");
                    let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];
                    grid.childNodes.forEach(function (line) {
                        line.childNodes.forEach(function (day) {
                            if(day.dataset.date.localeCompare(data["heureDebut"].split('T')[0])==0){
                                console.log(day.dataset.date);
                                console.log(data["heureDebut"].split('T')[0]);
                                day.innerHTML += "<div style='height: auto; width: 100%; background-color: #0090ff; margin-top: -60%' id='divId_"+ data["id"] +"'><h6>"+ data["nom"] +"</h6>"+ data["description"] +"" +
                                    "<button type='button' style='z-index: 2000; position: relative' class='btn btn-danger' onclick='supprEvent(event,"+ data["id"] +")'>Supprimer</button></div>"
                            }
                        });
                    });
                }
                else{
                    console.log("On affiche pas la date");
                }
            }
            else if(data["type"] == "message"){
                chatTab.push(data);
            }
            else if(data["type"] == "suppr"){
                var element = document.getElementById("divId_"+data["idEvent"]);
                element.remove();
            }
        };

        socket.onerror = function(error) {
            console.error("Erreur sur la connexion WebSocket: " + error.message);
        };

        socket.onclose = function(event) {
            if (event.wasClean) {
                console.log("Connexion WebSocket fermée proprement.");
            } else {
                console.log("Connexion WebSocket fermée avec erreur.");
                console.log("Code d'erreur: " + event.code + ", raison: " + event.reason);
            }
        };
    }
}


