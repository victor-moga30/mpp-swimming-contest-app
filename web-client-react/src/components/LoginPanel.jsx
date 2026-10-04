import { useEffect, useState } from 'react';

function LoginPanel({ currentUser, onLogin, onLogout, loading }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    useEffect(() => {
        if (currentUser) {
            setUsername('');
            setPassword('');
        }
    }, [currentUser]);

    function handleSubmit(event) {
        event.preventDefault();

        onLogin(username.trim(), password);

        setPassword('');
    }

    if (currentUser) {
        return (
            <section className="panel auth-panel authenticated-panel">
                <div className="panel-title-row">
                    <div>
                        <h2>Client autentificat</h2>
                        <p>
                            Esti autentificat ca <strong>{currentUser.username}</strong>.
                            Operatiile CRUD si conexiunea WebSocket sunt active.
                        </p>
                    </div>

                    <span className="status-pill status-authenticated">
                        Autentificat
                    </span>
                </div>

                <div className="auth-info-box">
                    <p>
                        Acest client poate adauga, modifica si sterge probe.
                        De asemenea, primeste notificari WebSocket cand alt client
                        autentificat modifica lista.
                    </p>
                </div>

                <button
                    type="button"
                    className="secondary-button"
                    onClick={() => {
                        setUsername('');
                        setPassword('');
                        onLogout();
                    }}
                    disabled={loading}
                >
                    Logout
                </button>
            </section>
        );
    }

    return (
        <section className="panel auth-panel">
            <div className="panel-title-row">
                <div>
                    <h2>Autentificare</h2>
                    <p>
                        Poti folosi aplicatia ca vizitator pentru lista, cautare si filtrare.
                        Pentru CRUD si WebSocket trebuie sa te autentifici.
                    </p>
                </div>

                <span className="status-pill status-guest">
                    Vizitator
                </span>
            </div>

            <div className="visitor-box">
                <h3>Mod vizitator</h3>
                <p>
                    Fara login poti vedea lista probelor, poti cauta dupa ID si poti filtra
                    dupa distanta. Nu poti adauga, modifica sau sterge.
                </p>
            </div>

            <form onSubmit={handleSubmit} className="event-form">
                <div className="form-group">
                    <label htmlFor="username">Username</label>
                    <input
                        id="username"
                        type="text"
                        value={username}
                        onChange={(event) => setUsername(event.target.value)}
                        placeholder="ex: oficiu1"
                        autoComplete="username"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">Parola</label>
                    <input
                        id="password"
                        type="password"
                        value={password}
                        onChange={(event) => setPassword(event.target.value)}
                        placeholder="parola"
                        autoComplete="current-password"
                    />
                </div>

                <button type="submit" className="primary-button full-width-button" disabled={loading}>
                    Login
                </button>
            </form>
        </section>
    );
}

export default LoginPanel;