function toggle(post_id) {
    const count = document.querySelector(`#count${post_id}`).innerHTML;
    
    fetch('/toggle', {
        method: 'PUT',
        body: JSON.stringify({
            post_id: post_id
        })
    })
    .then(() => {
        if (document.querySelector(`#heart${post_id}`).className == 'fa fa-heart-o') {
            document.querySelector(`#heart${post_id}`).className = ('fa fa-heart');
            document.querySelector(`#count${post_id}`).innerHTML = parseInt(count) + 1;
        } else {
            document.querySelector(`#heart${post_id}`).className = ('fa fa-heart-o');
            document.querySelector(`#count${post_id}`).innerHTML = parseInt(count) - 1;
        }
    })
    .catch((error) => {
        console.log('Error:', error);
    })
}

function follow(user_to_follow) {
    const follower_count = document.querySelector(`#followers${user_to_follow}`).innerHTML;
    console.log(user_to_follow);
    fetch('/follow', {
        method: 'PUT',
        body: JSON.stringify({
            user_to_follow: user_to_follow
        })
    })
    .then(() => {
        if (document.querySelector(`#follow${user_to_follow}`).className == 'unfollow') {
            document.querySelector(`#followers${user_to_follow}`).innerHTML = parseInt(follower_count) + 1;
        }
        if (document.querySelector(`#follow${user_to_follow}`).className == 'follow') {
            document.querySelector(`#followers${user_to_follow}`).innerHTML = parseInt(follower_count) - 1;
        }
    })
    .catch((error) => {
        console.log('Error:', error);
    })
    location.reload();
}

function edit(post_id) {
    document.querySelector(`#show_content${post_id}`).style.display = 'none';
    document.querySelector(`#edit_content${post_id}`).style.display = 'block';
    document.querySelector(`#edit${post_id}`).style.display = 'none';
}