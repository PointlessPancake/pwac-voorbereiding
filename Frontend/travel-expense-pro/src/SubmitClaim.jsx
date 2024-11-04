import { useState } from "react";
import { claimExpense } from "./api-functions";
import Logout from "./Logout";
import "./SubmitClaim.css";

export default function SubmitClaim({ setLoggedIn }) {
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");
  const [validationErrors, setValidationErrors] = useState({});
  const [alertCount, setAlertCount] = useState(0);

  const validateClaim = (claim) => {
    const errors = {};
    const titlePatternMin = /^.{3,}$/;
    const descriptionPatternMin = /^.{3,}$/;
    const titlePatternMax = /^.{1,10}$/;
    const descriptionPatternMax = /^.{1,50}$/;
    const moneyPattern = /^\d+(\.\d{1,2})?$/;

    // Title validation
    if (!claim.title) {
      errors.title = "Vul een titel in";
    } else if (!titlePatternMin.test(claim.title)) {
      errors.title = "De titel moet minimaal 3 tekens lang zijn";
    } else if (!titlePatternMax.test(claim.title)) {
      errors.title = "De titel mag uit maximaal 10 tekens bestaan";
    }

    // Amount validation
    if (!claim.amount) {
      errors.amount = "Vul een bedrag in";
    } else if (claim.amount > 10000) {
      errors.amount = "Bedrag is te hoog";
    } else if (claim.amount < 0) {
      errors.amount = "Bedrag is te laag";
    } else if (!moneyPattern.test(claim.amount)) {
      errors.amount = "Bedrag is geen geldig geldbedrag";
    }

    // Description validation
    if (!claim.description) {
      errors.description = "Vul een beschrijving in";
    } else if (!descriptionPatternMin.test(claim.description)) {
      errors.description = "De beschrijving moet minimaal 3 tekens lang zijn";
    } else if (!descriptionPatternMax.test(claim.description)) {
      errors.description = "De beschrijving mag uit maximaal 50 tekens bestaan";
    }

    setValidationErrors(errors);
    return errors;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const claim = {
      title: e.target.title.value,
      amount: e.target.amount.value,
      description: e.target.description.value,
    };
    const validateClaimErrors = validateClaim(claim);

    setSuccess(false);
    if (Object.keys(validateClaimErrors).length === 0) {
      claimExpense(claim, setSuccess, setError, setAlertCount);
    }
  };

  return (
    <>
      <Logout setLoggedIn={setLoggedIn} />
      <h1>TravelExpensePro</h1>
      <p
        role="alert"
        key={alertCount}
        className={success ? "success" : "error"}
      >
        {success ? "Declaratie is ingediend" : error}
      </p>
      <form onSubmit={handleSubmit}>
        <label htmlFor="title">Titel: </label>
        <input type="text" name="title" id="title" />
        {validationErrors.title && (
          <p className="error">{validationErrors.title}</p>
        )}

        <label htmlFor="amount">Bedrag: </label>
        <input type="number" name="amount" id="amount" step="0.01" />
        {validationErrors.amount && (
          <p className="error">{validationErrors.amount}</p>
        )}
        <label htmlFor="description">Beschrijving: </label>
        <textarea name="description" id="description"></textarea>
        {validationErrors.description && (
          <p className="error">{validationErrors.description}</p>
        )}

        <button type="submit">Indienen</button>
      </form>
    </>
  );
}