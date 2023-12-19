const tendersContainer = document.getElementById("tendersContainer");
let tendersData = []; // Initialize tendersData globally

function createTenderCard(tender) {
    const card = document.createElement("div");
    card.classList.add("card");
  
    const title = document.createElement("h2");
    title.textContent = tender.title;
  
    const description = document.createElement("p");
    description.textContent = tender.description;
  
    const totalCompliance = createButton1("Total Compliance", tender.totalCompliance, "compliance-btn");
    const state = createButton("State", tender.state ? tender.state.name : 'null', "state-btn");
    const ministry = createButton("Ministry", tender.ministry ? tender.ministry.name : 'null', "ministry-btn");
    const category = createButton("Category", tender.category ? tender.category.name : 'null', "category-btn");
    const industry = createButton("Industry", tender.industry ? tender.industry.name : 'null', "industry-btn");
    const tendersButton = createButton("Tenders", "View Tender", "tenders-btn", "desp.html");
  
    // Append elements to the card
    card.appendChild(title);
    card.appendChild(description);
    card.appendChild(totalCompliance);
    card.appendChild(state);
    card.appendChild(ministry);
    card.appendChild(category);
    card.appendChild(industry);
    card.appendChild(document.createElement("br"));
    card.appendChild(tendersButton);
  
    return card;
}

function createButton1(label, value, styleClass, link) {
    const button = document.createElement("button");
    button.textContent = `${label} : ${value || 'null'}`; // Use 'null' if value is null
    button.classList.add(styleClass);
  
    if (link) {
        button.onclick = function () {
            window.location.href = link;
        };
    }
  
    return button;
}
  
function createButton(label, value, styleClass, link) {
    const button = document.createElement("button");
    button.textContent = `${value || 'null'}`; // Use 'null' if value is null
    button.classList.add(styleClass);
  
    if (link) {
        button.onclick = function () {
            window.location.href = link;
        };
    }
  
    return button;
}

function displayTenders(tenders) {
    tendersContainer.innerHTML = "";

    tenders.forEach(tender => {
        const tenderCard = createTenderCard(tender);
        tendersContainer.appendChild(tenderCard);
    });
}

function fetchAllTenders() {
    fetch("http://localhost:8080/tender/all")
        .then(response => response.json())
        .then(data => {
            tendersData = data;
            displayTenders(tendersData);
        })
        .catch(error => console.error("Error fetching tenders:", error));
}

function fetchAndDisplayTenders(searchInput) {
    const apiUrl = searchInput.trim() === "" ? "http://localhost:8080/tender/all" : `http://localhost:8080/tender/search/${searchInput}`;

    fetch(apiUrl)
        .then(response => response.json())
        .then(data => {
            tendersData = data;
            displayTenders(tendersData);
        })
        .catch(error => console.error("Error fetching tenders:", error));
}

function searchTenders(event) {
    const searchInput = event.target.value.toLowerCase();

    if (searchInput.trim() === "") {
        // Fetch and display all tenders if search input is empty
        fetchAllTenders();
    } else {
        // Otherwise, fetch and display tenders based on the search input
        fetchAndDisplayTenders(searchInput);
    }
}

window.addEventListener("load", () => {
    // Fetch and display all tenders initially when the page loads
    fetchAllTenders();

    // Add event listener for search input
    document.getElementById("searchInput").addEventListener("input", searchTenders);
});
