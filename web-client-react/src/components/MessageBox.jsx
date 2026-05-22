function MessageBox({ type, message, onClose }) {
    if (!message) {
        return null;
    }

    let title = 'Mesaj';

    if (type === 'success') {
        title = 'Succes';
    }

    if (type === 'error') {
        title = 'Eroare';
    }

    if (type === 'websocket') {
        title = 'Notificare WebSocket';
    }

    return (
        <div className="toast-container">
            <div className={'message-box ' + type}>
                <div className="message-content">
                    <strong>{title}</strong>
                    <span>{message}</span>
                </div>

                <button type="button" onClick={onClose}>
                    x
                </button>
            </div>
        </div>
    );
}

export default MessageBox;