// preferenceScript.js

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
  const ministryOptions = document.getElementById('ministrySelect').options;
  const industryOptions = document.getElementById('industrySelect').options;
  const categoryOptions = document.getElementById('categorySelect').options;
  const stateOptions = document.getElementById('stateSelect').options;

  formObject.ministryId = ministryOptions.selectedIndex === 0 ? null : ministryOptions.selectedIndex;
  formObject.industryId = industryOptions.selectedIndex === 0 ? null : industryOptions.selectedIndex;
  formObject.categoryId = categoryOptions.selectedIndex === 0 ? null : categoryOptions.selectedIndex;
  formObject.stateId = stateOptions.selectedIndex === 0 ? null : stateOptions.selectedIndex;

  console.log(formObject);
    // Make an HTTP request to the backend
    sendUpdateRequest(formObject);
  }
  
  async function sendUpdateRequest(data) {
    const token = localStorage.getItem('token');
  
    try {
      const response = await fetch('http://localhost:8080/user/update', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(data),
      });
  
      if (response.ok) {
        const result = await response.json();
        console.log('Update successful:', result);
      } else {
        console.error('Update failed:', response.statusText);
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }
  