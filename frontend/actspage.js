const actsContainer = document.getElementById("actsContainer");
let actsData = [];

function createActCard(act) {
  const card = document.createElement("div");
  card.classList.add("card");

  const title = document.createElement("h2");
  title.textContent = act.title;

  const description = document.createElement("p");
  description.textContent = act.description;

  const id = document.createElement("id");
  id.textContent = act.id;
  console.log(act.id);

  const totalCompliance = createButton1("Total Compliance", act.totalCompliance, "compliance-btn");
  const state = createButton("State", act.state ? act.state.name : 'null', "state-btn");
  const ministry = createButton("Ministry", act.ministry ? act.ministry.name : 'null', "ministry-btn");
  const category = createButton("Category", act.category ? act.category.name : 'null', "category-btn");
  const industry = createButton("Industry", act.industry ? act.industry.name : 'null', "industry-btn");
  const actsButton = createButton("Acts", "View Act", "acts-btn");
  
  actsButton.addEventListener("click", () => generateAndPrintUniqueID(act.id)); 

  // Append elements to the card
  card.appendChild(title);
  card.appendChild(description);
  card.appendChild(totalCompliance);
  card.appendChild(state);
  card.appendChild(ministry);
  card.appendChild(category);
  card.appendChild(industry);
  card.appendChild(document.createElement("br"));
  card.appendChild(actsButton);

  return card;
}

function createButton1(label, value, styleClass) {
  const button = document.createElement("button");
  button.textContent = `${label} : ${value || 'null'}`;
  button.classList.add(styleClass);
  return button;
}

function createButton(label, value, styleClass) {
  const button = document.createElement("button");
  button.textContent = `${value || 'null'}`;
  button.classList.add(styleClass);
  return button;
}

function generateAndPrintUniqueID(actID) {
  console.log("Unique Act ID:", actID);

  // Redirect to the link
  window.location.href = "act.html"; // You can adjust the link if needed
}

function displayActs(acts) {
  actsContainer.innerHTML = "";

  acts.forEach(act => {
    const actCard = createActCard(act);
    actsContainer.appendChild(actCard);
  });
}

function fetchAllActs() {
  fetch("http://localhost:8080/act/all")
    .then(response => response.json())
    .then(data => {
      actsData = data;
      displayActs(actsData);
    })
    .catch(error => console.error("Error fetching acts:", error));
}

function fetchAndDisplayActs(searchInput) {
  const apiUrl = searchInput.trim() === "" ? "http://localhost:8080/act/all" : `http://localhost:8080/act/search/${searchInput}`;

  fetch(apiUrl)
    .then(response => response.json())
    .then(data => {
      actsData = data;
      displayActs(actsData);
    })
    .catch(error => console.error("Error fetching acts:", error));
}

function searchActs(event) {
  const searchInput = event.target.value.toLowerCase();

  if (searchInput.trim() === "") {
    fetchAllActs();
  } else {
    fetchAndDisplayActs(searchInput);
  }
}

window.addEventListener("load", () => {
  fetchAllActs();
  document.getElementById("searchInput").addEventListener("input", searchActs);
});
