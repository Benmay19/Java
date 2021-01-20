document.addEventListener('DOMContentLoaded', function() {
    document.querySelector('#post-form').onsubmit = () => {
        const content = document.querySelector('#content').value;

        fetch('/compose', {
            method: 'POST',
            body: JSON.stringify({
                content: content
            })
        })
        .then(response => response.json())
        .then(result => {
            console.log(result);
            location.reload();
        })

        .catch((error) => {
            console.log('Error:', error);
        })
    }
});