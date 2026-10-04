const BASE_URL = 'http://localhost:8080/api/events';

async function handleResponse(response) {
    if (response.ok) {
        if (response.status === 204) {
            return null;
        }

        return await response.json();
    }

    let errorMessage = 'A aparut o eroare la comunicarea cu serverul REST.';

    try {
        const errorBody = await response.json();

        if (errorBody && errorBody.message) {
            errorMessage = errorBody.message;
        }
    } catch (error) {
        errorMessage = response.status + ' ' + response.statusText;
    }

    throw new Error(errorMessage);
}

function authHeaders(token) {
    return {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': 'Bearer ' + token
    };
}

export async function getEvents(distance) {
    let url = BASE_URL;

    if (distance !== null && distance !== undefined && distance !== '') {
        url = BASE_URL + '?distance=' + encodeURIComponent(distance);
    }

    const response = await fetch(url, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });

    return await handleResponse(response);
}

export async function getEventById(id) {
    const response = await fetch(BASE_URL + '/' + id, {
        method: 'GET',
        headers: {
            'Accept': 'application/json'
        }
    });

    return await handleResponse(response);
}

export async function createEvent(eventData, token) {
    const response = await fetch(BASE_URL, {
        method: 'POST',
        headers: authHeaders(token),
        body: JSON.stringify(eventData)
    });

    return await handleResponse(response);
}

export async function updateEvent(id, eventData, token) {
    const response = await fetch(BASE_URL + '/' + id, {
        method: 'PUT',
        headers: authHeaders(token),
        body: JSON.stringify(eventData)
    });

    return await handleResponse(response);
}

export async function deleteEvent(id, token) {
    const response = await fetch(BASE_URL + '/' + id, {
        method: 'DELETE',
        headers: {
            'Authorization': 'Bearer ' + token
        }
    });

    return await handleResponse(response);
}