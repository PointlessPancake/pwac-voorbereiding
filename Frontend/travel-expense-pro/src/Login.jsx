import { useEffect, useRef, useState } from "react";
import { getToken } from "./api-functions";
import "./Login.css";

function Login({ setLoggedIn }) {
  const usernameRef = useRef(null);
  const passwordRef = useRef(null);
  
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  })
  const [errors, setErrors] = useState({});
  const [submitError, setSubmitError] = useState({});

  // Focus first field with error
  useEffect(() => {
    if (errors.username) {
      usernameRef.current.focus();
    } else if (errors.password) {
      passwordRef.current.focus();
    }
  }, [errors])

  const validateForm= () => {
    const newErrors = {};

    // Username
    if (!formData.username) {
      newErrors.username = "Vul een gebruikersnaam in."
    } else if (formData.username.length < 3) {
      newErrors.username = "De gebruikersnaam moet minimaal 3 tekens bevatten.";
    } else if (formData.username.length > 16) {
      newErrors.username = "De gebruikersnaam mag maximaal 16 tekens bevatten.";
    }

    // Password
    if (!formData.password) {
      newErrors.password = "Vul een wachtwoord in."
    } else if (formData.password.length < 6) {
      newErrors.password = "Het wachtwoord moet minimaal 6 tekens bevatten.";
    } else if (formData.password.length > 20) {
      newErrors.password = "Het wachtwoord mag maximaal 20 tekens bevatten.";
    }
    setErrors(newErrors);
    return newErrors;
  }

  const handleLogin = async (e) => {
    e.preventDefault();
    const validationErrors = validateForm();

    if (!Object.keys(validationErrors).length) {
      try {
        const accessToken = await getToken(formData.username, formData.password);
        window.sessionStorage.setItem("token", accessToken);
        setLoggedIn(true);
      } catch (e) {
        setSubmitError(prev => ({
          message: e.message,
          count: (prev.count || 0) + 1
        }))
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value.trim()
    }));
  }

  return (
    <>
      <h1>TravelExpensePro</h1>
      {submitError && <p role="alert" className="error" key={submitError.count}>{submitError.message}</p>}
      <form onSubmit={handleLogin}>
        <label htmlFor="username">Gebruikersnaam</label>
        <input
          ref={usernameRef}
          type="text"
          id="username"
          name="username"
          value={formData.username}
          onChange={handleInputChange}
          {...(errors.username && {
            "aria-invalid": "true",
            "aria-describedby": "error-username"
          })}
        />
        {errors.username && <p className="error" id="error-username">{errors.username}</p>}
        <label htmlFor="password">Wachtwoord</label>
        <input
          ref={passwordRef}
          type="password"
          id="password"
          name="password"
          value={formData.password}
          onChange={handleInputChange}
          {...(errors.password && {
            "aria-invalid": "true",
            "aria-describedby": "error-password"
          })}
        />
        {errors.password && <p className="error" id="error-password">{errors.password}</p>}
        <button type="submit">Inloggen</button>
      </form>
    </>
  );
}

export default Login;
