function EventFilter({ distanceFilter, onDistanceChange, onApplyFilter, onClearFilter }) {
    return (
        <section className="panel">
            <div className="panel-title-row">
                <div>
                    <h2>Filtrare probe</h2>
                    <p>Filtrarea foloseste endpoint-ul REST: GET /api/events?distance=...</p>
                </div>
            </div>

            <div className="filter-row">
                <div className="form-group">
                    <label htmlFor="distanceFilter">Distanta</label>
                    <input
                        id="distanceFilter"
                        type="number"
                        min="1"
                        value={distanceFilter}
                        onChange={(event) => onDistanceChange(event.target.value)}
                        placeholder="ex: 50, 100, 1000"
                    />
                </div>

                <div className="button-row">
                    <button type="button" className="primary-button" onClick={onApplyFilter}>
                        Filtreaza
                    </button>

                    <button type="button" className="secondary-button" onClick={onClearFilter}>
                        Afiseaza toate
                    </button>
                </div>
            </div>
        </section>
    );
}

export default EventFilter;