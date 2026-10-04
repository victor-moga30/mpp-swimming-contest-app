function EventTable({ events, onEdit, onDelete, loading, authenticated }) {
    return (
        <section className="panel">
            <div className="panel-title-row">
                <div>
                    <h2>Lista probelor</h2>
                    <p>
                        GET este public. Actiunile de modificare si stergere apar doar
                        pentru clientii autentificati.
                    </p>
                </div>

                <span className="counter">
                    {events.length} probe
                </span>
            </div>

            {loading && (
                <p className="loading-text">Se incarca datele...</p>
            )}

            {!loading && events.length === 0 && (
                <p className="empty-text">Nu exista probe pentru filtrul curent.</p>
            )}

            {!loading && events.length > 0 && (
                <div className="table-wrapper">
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nume</th>
                            <th>Distanta</th>
                            <th>Varsta minima</th>
                            <th>Varsta maxima</th>
                            {authenticated && <th>Actiuni</th>}
                        </tr>
                        </thead>

                        <tbody>
                        {events.map((event) => (
                            <tr key={event.id}>
                                <td>{event.id}</td>
                                <td>{event.name}</td>
                                <td>{event.distance} m</td>
                                <td>{event.minAge}</td>
                                <td>{event.maxAge}</td>

                                {authenticated && (
                                    <td>
                                        <div className="table-actions">
                                            <button
                                                type="button"
                                                className="small-secondary-button"
                                                onClick={() => onEdit(event)}
                                            >
                                                Modifica
                                            </button>

                                            <button
                                                type="button"
                                                className="small-danger-button"
                                                onClick={() => onDelete(event)}
                                            >
                                                Sterge
                                            </button>
                                        </div>
                                    </td>
                                )}
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            )}
        </section>
    );
}

export default EventTable;