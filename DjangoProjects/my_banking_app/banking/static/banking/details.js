document.addEventListener('DOMContentLoaded', function() {

    // Use buttons to toggle between views
    document.querySelector('#ch-acc').addEventListener('click', () => {
        document.querySelector('#checking-view').style.display = 'block';
        document.querySelector('#savings-view').style.display = 'none';
    })
    document.querySelector('#sav-acc').addEventListener('click', () => {
        document.querySelector('#checking-view').style.display = 'none';
        document.querySelector('#savings-view').style.display = 'block';
    })

    document.querySelector('#checking-view').style.display = 'block';
    document.querySelector('#savings-view').style.display = 'none';
})