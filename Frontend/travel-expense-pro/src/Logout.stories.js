import { expect, fn, userEvent, within } from "@storybook/test";

import Logout from "./Logout";

export default {
  component: Logout,
  args: {
    setLoggedIn: fn(),
  },
};

export const LogoutButton = {
  play: async ({ canvasElement, args }) => {
    const canvas = within(canvasElement);
    // Arrange
    const logoutButton = canvas.getByRole("button");

    //Act
    await userEvent.click(logoutButton);

    //Assert
    await expect(args.setLoggedIn).toHaveBeenCalled();
  },
};
