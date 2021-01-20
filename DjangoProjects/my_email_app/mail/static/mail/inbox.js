document.addEventListener('DOMContentLoaded', function() {

  // Use buttons to toggle between views
  document.querySelector('#inbox').addEventListener('click', () => load_mailbox('inbox'));
  document.querySelector('#sent').addEventListener('click', () => load_mailbox('sent'));
  document.querySelector('#archived').addEventListener('click', () => load_mailbox('archive'));
  document.querySelector('#compose').addEventListener('click', compose_email);

  // By default, load the inbox
  load_mailbox('inbox');
});

function compose_email() {

  // Show compose view and hide other views
  document.querySelector('#emails-view').style.display = 'none';
  document.querySelector('#display-email').style.display = 'none';
  document.querySelector('#compose-view').style.display = 'block';

  // Clear out composition fields
  document.querySelector('#compose-recipients').value = '';
  document.querySelector('#compose-subject').value = '';
  document.querySelector('#compose-body').value = '';
}

function load_mailbox(mailbox) {
  
  // Show the mailbox and hide other views
  document.querySelector('#emails-view').style.display = 'block';
  document.querySelector('#display-email').style.display = 'none';
  document.querySelector('#compose-view').style.display = 'none';

  // Show the mailbox name
  document.querySelector('#emails-view').innerHTML = 
  `<h3>${mailbox.charAt(0).toUpperCase() + mailbox.slice(1)}</h3>
  <table class="table table-hover"></table>`;

  // Show emails in mailbox
  fetch(`/emails/${mailbox}`)
    .then(response => response.json())
    .then(emails => {
      console.log(emails);

      emails.forEach(function(email) {
        const tr = document.createElement('tr');
        if (email.read) {
          tr.className = 'table-secondary';
        } else {
          tr.style.fontWeight = 'bold';
        }
        if (mailbox == 'sent') {
          tr.innerHTML =
          `<td class="text-truncate" style="max-width: 225px;">${email.recipients}</td>
          <td class="text-truncate" style="max-width: 225px;">${email.subject}</td>
          <td class="text-truncate">${email.timestamp}</td>`;  
        } else {
          tr.innerHTML = 
          `<td class="text-truncate" style="max-width: 225px;">${email.sender}</td>
          <td class="text-truncate" style="max-width: 225px;">${email.subject}</td>
          <td class="text-truncate">${email.timestamp}</td>`;
        }
        tr.addEventListener('click', () => {
          load_email(email.id);
        });
        document.querySelector('#emails-view > table').append(tr);
      });
    });
}

function load_email(id) {

  document.querySelector('#emails-view').style.display = 'none';
  document.querySelector('#display-email').style.display = 'block';
  document.querySelector('#compose-view').style.display = 'none';

  document.querySelector('#display-email').innerHTML = '';

  fetch(`/emails/${id}`, {
    method: 'PUT',
    body: JSON.stringify({
        read: true
    })
  })

  fetch(`/emails/${id}`)
  .then(response => response.json())
  .then(email => {
    // Print email
    console.log(email);

    const div = document.createElement('div');
    div.innerHTML = 
    `<strong>From: </strong>${email.sender}<br>
    <strong>To: </strong>${email.recipients}<br>
    <strong>Subject: </strong>${email.subject}<br>
    <strong>Timestamp: </strong>${email.timestamp}<br>
    <button class="btn btn-outline-primary" id="reply">Reply</button>
    <button class="btn btn-outline-success" id="archive">Archive</button>
    <button class="btn btn-outline-danger" id="unarchive">Unarchive</button>
    <br><br>
    ${email.body}`;
    document.querySelector('#display-email').append(div);

    document.querySelector('#reply').addEventListener('click', () => {
      reply(email.id);
    })
    if (email.archived) {
      document.querySelector('#archive').style.display = 'none';
      document.querySelector('#unarchive').addEventListener('click', () => {
        is_archived(email);
        location.reload();
        load_mailbox('inbox');
      })
    } else {
      document.querySelector('#unarchive').style.display = 'none';
      document.querySelector('#archive').addEventListener('click', () => {
        is_archived(email);
        location.reload();
        load_mailbox('inbox');
      })
    }
  })
}

function is_archived(email) {
  if (email.archived) {
    fetch(`/emails/${email.id}`, {
      method: 'PUT',
      body: JSON.stringify({
          archived: false
      })
    })
  } else {
    fetch(`/emails/${email.id}`, {
      method: 'PUT',
      body: JSON.stringify({
          archived: true
      })
    })
  }
}

function reply(id) {

  document.querySelector('#emails-view').style.display = 'none';
  document.querySelector('#display-email').style.display = 'none';
  document.querySelector('#compose-view').style.display = 'block';

  fetch(`/emails/${id}`)
  .then(response => response.json())
  .then(email => {
    document.querySelector('#compose-recipients').value = `${email.sender}`;
    if (email.subject.includes('Re:')) {
      document.querySelector('#compose-subject').value = `${email.subject}`;
    } else {
      document.querySelector('#compose-subject').value = `Re: ${email.subject}`;
    }
    document.querySelector('#compose-body').value = `On ${email.timestamp} ${email.sender} wrote:\n${email.body}`;
  })
}