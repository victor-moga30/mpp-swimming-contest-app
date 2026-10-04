import { useEffect, useState } from 'react';

const emptyForm = {
    name: '',
    distance: '',
    minAge: '',
    maxAge: ''
};

function EventForm({ selectedEvent, onSave, onCancelEdit, loading }) {
    const [formData, setFormData] = useState(emptyForm);

    useEffect(() => {
        if (selectedEvent) {
            setFormData({
                name: selectedEvent.name,
                distance: String(selectedEvent.distance),
                minAge: String(selectedEvent.minAge),
                maxAge: String(selectedEvent.maxAge)
            });
        } else {
            setFormData(emptyForm);
        }
    }, [selectedEvent]);

    function handleChange(event) {
        const fieldName = event.target.name;
        const fieldValue = event.target.value;

        setFormData({
            ...formData,
            [fieldName]: fieldValue
        });
    }

    function handleSubmit(event) {
        event.preventDefault();

        const eventData = {
            name: formData.name.trim(),
            distance: Number(formData.distance),
            minAge: Number(formData.minAge),
            maxAge: Number(formData.maxAge)
        };

        onSave(eventData);
    }

    return (
        <section className="panel">
            <div className="panel-title-row">
                <div>
                    <h2>{selectedEvent ? 'Modifica proba' : 'Adauga proba'}</h2>
                    <p>
                        Formularul foloseste POST pentru adaugare si PUT pentru modificare.
                    </p>
                </div>
            </div>

            <form onSubmit={handleSubmit} className="event-form">
                <div className="form-group">
                    <label htmlFor="name">Nume proba</label>
                    <input
                        id="name"
                        name="name"
                        type="text"
                        value={formData.name}
                        onChange={handleChange}
                        placeholder="ex: 100m"
                    />
                </div>

                <div className="form-grid">
                    <div className="form-group">
                        <label htmlFor="distance">Distanta</label>
                        <input
                            id="distance"
                            name="distance"
                            type="number"
                            min="1"
                            value={formData.distance}
                            onChange={handleChange}
                            placeholder="ex: 100"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="minAge">Varsta minima</label>
                        <input
                            id="minAge"
                            name="minAge"
                            type="number"
                            min="0"
                            value={formData.minAge}
                            onChange={handleChange}
                            placeholder="ex: 6"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="maxAge">Varsta maxima</label>
                        <input
                            id="maxAge"
                            name="maxAge"
                            type="number"
                            min="0"
                            value={formData.maxAge}
                            onChange={handleChange}
                            placeholder="ex: 8"
                        />
                    </div>
                </div>

                <div className="button-row">
                    <button type="submit" className="primary-button" disabled={loading}>
                        {selectedEvent ? 'Salveaza modificarile' : 'Adauga proba'}
                    </button>

                    {selectedEvent && (
                        <button
                            type="button"
                            className="secondary-button"
                            onClick={onCancelEdit}
                            disabled={loading}
                        >
                            Renunta la editare
                        </button>
                    )}
                </div>
            </form>
        </section>
    );
}

export default EventForm;