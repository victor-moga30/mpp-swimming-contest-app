const AUTH_BASE_URL = 'http://localhost:8080/api/auth';

async function handleResponse(response) {
    if (response.ok) {
        return await response.json();
    }

    let errorMessage = 'A aparut o eroare la autentificare.';

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

export async function login(username, password) {
    const response = await fetch(AUTH_BASE_URL + '/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    });

    return await handleResponse(response);
}