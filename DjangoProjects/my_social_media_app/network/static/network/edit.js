function submitEdit(post_id) {
    const new_content = document.querySelector(`#edit-form${post_id}`).elements[`edit_content${post_id}`].value;

    fetch('/edit', {
        method: 'PUT',
        body: JSON.stringify({
            post_id: post_id,
            new_content: new_content
        })
    })
    .then(() => {
        document.querySelector(`#post_content${post_id}`).innerHTML = new_content;
        document.querySelector(`#show_content${post_id}`).style.display = 'block';
        document.querySelector(`#edit_content${post_id}`).style.display = 'none';
    })
    .catch((error) => {
        console.log('Error:', error);
    })
}