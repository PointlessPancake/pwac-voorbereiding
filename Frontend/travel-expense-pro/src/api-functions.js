const BASE_URL = "http://localhost:8080";

export async function getToken(username, password) {
  try {
    const response = await fetch(`${BASE_URL}/users/login`, {
      mode: "cors",
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ username, password }),
    });
    if (response.status === 401) {
      throw new Error(
        "Ongeldige gebruikersnaam of wachtwoord. Probeer het opnieuw."
      );
    }
    const data = await response.json();
    return data.accessToken;
  } catch (e) {
    if (
      e.message.includes(
        "Ongeldige gebruikersnaam of wachtwoord. Probeer het opnieuw."
      )
    ) {
      throw e;
    }
    throw new Error("Er is iets misgegaan. Probeer het later nog eens.");
  }
}

export async function claimExpense(
  declaratie,
  setSuccess,
  setError,
  setAlertCount
) {
  const token = window.sessionStorage.getItem("token");
  if (token) {
    fetch(`${BASE_URL}/expenseclaims`, {
      mode: "cors",
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(declaratie),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error(`HTTP status ${response.status}`);
        }
      })
      .then((result) => {
        console.log(result);
        console.log(declaratie);
        setSuccess(true);
        setAlertCount((prevCount) => prevCount + 1);
      })
      .catch((e) => {
        setError("Er ging iets mis. probeer later opnieuw");
        setAlertCount((prevCount) => prevCount + 1);
      });
  } else {
    setError("U bent niet geautoriseerd");
    setAlertCount((prevCount) => prevCount + 1);
  }
}
