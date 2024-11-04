import SubmitClaim from "./SubmitClaim";
import { expect, userEvent, within, waitFor } from "@storybook/test";
import { http, HttpResponse } from "msw";

const SUBMITCLAIM_URL = "*/expenseclaims";

export default {
  component: SubmitClaim,
  args: {
    title: "titel",
    amount: "400",
    description: "beschrijving",
  },
};

const submitAttempt = async ({ args, canvas, step }) => {
  // Arrange
  let TitleField, AmountField, DescriptionField, SubmitButton;

  await step("Find form elements", async () => {
    TitleField = canvas.getByLabelText("Titel:");
    AmountField = canvas.getByLabelText("Bedrag:");
    DescriptionField = canvas.getByLabelText("Beschrijving:");
    SubmitButton = canvas.getByRole("button", { name: "Indienen" });
  });

  // Act
  await step("enter data", async () => {
    await userEvent.type(TitleField, args.title);
    await userEvent.type(AmountField, args.amount);
    await userEvent.type(DescriptionField, args.description);
  });
  await step("Submit form", async () => {
    await userEvent.click(SubmitButton);
  });

  return { canvas };
};

export const SubmitSucces = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    // Arrange
    window.sessionStorage.setItem(
      "token",
      "JWT token met gebruikersnaam en wachtwoord"
    );
    await submitAttempt({ args, canvas, step });

    // Assert
    await step("submitted successfully", async () => {
      await waitFor(() => {
        expect(canvas.getByText("Declaratie is ingediend").toBeInDocument);
        window.sessionStorage.clear();
      });
    });
  },
  parameters: {
    msw: {
      handlers: [
        http.post(SUBMITCLAIM_URL, () => {
          return new HttpResponse(null, { status: 200 });
        }),
      ],
    },
  },
};

export const UnsuccessfulSubmit = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    // Arrange
    window.sessionStorage.setItem(
      "token",
      "JWT token met gebruikersnaam en wachtwoord"
    );
    await submitAttempt({ args, canvas, step });

    // Assert
    await step("submitted wrong", async () => {
      await waitFor(() => {
        expect(
          canvas.getByText("Er ging iets mis. probeer later opnieuw")
            .toBeInDocument
        );
        window.sessionStorage.clear();
      });
    });
  },
  parameters: {
    msw: {
      handlers: [
        http.post(SUBMITCLAIM_URL, () => {
          return new HttpResponse(null, { status: 500 });
        }),
      ],
    },
  },
};

export const TooMuch = {
  args: {
    title: "Titel die te lang is",
    amount: "20000",
    description: "Beschrijving is top is veels te lang en dat geeft problemen",
  },
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    // Arrange
    await submitAttempt({ args, canvas, step });

    // Assert
    await step("submitted wrong", async () => {
      await waitFor(() => {
        expect(
          canvas.getByText("De titel mag uit maximaal 10 tekens bestaan")
            .toBeInDocument
        );
        expect(canvas.getByText("Bedrag is te hoog").toBeInDocument);
        expect(
          canvas.getByText("De beschrijving mag uit maximaal 50 tekens bestaan")
            .toBeInDocument
        );
      });
    });
  },
};

export const NotEnough = {
  args: {
    title: "ti",
    amount: "-400",
    description: "b",
  },
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    //Arrange
    await submitAttempt({ args, canvas, step });

    // Assert
    await step("submitted wrong", async () => {
      await waitFor(() => {
        expect(
          canvas.getByText("De titel moet minimaal 3 tekens lang zijn")
            .toBeInDocument
        );
        expect(canvas.getByText("Bedrag is te laag").toBeInDocument);
        expect(
          canvas.getByText("De beschrijving moet minimaal 3 tekens lang zijn")
            .toBeInDocument
        );
      });
    });
  },
};

export const EmptyForm = {
  play: async ({ canvasElement, step }) => {
    const canvas = within(canvasElement);

    // Arrange
    const SubmitButton = canvas.getByRole("button", { name: "Indienen" });

    await step("submit form", async () => {
      await userEvent.click(SubmitButton);
    });

    // Assert
    await step("submitted wrong", async () => {
      await waitFor(() => {
        expect(canvas.getByText("Vul een titel in").toBeInDocument);
        expect(canvas.getByText("Vul een bedrag in").toBeInDocument);
        expect(canvas.getByText("Vul een beschrijving in").toBeInDocument);
      });
    });
  },
};

export const Unauthorized = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    //Arrange
    await submitAttempt({ args, canvas, step });

    // Assert
    await step("not authorized", async () => {
      await waitFor(() => {
        expect(canvas.getByText("U bent niet geautoriseerd").toBeInDocument);
      });
    });
  },
};