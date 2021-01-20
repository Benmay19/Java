document.addEventListener('DOMContentLoaded', function() {

    // Use buttons to toggle between views
    document.querySelector('#deposit').addEventListener('click', () => {
        document.querySelector('#deposit-view').style.display = 'block';
        document.querySelector('#withdrawl-view').style.display = 'none';
        document.querySelector('#transfer-view').style.display = 'none';
    })
    document.querySelector('#withdrawl').addEventListener('click', () => {
        document.querySelector('#deposit-view').style.display = 'none';
        document.querySelector('#withdrawl-view').style.display = 'block';
        document.querySelector('#transfer-view').style.display = 'none';
    })
    document.querySelector('#transfer').addEventListener('click', () => {
        document.querySelector('#deposit-view').style.display = 'none';
        document.querySelector('#withdrawl-view').style.display = 'none';
        document.querySelector('#transfer-view').style.display = 'block';
    })

    document.querySelector('#deposit-view').style.display = 'block';
    document.querySelector('#withdrawl-view').style.display = 'none';
    document.querySelector('#transfer-view').style.display = 'none';

})

function update_balance(ttype) {
    if (ttype == 'deposit') {
        const account = document.querySelector('#account').value;
        const amount = document.querySelector('#amount').value;
        const chBal = document.querySelector('#ch-bal').innerHTML;
        const savBal = document.querySelector('#sav-bal').innerHTML; 

        fetch('/deposit', {
            method: 'PUT',
            body: JSON.stringify({
                account: account,
                amount: amount,
            })
        })

        .then(() => {
            location.reload();
        })

        .catch((error) => {
            console.log('Error:', error);
        })
    } else if (ttype == 'withdrawl') {
        const account = document.querySelector('#waccount').value;
        const amount = document.querySelector('#wamount').value;
        const chBal = document.querySelector('#ch-w').innerHTML;
        const savBal = document.querySelector('#sav-w').innerHTML;

        fetch('/withdrawl', {
            method: 'PUT',
            body: JSON.stringify({
                account: account,
                amount: amount
            })
        })

        .then(() => {
            location.reload();
        })

        .catch((error) => {
            console.log('Error:', error);
        })
    } else {
        const account = document.querySelector('#taccount').value;
        const amount = document.querySelector('#tamount').value;
        const chBal = document.querySelector('#ch-t').innerHTML;
        const savBal = document.querySelector('#sav-t').innerHTML;

        fetch('/transfer', {
            method: 'PUT',
            body: JSON.stringify({
                account: account,
                amount: amount
            })
        })

        .then(() => {
            location.reload();
        })

        .catch((error) => {
            console.log('Error:', error);
        })
    }
}