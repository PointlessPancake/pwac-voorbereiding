export default function Logout({setLoggedIn}) {
  const logout = () => {
    window.sessionStorage.removeItem("token");
    setLoggedIn(false);
  };

  return (
    <button type="button" className = "logoutButton"onClick={logout}>
      uitloggen
    </button>
  );
}