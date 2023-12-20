// preferenceScript.js

function generateComplianceSets() {
    const complianceCount = document.getElementById("complianceCount").value;
    const complianceContainer = document.getElementById("complianceContainer");
    complianceContainer.innerHTML = ""; // Clear previous content

    for (let i = 1; i <= complianceCount; i++) {
        const complianceForm = document.createElement("div");
        complianceForm.style.width = "300px";
        complianceForm.style.margin = "10px";
        complianceForm.style.padding = "20px";
        complianceForm.style.backgroundColor = "#30E3CA";
        complianceForm.style.borderRadius = "10px";
        complianceForm.style.boxShadow = "0 0 5px rgba(0, 0, 0, 0.1)";
        complianceForm.style.textAlign = "left";

        complianceForm.innerHTML = `
            <h2 style="margin-bottom: 10px; color: #11999E;">Compliance Set ${i}</h2>

            <label for="title${i}" style="color: #11999E;">Title:</label>
            <input type="text" id="title${i}" name="title${i}" required>

            <label for="description${i}" style="color: #11999E;">Description:</label>
            <input type="text" id="description${i}" name="description${i}" required>

            <label for="reference${i}" style="color: #11999E;">Reference:</label>
            <input type="text" id="reference${i}" name="reference${i}" required>

            <label for="penalty${i}" style="color: #11999E;">Penalty:</label>
            <input type="text" id="penalty${i}" name="penalty${i}" required>

            <label for="form${i}" style="color: #11999E;">Form:</label>
            <input type="text" id="form${i}" name="form${i}" required>
        `;

        complianceContainer.appendChild(complianceForm);
    }
}

function submitForm(event) {
    event.preventDefault(); // Prevent the default form submission

    const form = document.getElementById('updateForm');
    const formData = new FormData(form);

    // Get form data
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    // Map selected options to their indexes
    formObject.title = document.getElementById('title').value;
    formObject.description = document.getElementById('description').value;
    const ministryOptions = document.getElementById('ministrySelect').options;
    const industryOptions = document.getElementById('industrySelect').options;
    const categoryOptions = document.getElementById('categorySelect').options;
    const stateOptions = document.getElementById('stateSelect').options;
    formObject.totalCompliance = document.getElementById('complianceCount').value;

    

    formObject.ministry = { id: ministryOptions.selectedIndex === 0 ? null : ministryOptions.selectedIndex };
    formObject.industry = { id: industryOptions.selectedIndex === 0 ? null : industryOptions.selectedIndex };
    formObject.category = { id: categoryOptions.selectedIndex === 0 ? null : categoryOptions.selectedIndex };
    formObject.state = { id: stateOptions.selectedIndex === 0 ? null : stateOptions.selectedIndex };

    const complianceSet = [];

    for (let i = 1; i <= formObject.totalCompliance; i++) {
        const complianceSetItem = {
            title: document.getElementById(`title${i}`).value,
            description: document.getElementById(`description${i}`).value,
            reference: document.getElementById(`reference${i}`).value,
            penalty: document.getElementById(`penalty${i}`).value,
            form: document.getElementById(`form${i}`).value
        };

        complianceSet.push(complianceSetItem);
    }

    formObject.complianceSet = complianceSet;

    console.log(formObject);
    // Make an HTTP request to the backend
    sendUpdateRequest(formObject);
}

async function sendUpdateRequest(data) {
    const token = localStorage.getItem('token');

    try {
        const response = await fetch('http://localhost:8080/tender/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(data),
        });

        if (response.ok) {
            const result = await response.json();
            console.log('Creation successful:', result);
        } else {
            console.error('Creation failed:', response.statusText);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}
