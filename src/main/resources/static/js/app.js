$(document).ready(() => {
    $("#addFieldsBtn").click(function () {
        $($('.authors-fields-group')[0]).clone().appendTo($('.author-fields'));
    });


    $("#filterForm").submit(async function (e) {
        e.preventDefault();
        const formData = $(this).serialize();

        const response = await fetch(`${API_URL}books/filter?${formData}`);
        const filteredBooks = await response.json();

        const readyBooks = [];
        filteredBooks.forEach(book =>
            readyBooks.push(createBookCard({...book})));

        $(".books-wrapper").empty().append(readyBooks);
    });

    $(".block-inp").click(async function () {
        const secondParent = $(this).parent().parent();
        const id = $(secondParent)
            .parent()
            .data("user-id");
        const isBlocked = $(this).is(':checked');

        const res = await fetch(`${API_URL}users/block-user`, {
            method: "POST",
            body: JSON.stringify({
                id,
                isBlocked
            }),
            headers: {
                [header]: token,
                "Content-Type": "application/json"
            }
        });

        const result = await res.text();

        if (result !== 'ok') {
            return;
        }

        $(secondParent)
            .next()
            .find(".block-date")
            .prop('disabled', !isBlocked);
    });


    $(".block-submit-btn").click(async function () {
        const secondParent = $(this).parent().parent();
        const id = $(secondParent)
            .data("user-id");

        console.log(id);

        const date = new Date($(this)
            .siblings()
            .find(".block-date")
            .val());

        const res = await fetch(`${API_URL}users/set-unblock-date`, {
            method: "POST",
            body: JSON.stringify({
                id,
                date
            }),
            headers: {
                [header]: token,
                "Content-Type": "application/json"
            }
        });
        const result = await res.text();

        if (result === 'ok') {
            alert("OK");
        }
    })

});
