document.addEventListener('DOMContentLoaded', function () {
    document.querySelector('#compose-form').addEventListener('submit', (event) => {
        event.preventDefault();
        const recipients = document.querySelector('#compose-recipients').value;
        const subject = document.querySelector('#compose-subject').value;
        const body = document.querySelector('#compose-body').value;

        fetch('/emails', {
            method: 'POST',
            body: JSON.stringify({
                recipients: recipients,
                subject: subject,
                body: body,
            })
        })
        .then(response => response.json())
        .then(result => {
            console.log(result);
            load_mailbox('sent');
        })

        .catch((error) => {
            console.log('Error:', error)
        })
    })
})