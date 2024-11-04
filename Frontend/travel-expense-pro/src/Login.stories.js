import { expect, fn, userEvent, waitFor, within } from "@storybook/test";
import { http, HttpResponse } from "msw";

import Login from "./Login";

const LOGIN_URL = "*/users/login";

export default {
  component: Login,
  args: {
    setLoggedIn: fn(),
    user: "gebruiker",
    pwd: "wachtwoord",
  },
};

const loginAttempt = async ({ args, canvas, step }) => {
  // Arrange
  let usernameInput, passwordInput, loginButton;

  await step("Find form elements", async () => {
    usernameInput = canvas.getByRole("textbox", { name: "Gebruikersnaam" });
    passwordInput = canvas.getByLabelText("Wachtwoord");
    loginButton = canvas.getByRole("button");
  });

  // Act
  await step("Enter username and password", async () => {
    await userEvent.type(usernameInput, args.user);
    await userEvent.type(passwordInput, args.pwd);
  });
  await step("Submit form", async () => {
    await userEvent.click(loginButton);
  });

  return { canvas };
};

export const Successful = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify successful login", async () => {
      await waitFor(() => {
        expect(args.setLoggedIn).toHaveBeenCalled();
      });
    });
  },
  parameters: {
    msw: {
      handlers: [
        http.post(LOGIN_URL, () => {
          return HttpResponse.json({ accessToken: "" });
        }),
      ],
    },
  },
};

export const InvalidCredentialsError = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify unsuccessful login", async () => {
      await waitFor(() => {
        expect(canvas.getByRole("alert").textContent).toBe(
          "Ongeldige gebruikersnaam of wachtwoord. Probeer het opnieuw."
        );
      });
      expect(args.setLoggedIn).not.toHaveBeenCalled();
    });
  },
  parameters: {
    msw: {
      handlers: [
        http.post(LOGIN_URL, () => {
          return new HttpResponse(null, { status: 401 });
        }),
      ],
    },
  },
};

export const ServerError = {
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify unsuccessful login", async () => {
      await waitFor(() => {
        expect(canvas.getByRole("alert").textContent).toBe(
          "Er is iets misgegaan. Probeer het later nog eens."
        );
      });
      expect(args.setLoggedIn).not.toHaveBeenCalled();
    });
  },
  parameters: {
    msw: {
      handlers: [
        http.post(LOGIN_URL, () => {
          return new HttpResponse(null, { status: 500 });
        }),
      ],
    },
  },
};

export const requiredFields = {
  args: {
    user: " ",
    pwd: " ",
  },
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify required errors", async () => {
      await waitFor(() => {
        expect(canvas.getByText("Vul een gebruikersnaam in."));
        expect(canvas.getByText("Vul een wachtwoord in."));
      });
      expect(args.setLoggedIn).not.toHaveBeenCalled();
    });
  },
};

export const minLength = {
  args: {
    user: "12",
    pwd: "12345",
  },
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify minimal length errors", async () => {
      await waitFor(() => {
        expect(canvas.getByText("De gebruikersnaam moet minimaal 3 tekens bevatten."));
        expect(canvas.getByText("Het wachtwoord moet minimaal 6 tekens bevatten."));
      });
      expect(args.setLoggedIn).not.toHaveBeenCalled();
    });
  },
};

export const maxLength = {
  args: {
    user: "12345678901234567",
    pwd: "123456789012345678901",
  },
  play: async ({ args, canvasElement, step }) => {
    const canvas = within(canvasElement);
    await loginAttempt({ args, canvas, step });

    // Assert
    await step("Verify maximum length errors", async () => {
      await waitFor(() => {
        expect(canvas.getByText("De gebruikersnaam mag maximaal 16 tekens bevatten."));
        expect(canvas.getByText("Het wachtwoord mag maximaal 20 tekens bevatten."));
      });
      expect(args.setLoggedIn).not.toHaveBeenCalled();
    });
  },
};