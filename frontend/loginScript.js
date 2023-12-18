const wrapper = document.querySelector(".wrapper"),
  signupHeader = document.querySelector(".signup header"),
  loginHeader = document.querySelector(".login header");

loginHeader.addEventListener("click", () => {
  wrapper.classList.add("active");
});

signupHeader.addEventListener("click", () => {
  wrapper.classList.remove("active");
});

// Function to handle signup form submission
const signupForm = document.querySelector('.signup form');
signupForm.addEventListener('submit', (event) => {
  event.preventDefault();

  const username = document.querySelector('.signup input[placeholder="Full name"]').value;
  const email = document.querySelector('.signup input[placeholder="Email address"]').value;
  const password = document.querySelector('.signup input[placeholder="Password"]').value;

  const adminCheckbox = document.getElementById('signupCheck');
  const isAdmin = adminCheckbox.checked;

  const userData = {
    username,
    email,
    password,
    isAdmin,
  };

  fetch('http://localhost:8080/auth/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })
    .then((response) => response.json())
    .then((data) => {
      window.location.href = "preferences.html";
      localStorage.setItem('token', data.jwt);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
});

const loginForm = document.querySelector('.form.login');
loginForm.addEventListener("submit", async (event) => {
  event.preventDefault();
  console.log("Login Button");
  await loginFunction();
});

async function loginFunction() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const userData = {
    email: email,
    password: password,
  };

  try {
    const response = await fetch("http://localhost:8080/auth/signin", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(userData),
    });

    if (response.ok) {
      const result = await response.json();
      localStorage.setItem("token", result.jwt);
      console.log(result.jwt);
      window.location.href = "home.html";
    } else {
      document.getElementById("login-error").textContent =
        "Invalid credentials. Please try again.";
    }
  } catch (error) {
    console.error("Error:", error);
  }
}
