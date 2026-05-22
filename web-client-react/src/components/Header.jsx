function Header({ currentUser }) {
    return (
        <header className="app-header">
            <div>
                <p className="eyebrow">Tema Proiect 8</p>
                <h1>JWT + WebSocket Observer</h1>
                <p className="subtitle">
                    Aplicatie React care consuma serviciile REST pentru probele concursului.
                    Lista este publica, iar operatiile CRUD si notificarile WebSocket sunt
                    disponibile doar pentru clientii autentificati.
                </p>
            </div>

            <div className="header-status-card">
                {currentUser ? (
                    <>
                        <span className="header-status-label">Conectat ca</span>
                        <strong>{currentUser.username}</strong>
                        <span>WebSocket activ</span>
                    </>
                ) : (
                    <>
                        <span className="header-status-label">Mod curent</span>
                        <strong>Vizitator</strong>
                        <span>Doar citire</span>
                    </>
                )}
            </div>
        </header>
    );
}

export default Header;