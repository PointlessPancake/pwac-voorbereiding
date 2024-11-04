import { initialize, mswLoader } from "msw-storybook-addon";

// Initialize MSW
initialize();

/** @type { import('@storybook/react').Preview } */
const preview = {
  parameters: {
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
      disableSaveFromUI: true, // Prevent saving args changes
    },
  },
  loaders: [mswLoader], // Add MSW loader for all stories
};

export default preview;

