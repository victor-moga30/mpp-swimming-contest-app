import { useEffect, useRef, useState } from 'react';
import Header from './components/Header.jsx';
import EventFilter from './components/EventFilter.jsx';
import EventForm from './components/EventForm.jsx';
import EventTable from './components/EventTable.jsx';
import MessageBox from './components/MessageBox.jsx';
import EventSearchById from './components/EventSearchById.jsx';
import LoginPanel from './components/LoginPanel.jsx';
import { login } from './api/authApi.js';
import {
    createEvent,
    deleteEvent,
    getEventById,
    getEvents,
    updateEvent
} from './api/eventsApi.js';

function App() {
    const [events, setEvents] = useState([]);
    const [distanceFilter, setDistanceFilter] = useState('');
    const [activeDistanceFilter, setActiveDistanceFilter] = useState('');
    const [searchId, setSearchId] = useState('');
    const [foundEvent, setFoundEvent] = useState(null);
    const [selectedEvent, setSelectedEvent] = useState(null);
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('success');

    const [token, setToken] = useState(localStorage.getItem('jwtToken'));
    const [currentUser, setCurrentUser] = useState(() => {
        const savedUser = localStorage.getItem('currentUser');

        if (!savedUser) {
            return null;
        }

        try {
            return JSON.parse(savedUser);
        } catch (error) {
            localStorage.removeItem('currentUser');
            localStorage.removeItem('jwtToken');
            return null;
        }
    });

    const webSocketRef = useRef(null);
    const messageTimerRef = useRef(null);

    useEffect(() => {
        loadEvents(activeDistanceFilter);
    }, [activeDistanceFilter]);

    useEffect(() => {
        if (!token || !currentUser) {
            closeWebSocket();
            return;
        }

        const socket = new WebSocket(
            'ws://localhost:8080/ws/events?token=' + encodeURIComponent(token)
        );

        socket.onopen = function () {
            console.log('WebSocket autentificat deschis.');
        };

        socket.onmessage = function (event) {
            const notification = JSON.parse(event.data);

            console.log('Notificare WebSocket primita:', notification);

            if (notification.changedBy !== currentUser.username) {
                showWebSocketNotification(
                    buildNotificationMessage(notification)
                );

                loadEvents(activeDistanceFilter);
            }
        };

        socket.onerror = function () {
            console.log('Eroare WebSocket.');
        };

        socket.onclose = function () {
            console.log('WebSocket inchis.');
        };

        webSocketRef.current = socket;

        return function cleanup() {
            socket.close();
        };
    }, [token, currentUser, activeDistanceFilter]);

    function buildNotificationMessage(notification) {
        if (notification.type === 'CREATED') {
            return 'Alt client autentificat a adaugat o proba. Utilizator: ' +
                notification.changedBy + '. Lista a fost actualizata automat.';
        }

        if (notification.type === 'UPDATED') {
            return 'Alt client autentificat a modificat proba cu id-ul ' +
                notification.eventId + '. Utilizator: ' +
                notification.changedBy + '. Lista a fost actualizata automat.';
        }

        if (notification.type === 'DELETED') {
            return 'Alt client autentificat a sters proba cu id-ul ' +
                notification.eventId + '. Utilizator: ' +
                notification.changedBy + '. Lista a fost actualizata automat.';
        }

        return 'Lista a fost actualizata de alt client autentificat.';
    }

    function closeWebSocket() {
        if (webSocketRef.current) {
            webSocketRef.current.close();
            webSocketRef.current = null;
        }
    }

    async function loadEvents(distance) {
        setLoading(true);

        try {
            const data = await getEvents(distance);
            setEvents(data);
        } catch (error) {
            setEvents([]);

            if (distance !== null && distance !== undefined && distance !== '') {
                showError('Nu exista probe pentru distanta ' + distance + '.');
            } else {
                showError(error.message);
            }
        } finally {
            setLoading(false);
        }
    }

    function showMessage(type, text, autoClose) {
        if (messageTimerRef.current) {
            clearTimeout(messageTimerRef.current);
        }

        setMessageType(type);
        setMessage(text);

        if (autoClose) {
            messageTimerRef.current = setTimeout(function () {
                setMessage('');
            }, 6000);
        }
    }

    function showSuccess(text) {
        showMessage('success', text, true);
    }

    function showError(text) {
        showMessage('error', text, false);
    }

    function showWebSocketNotification(text) {
        showMessage('websocket', text, true);
    }

    function clearMessage() {
        if (messageTimerRef.current) {
            clearTimeout(messageTimerRef.current);
        }

        setMessage('');
    }

    function validateEventData(eventData) {
        if (!eventData.name) {
            throw new Error('Numele probei nu poate fi gol.');
        }

        if (!Number.isInteger(eventData.distance) || eventData.distance <= 0) {
            throw new Error('Distanta trebuie sa fie un numar intreg pozitiv.');
        }

        if (!Number.isInteger(eventData.minAge) || !Number.isInteger(eventData.maxAge)) {
            throw new Error('Varstele trebuie sa fie numere intregi.');
        }

        if (eventData.minAge < 0 || eventData.maxAge < 0) {
            throw new Error('Varstele nu pot fi negative.');
        }

        if (eventData.minAge > eventData.maxAge) {
            throw new Error('Varsta minima nu poate fi mai mare decat varsta maxima.');
        }

        if (eventData.minAge < 6 || eventData.maxAge > 15) {
            throw new Error('Pentru concursul acesta varsta trebuie sa fie intre 6 si 15 ani.');
        }
    }

    async function handleLogin(username, password) {
        if (!username) {
            showError('Username-ul nu poate fi gol.');
            return;
        }

        if (!password) {
            showError('Parola nu poate fi goala.');
            return;
        }

        setLoading(true);

        try {
            const response = await login(username, password);

            const user = {
                username: response.username,
                office: response.office
            };

            localStorage.setItem('jwtToken', response.token);
            localStorage.setItem('currentUser', JSON.stringify(user));

            setToken(response.token);
            setCurrentUser(user);

            showSuccess('Autentificare reusita. CRUD si WebSocket sunt active.');
        } catch (error) {
            localStorage.removeItem('jwtToken');
            localStorage.removeItem('currentUser');

            setToken(null);
            setCurrentUser(null);

            showError(error.message);
        } finally {
            setLoading(false);
        }
    }

    function handleLogout() {
        localStorage.removeItem('jwtToken');
        localStorage.removeItem('currentUser');

        setToken(null);
        setCurrentUser(null);
        setSelectedEvent(null);
        closeWebSocket();

        showSuccess('Te-ai delogat. Aplicatia a revenit in modul vizitator.');
    }

    async function handleSave(eventData) {
        if (!token) {
            showError('Trebuie sa fii autentificat pentru adaugare sau modificare.');
            return;
        }

        try {
            validateEventData(eventData);
            setLoading(true);

            if (selectedEvent) {
                await updateEvent(selectedEvent.id, eventData, token);
                showSuccess('Proba a fost modificata cu succes.');
            } else {
                await createEvent(eventData, token);
                showSuccess('Proba a fost adaugata cu succes.');
            }

            setSelectedEvent(null);
            setFoundEvent(null);
            await loadEvents(activeDistanceFilter);
        } catch (error) {
            showError(error.message);
        } finally {
            setLoading(false);
        }
    }

    function handleEdit(event) {
        if (!token) {
            showError('Trebuie sa fii autentificat pentru modificare.');
            return;
        }

        setSelectedEvent(event);
        clearMessage();

        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    }

    async function handleDelete(event) {
        if (!token) {
            showError('Trebuie sa fii autentificat pentru stergere.');
            return;
        }

        const confirmed = window.confirm(
            'Sigur vrei sa stergi proba "' + event.name + '" cu id-ul ' + event.id + '?'
        );

        if (!confirmed) {
            return;
        }

        setLoading(true);

        try {
            await deleteEvent(event.id, token);

            if (selectedEvent && selectedEvent.id === event.id) {
                setSelectedEvent(null);
            }

            if (foundEvent && foundEvent.id === event.id) {
                setFoundEvent(null);
            }

            showSuccess('Proba a fost stearsa cu succes.');
            await loadEvents(activeDistanceFilter);
        } catch (error) {
            showError(error.message);
            await loadEvents(activeDistanceFilter);
        } finally {
            setLoading(false);
        }
    }

    function handleApplyFilter() {
        clearMessage();

        if (distanceFilter !== '' && Number(distanceFilter) <= 0) {
            showError('Distanta pentru filtrare trebuie sa fie pozitiva.');
            return;
        }

        setActiveDistanceFilter(distanceFilter);
    }

    function handleClearFilter() {
        setDistanceFilter('');
        setActiveDistanceFilter('');
        clearMessage();
    }

    async function handleSearchById() {
        clearMessage();
        setFoundEvent(null);

        if (searchId === '') {
            showError('Introdu un ID pentru cautare.');
            return;
        }

        const numericId = Number(searchId);

        if (!Number.isInteger(numericId) || numericId <= 0) {
            showError('ID-ul trebuie sa fie un numar intreg pozitiv.');
            return;
        }

        setLoading(true);

        try {
            const event = await getEventById(numericId);
            setFoundEvent(event);
            showSuccess('Proba cu ID-ul ' + numericId + ' a fost gasita.');
        } catch (error) {
            showError('Nu exista nicio proba cu ID-ul ' + numericId + '.');
        } finally {
            setLoading(false);
        }
    }

    function handleClearSearch() {
        setSearchId('');
        setFoundEvent(null);
        clearMessage();
    }

    function handleCancelEdit() {
        setSelectedEvent(null);
        clearMessage();
    }

    return (
        <div className="app-shell">
            <Header currentUser={currentUser} />

            <MessageBox
                type={messageType}
                message={message}
                onClose={clearMessage}
            />

            <main className="main-layout">
                <div className="left-column">
                    <LoginPanel
                        currentUser={currentUser}
                        onLogin={handleLogin}
                        onLogout={handleLogout}
                        loading={loading}
                    />

                    {currentUser && (
                        <EventForm
                            selectedEvent={selectedEvent}
                            onSave={handleSave}
                            onCancelEdit={handleCancelEdit}
                            loading={loading}
                        />
                    )}

                    {!currentUser && (
                        <section className="panel public-mode-panel">
                            <h2>Acces public</h2>
                            <p>
                                Esti in modul vizitator. Ai acces la operatiile publice:
                                lista probelor, filtrare si cautare dupa ID.
                            </p>
                            <p>
                                Pentru POST, PUT, DELETE si WebSocket trebuie sa faci login.
                            </p>
                        </section>
                    )}

                    <EventSearchById
                        searchId={searchId}
                        onSearchIdChange={setSearchId}
                        onSearchById={handleSearchById}
                        onClearSearch={handleClearSearch}
                        foundEvent={foundEvent}
                    />

                    <EventFilter
                        distanceFilter={distanceFilter}
                        onDistanceChange={setDistanceFilter}
                        onApplyFilter={handleApplyFilter}
                        onClearFilter={handleClearFilter}
                    />
                </div>

                <div className="right-column">
                    <EventTable
                        events={events}
                        onEdit={handleEdit}
                        onDelete={handleDelete}
                        loading={loading}
                        authenticated={currentUser !== null}
                    />
                </div>
            </main>
        </div>
    );
}

export default App;