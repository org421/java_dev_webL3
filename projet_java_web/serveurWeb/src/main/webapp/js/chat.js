let userPseudo;
let userID;

let chatOpen = 0 //pour savoir si on à ouvert le chat

function chargerUser(id, pseudo, role){
    userID = id;
    userPseudo = pseudo;
    chargerDonneeCalendrier(role);
    loadInviteDoc(role);
    supprimerToday();
}
function chatLoad() {
    chatOpen = 1;
    if(document.getElementById("divChatOut") != null){
        chatUnload();
    }

    //alert("clique chat");
    let divChatOut = document.createElement('div');
    divChatOut.setAttribute('id', 'divChatOut');
    divChatOut.style.position = 'fixed';
    divChatOut.style.top = '0';
    divChatOut.style.left = '0';
    divChatOut.style.display= 'flex';
    divChatOut.style.width= '100%';
    divChatOut.style.height= '100%';
    divChatOut.style.backgroundColor = 'rgba(0,0,0,0.5)';// 0.5: Fond semi-transparent
    divChatOut.style.justifyContent = 'center';
    divChatOut.style.alignItems = 'center';
    divChatOut.style.zIndex = '9999';
    divChatOut.style.paddingLeft = '60%';

    let divChatIn = document.createElement('div');
    divChatIn.setAttribute('id', 'divChatIn');
    divChatIn.style.backgroundColor = 'rgba(255,255,255)';
    divChatIn.style.flex= '1';
    divChatIn.style.display= 'flex';
    divChatIn.style.flexDirection= 'column';
    divChatIn.style.width= '100%';
    divChatIn.style.height = '100%';
    divChatIn.style.justifyContent = 'center';
    divChatIn.style.alignItems = 'center';
    divChatIn.style.zIndex = '9999';

    divChatIn.style.borderTopLeftRadius = '10px';
    divChatIn.style.borderBottomLeftRadius = '10px';

    divChatIn.classList.add('container');

    let divChatHistory = document.createElement('ul');
    divChatHistory.setAttribute('id', 'divChatHistory');
    divChatHistory.style.backgroundColor = 'rgba(255,255,255)';
    divChatHistory.style.flex= '1';
    divChatHistory.style.width= '100%';
    divChatHistory.style.height= '100%';
    divChatHistory.style.borderBottom = '1px solid black'
    divChatHistory.style.zIndex = '9999';
    divChatHistory.style.marginBottom = '10px';

    divChatHistory.classList.add('list-group','flex-fill', 'list-unstyled');
    divChatHistory.style.maxWidth = "600px"; // Limite de largeur de la zone d'affichage des messages
    divChatHistory.style.overflowX = "auto";
    divChatHistory.style.maxHeight = "600px"; // Limite de la hauteur de la zone d'affiche des messages (jsp si celui la est utile)
    divChatHistory.style.overflowY = "auto";


    let chat = document.createElement('form');
    chat.setAttribute('id', 'ChatID');
    chat.setAttribute('method', 'post');
    chat.setAttribute('action', ''); // L'action du formulaire, à définir
    chat.style.width= '100%';

    // message à envoyer

    let chatMessage = document.createElement('input');
    chatMessage.setAttribute('id', 'chatMessage');
    chatMessage.setAttribute('type', 'textArea');
    chatMessage.setAttribute('name', 'chatMessage');
    chatMessage.setAttribute('placeholder', 'Ecrire...');

    chatMessage.classList.add('form-control');
    chatMessage.style.marginBottom = '10px' ;

    // Bouton d'enregistrement
    let saveButton = document.createElement('input');
    saveButton.setAttribute('type', 'button');
    saveButton.setAttribute('value', 'Envoyer');
    saveButton.classList.add('btn', 'btn-primary', 'mr-3');
    saveButton.onclick = chatSend;

    // Bouton d'annulation
    let cancelButton = document.createElement('input');
    cancelButton.setAttribute('type', 'button');
    cancelButton.setAttribute('value', 'Fermer');
    saveButton.classList.add('btn', 'btn-secondary');
    cancelButton.onclick = chatUnload;

    // Div pour contenir les boutons
    let buttonsContainer = document.createElement('div');
    buttonsContainer.classList.add('d-flex', 'justify-content-between');

    buttonsContainer.appendChild(saveButton);
    buttonsContainer.appendChild(cancelButton);

    // Ajout des champs au formulaire
    chat.appendChild(chatMessage);
    chat.appendChild(buttonsContainer);

    divChatIn.appendChild(divChatHistory);
    divChatIn.appendChild(chat);

    divChatOut.appendChild(divChatIn);

    // Ajout du div à la page
    document.body.appendChild(divChatOut);
}


let chatUnload = function(){
    document.body.removeChild(divChatOut);
    //on désaffiche les messages
    chatTab.forEach(function(message, index){
        if(message['type'] == 'message'){
                message['affiche'] = 0;
        }
    })
    chatOpen = 0

}


let chatSend = function() {
    let chat = document.getElementById("chatMessage");
    //console.log("envoie du message: "+chat.value);
    //console.log("user: "+ userPseudo)

    //construction de la Map
    let map = {'type' : 'message', 'id' : userID, 'pseudo' : userPseudo, 'contenu' : chat.value, 'affiche': 0}
    socket.send(JSON.stringify(map));

    //envoie du message
    //...

    chat.value = ''; //on supprime le message dans la zone d'écriture
}


/*
let chat = document.getElementById('chatMessage');

chat.addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        chatSend();
    }
});
*/


let chatLoadMessage = function() {
    chatTab.forEach(function(message, index){
        //console.log(message['type'])
        let chatHistory = document.getElementById("divChatHistory");
        if(message['type'] == 'message'){
            if(message['affiche']==0) { //on vérifie si l'élément n'est pas déjà affiché
                let chatElement = document.createElement("li")

                let chatPseudo = message['pseudo'];
                let chatContent = message['contenu'];

                let pseudoElement = document.createElement("strong");
                pseudoElement.textContent = chatPseudo;
                pseudoElement.classList.add("font-weight-bold");
                if(message['id'] == userID){ //si c'est l'utilisateur qui a envoyer le message, on affiche son pseudo en bleu
                    pseudoElement.style.color = "blue";
                }

                let contenuElement = document.createElement("span");
                contenuElement.textContent = ": " + chatContent;

                chatElement.appendChild(pseudoElement);
                chatElement.appendChild(contenuElement);

                chatHistory.appendChild(chatElement) //on envoie le message
                message['affiche'] = 1; //on n'affichera plus ce message
            }
        }
        //chatTab.splice(index, 1) //à vérifier si ca marche toujours avec plusieurs donnée d'un coup
    })
}

setInterval(function () {
    if(chatOpen)
        chatLoadMessage();
}, 100); //à modifier
