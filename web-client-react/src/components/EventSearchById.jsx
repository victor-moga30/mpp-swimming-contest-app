function EventSearchById({
                             searchId,
                             onSearchIdChange,
                             onSearchById,
                             onClearSearch,
                             foundEvent
                         }) {
    return (
        <section className="panel">
            <div className="panel-title-row">
                <div>
                    <h2>Cautare dupa ID</h2>
                    <p>Cautarea foloseste endpoint-ul REST: GET /api/events/id</p>
                </div>
            </div>

            <div className="filter-row">
                <div className="form-group">
                    <label htmlFor="searchId">ID proba</label>
                    <input
                        id="searchId"
                        type="number"
                        min="1"
                        value={searchId}
                        onChange={(event) => onSearchIdChange(event.target.value)}
                        placeholder="ex: 1"
                    />
                </div>

                <div className="button-row">
                    <button type="button" className="primary-button" onClick={onSearchById}>
                        Cauta dupa ID
                    </button>

                    <button type="button" className="secondary-button" onClick={onClearSearch}>
                        Sterge cautarea
                    </button>
                </div>
            </div>

            {foundEvent && (
                <div className="found-event-card">
                    <h3>Rezultat cautare</h3>

                    <div className="found-event-grid">
                        <span>ID:</span>
                        <strong>{foundEvent.id}</strong>

                        <span>Nume:</span>
                        <strong>{foundEvent.name}</strong>

                        <span>Distanta:</span>
                        <strong>{foundEvent.distance} m</strong>

                        <span>Varsta minima:</span>
                        <strong>{foundEvent.minAge}</strong>

                        <span>Varsta maxima:</span>
                        <strong>{foundEvent.maxAge}</strong>
                    </div>
                </div>
            )}
        </section>
    );
}

export default EventSearchById;