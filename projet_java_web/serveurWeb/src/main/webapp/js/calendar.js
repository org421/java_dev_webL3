let days = [];
let daysIndex = 0;
let divOut //la balise contenant le formulaire
let invite = -1;

function chargerDonneeCalendrier(role){
    if(role==="viewer"){
        invite = 1;
    }
    else{
        invite = 0;
    }
}

let clickHandler = function(){
    console.log(daysIndex);
    var date = this.getAttribute('data-date');
    console.log(this.getAttribute('data-date'));
    if(invite === 0) {
        form(daysIndex, date);
    }
}

let unloadForm = function() {
  document.body.removeChild(divOut);
}


document.addEventListener('DOMContentLoaded', function() {
  let calendarEl = document.getElementById('calendar');

  let calendar = new FullCalendar.Calendar(calendarEl, {
    headerToolbar: {
      start: 'dayGridMonth,timeGridWeek,timeGridDay custom1',
      center: 'title',
      end: 'custom2 prevYear,prev,next,nextYear'
    }
  });

  calendar.render();
  loadDays(); //ajoute des listeners sur les jours du mois
  loadButtons(); //ajoute des listeners sur les boutons

})

function form(index,date){ //donne un formulaire
    //alert("vous avez clique sur le jour "+(index+1)+" de ce mois");
    console.log("vous avez clique sur le jour "+(index+1)+" de ce mois");

    divOut = document.createElement('div');
    divOut.setAttribute('id', 'divOut');
    divOut.style.position = 'fixed';
    divOut.style.top = '0';
    divOut.style.width = '100%';
    divOut.style.left = '0';
    divOut.style.height = '100%';
    divOut.style.backgroundColor = 'rgba(0,0,0,0.5)';// 0.5: Fond semi-transparent
    divOut.style.display = 'flex';
    divOut.style.justifyContent = 'center';
    divOut.style.alignItems = 'center';
    divOut.style.zIndex = '9999';
    divOut.style.padding = '100px';
    divOut.classList.add('position-fixed', 'top-0', 'start-0', 'w-100', 'h-100', 'd-flex', 'justify-content-center', 'align-items-center', 'bg-opacity-50');

    let divForm = document.createElement('div');
    divForm.setAttribute('id', 'divForm');
    divForm.style.width = '100%';
    divForm.style.height = '100%';
    divForm.style.backgroundColor = 'rgba(255,255,255)';
    divForm.style.display = 'flex';
    divForm.style.justifyContent = 'center';
    divForm.style.alignItems = 'center';
    divForm.style.zIndex = '9999';
    divForm.classList.add('container', 'p-5', 'rounded');

    let form = document.createElement('form');
    form.setAttribute('id', 'formEvent');
    form.setAttribute('method', 'post');
    form.setAttribute('action', ''); // L'action du formulaire, à définir
    form.classList.add('row', 'g-3');

    // Nom de l'événement
    let eventName = document.createElement('input');
    eventName.setAttribute('type', 'text');
    eventName.setAttribute('name', 'eventName');
    eventName.setAttribute('id', 'eventName');
    eventName.setAttribute('placeholder', 'Evenement...');
    eventName.classList.add('form-control');
    eventName.style.marginBottom = '10px';

    // Description de l'événement
    let eventDescription = document.createElement('input');
    eventDescription.setAttribute('type', 'textArea');
    eventDescription.setAttribute('name', 'eventDescription');
    eventDescription.setAttribute('id', 'eventDescription');
    eventDescription.setAttribute('placeholder', 'Description...');
    eventDescription.classList.add('form-control');
    eventDescription.style.marginBottom = '10px';

    // heure de début de l'evenement
    let eventBegin = document.createElement('input');
    eventBegin.setAttribute('type', 'datetime-local');
    eventBegin.setAttribute('name', 'eventBegin');
    eventBegin.setAttribute('id', 'eventBegin');
    let dateBegin = new Date(date);
    dateBegin.setDate(dateBegin.getDate() + 1);
    dateBegin.setHours(0, 0, 1, 0);
    let defaultDateTime = dateBegin.toISOString().slice(0, 16);
    eventBegin.setAttribute('value', defaultDateTime);

    eventBegin.classList.add('form-control');
    eventBegin.style.marginBottom = '10px';


    // date de fin de l'evenement
    let eventEnd = document.createElement('input');
    eventEnd.setAttribute('type', 'datetime-local');
    eventEnd.setAttribute('name', 'eventEnd');
    eventEnd.setAttribute('id', 'eventEnd');
    eventEnd.setAttribute('value', defaultDateTime);

    eventEnd.classList.add('form-control');
    eventEnd.style.marginBottom = '10px';

    // Bouton d'enregistrement
    let saveButton = document.createElement('input');
    saveButton.setAttribute('type', 'button');
    saveButton.setAttribute('value', 'Enregistrer');
    saveButton.onclick = envoyerEvenement;//envoyerEvenement;
    saveButton.classList.add('btn', 'btn-primary', 'me-3');

    // Bouton d'annulation
    let cancelButton = document.createElement('input');
    cancelButton.setAttribute('type', 'button');
    cancelButton.setAttribute('value', 'Annuler');
    cancelButton.onclick = unloadForm;
    cancelButton.classList.add('btn', 'btn-secondary');

    // Div pour centrer verticalement les boutons
    let buttonsContainer = document.createElement('div');
    buttonsContainer.classList.add('d-flex', 'justify-content-center');

    buttonsContainer.appendChild(saveButton);
    buttonsContainer.appendChild(cancelButton);

    // Ajout des champs au formulaire
    form.appendChild(eventName);
    form.appendChild(eventDescription);
    form.appendChild(eventBegin);
    form.appendChild(eventEnd);
    form.appendChild(buttonsContainer);

    // Ajout du formulaire à la divForm et du divForm à la divOut
    divForm.appendChild(form);
    divOut.appendChild(divForm);

    // Ajout du div à la page
    document.body.appendChild(divOut);
}

function loadDays() {

  let calendar = document.getElementById("calendar");
  let grid = calendar.childNodes[1].childNodes[0].childNodes[0].childNodes[1].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[0].childNodes[1];

  grid.childNodes.forEach(function (line) {
    line.childNodes.forEach(function (day) {
      //console.log('day');
      let classes = day.classList
      let index = 0;
      classes.forEach(function (className) {
        if (className == 'fc-day-other') {
          index = 1;
        }
        //console.log(className);
      })
      if (index == 0) {
        days.push(day);
      }
    })
  })
  //console.log(days)
        for (let i = 0; i < days.length; i++) {
            daysIndex = i;
            let day = days[daysIndex];
            day.addEventListener('click', clickHandler)
            daysIndex = 0;
        }
}

function unloadDays() {
  for(let i=0; i<days.length; i++){
    daysIndex = i;
    days[daysIndex].removeEventListener('click', clickHandler);
  }
  days = []
  daysIndex=0
}

function loadButtons() {
  let calendar = document.getElementById("calendar");
  let elements = calendar.childNodes[0].childNodes[2].childNodes[1];
  //console.log(elements)

  elements.childNodes.forEach(function (button) {
    //console.log(button);
    button.addEventListener('click', function (){
      unloadDays(); //enlève tous les events listeners
      loadDays(); //ajoute les events listeners sur les jours du nouveau mois
      //console.log(days)
    })
  })
}

function supprimerToday(){
    let calendar = document.getElementById("calendar");
  let elements = calendar.childNodes[0].childNodes[2].childNodes[0];
  //console.log(elements)
    elements.remove();
}
