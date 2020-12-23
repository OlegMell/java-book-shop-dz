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
        const isBlocked = $(this).is(':checked');

        $(secondParent)
            .next()
            .find(".block-date")
            .prop('disabled', !isBlocked);
    });


    $(".block-submit-btn").click(async function () {
        const secondParent = $(this).parent().parent();
        const id = $(secondParent)
            .data("user-id");

        const date = new Date($(this)
            .siblings()
            .find(".block-date")
            .val());

        const res = await fetch(`${API_URL}users/set-unblock-date`, {
            method: "POST",
            body: JSON.stringify({
                id,
                date,
                isBlocked: $(this).parent().prev().children().find(".block-inp").is(':checked')
            }),
            headers: {
                [header]: token,
                "Content-Type": "application/json"
            }
        });
        const result = await res.text();

        if (result === 'ok') {
            alert("User  has been blocked!");
        }
    })
});
