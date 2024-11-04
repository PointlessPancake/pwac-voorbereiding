import { useEffect, useState } from 'react';

import Login from "./Login";
import SubmitClaim from './SubmitClaim';

function App() {
  const [loggedIn, setLoggedIn] = useState(null);

  useEffect(() => {
    const token = window.sessionStorage.getItem("token");
    if (token) {
      setLoggedIn(true);
    }
  }, [])

  return (
    <>
      {loggedIn ? <SubmitClaim setLoggedIn={setLoggedIn} /> : <Login setLoggedIn={setLoggedIn} />}
    </>
  );
}

export default App;