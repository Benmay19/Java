function add() {
    const account = document.querySelector('#raccount').value;
    const rtype = document.querySelector('#rtype').value;
    const name = document.querySelector('#add-name').value;
    const amount = document.querySelector('#add-amount').value;
    const day = document.querySelector('#day').value;
    
    fetch('/add', {
        method: "POST",
        body: JSON.stringify({
            account: account,
            rtype: rtype,
            name: name,
            amount: amount,
            day: day
        })
    })
    .then(() => {
        location.reload();
    })
    .catch((error) => {
        console.log("Error:", error);
    })
}